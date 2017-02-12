package com.restapijs;

import com.restapijs.exception.ClassScannerException;
import com.restapijs.jswriter.JsWriter;
import com.restapijs.jswriter.JsWriterBuilder;
import com.restapijs.model.JsModel;
import com.restapijs.model.JsModelConfig;
import com.restapijs.scaner.ClassScanner;
import com.restapijs.scaner.SBootClassScannerImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseimatsarski on 1/29/17.
 */
@Configuration
@ComponentScan(basePackageClasses = {RestApiJsConfiguration.class})
public class RestApiJsConfiguration implements ApplicationListener<ApplicationReadyEvent> {

    private final String JS_OUTPUT_BASE_PATH_ENV_PROP = "restjs.js.path";
    private final String JS_NAMESPACE_ENV_PROP = "restapijs.js.namespace";
    private final String DEFAULT_JS_OUTPUT_BASE_PATH = "src/main/resources/static/js/models/";
    private final String DEFAULT_JS_NAMESPACE = "RestApiJs";

    @Autowired
    private Environment env;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            ClassScanner classScanner = new SBootClassScannerImpl();
            Map<String, List<JsModel>> jsModelMap = classScanner.scanForModels();
            Map<String, JsModelConfig> jsModelConfigMap = classScanner.scanForModelsConfig();
            String path = env.getProperty(JS_OUTPUT_BASE_PATH_ENV_PROP);
            String nameSpace = env.getProperty(JS_NAMESPACE_ENV_PROP, DEFAULT_JS_NAMESPACE);
            path = StringUtils.isNotBlank(path)?path:DEFAULT_JS_OUTPUT_BASE_PATH;
            for (Map.Entry<String, List<JsModel>> entry:jsModelMap.entrySet()){
                JsModelConfig jsModelConfig = jsModelConfigMap.get(entry.getKey());
                if (jsModelConfig == null){
                    jsModelConfig = new JsModelConfig(entry.getKey(), entry.getKey().toLowerCase() + ".js", "");
                }
                JsWriter jsWriter = new JsWriterBuilder().createBuilder(path, jsModelConfig).build().getDefaultWriter();
                jsWriter.addNameSpace(nameSpace)
                        .addJsModel(entry.getValue(), entry.getKey())
                        .writeFile()
                        .createNewFile();
            }
        } catch (ClassScannerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
