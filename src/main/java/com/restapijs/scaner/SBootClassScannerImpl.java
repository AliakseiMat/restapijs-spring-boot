package com.restapijs.scaner;

import com.restapijs.annotation.JsRestModel;
import com.restapijs.annotation.JsRestModelConfig;
import com.restapijs.exception.ClassScannerException;
import com.restapijs.model.JsModel;
import com.restapijs.model.JsModelConfig;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by aliakseimatsarski on 11/13/16.
 */
public class SBootClassScannerImpl implements ClassScanner {

    private Reflections reflections;

    private Logger log = LoggerFactory.getLogger(SBootClassScannerImpl.class);

    public SBootClassScannerImpl() {
        List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        this.reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new MethodAnnotationsScanner(), new TypeAnnotationsScanner())
                        .setUrls(ClasspathHelper.forPackage(
                "", ClasspathHelper.contextClassLoader(),
                ClasspathHelper.staticClassLoader())));
        log.debug("SBootClassScanner was created.");
    }

    @Override
    public Map<String, List<JsModel>> scanForModels() throws ClassScannerException {
        Map<String, List<JsModel>> modelMap = new TreeMap<String, List<JsModel>>();
        Set<Method> methodsAnnotatedWith = reflections.getMethodsAnnotatedWith(JsRestModel.class);
        for (Method method:methodsAnnotatedWith){
            JsModel jsModel = createJsModel(method);
            if(modelMap.containsKey(jsModel.getName())){
                modelMap.get(jsModel.getName()).add(jsModel);
            } else {
                List<JsModel> jsModelList = new ArrayList<JsModel>();
                jsModelList.add(jsModel);
                modelMap.put(jsModel.getName(), jsModelList);
            }
        }
        log.info("Was found {} js models entities", modelMap.size());

        return modelMap;
    }

    @Override
    public Map<String, JsModelConfig> scanForModelsConfig() throws ClassScannerException {
        Map<String, JsModelConfig> modelConfigMap = new TreeMap<String, JsModelConfig>();
        Set<Class<?>> classAnnotatedWith = reflections.getTypesAnnotatedWith(JsRestModelConfig.class);
        for (Class classs:classAnnotatedWith){
            JsModelConfig jsModelConfig = createModelConfig(classs);
            modelConfigMap.put(jsModelConfig.getName(), jsModelConfig);
        }
        log.info("Was found {} js config entities", modelConfigMap.size());

        return modelConfigMap;
    }

    private JsModelConfig createModelConfig(Class classs){
        JsRestModelConfig jsRestModelConfig = (JsRestModelConfig)classs.getAnnotation(JsRestModelConfig.class);
        JsModelConfig jsModelConfig = new JsModelConfig();
        jsModelConfig.setName(jsRestModelConfig.name());
        jsModelConfig.setFileName(jsRestModelConfig.fileName());
        jsModelConfig.setPath(jsRestModelConfig.path());
        log.debug("Js config was created jsConfig:{}", jsModelConfig.toString());

        return jsModelConfig;
    }

    private JsModel createJsModel(Method method) throws ClassScannerException {
        JsModel jsModel =  new JsModel();
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        if (requestMapping.method().length > 0) {
            jsModel.setMethod(JsModel.RestRequestMethod.valueOf(requestMapping.method()[0].toString()));
        } else {
            throw new ClassScannerException("Endpoint " + method.getName() + " do not define any request method.");
        }
        if (requestMapping.path().length > 0){
            jsModel.setPath(requestMapping.path()[0]);
        } else {
            throw new ClassScannerException("Endpoint " + method.getName() + " do not define any request path.");
        }
        JsRestModel jsRestModelAnnotation = method.getAnnotation(JsRestModel.class);
        jsModel.setName(jsRestModelAnnotation.name());
        log.debug("Js model was created jsModel:{]", jsModel.toString());

        return jsModel;
    }
}
