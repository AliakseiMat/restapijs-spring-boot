package com.restapijs.jswriter.impl;

import com.restapijs.jswriter.JsWriter;
import com.restapijs.model.JsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aliakseimatsarski on 11/28/16.
 */
public class DefaultJsWriter implements JsWriter {

    private Logger log = LoggerFactory.getLogger(DefaultJsWriter.class);

    private final String PATH_TO_TEMPLATES = "/jstemplates/";

    private final String PATH_TO_JS_MODEL_TEMPLATE = "js_model_template";

    private Path jsFile;

    private String nameSpace;

    private List<String> lines = new ArrayList<String>();


    public DefaultJsWriter(Path jsFile) {
        this.jsFile = jsFile;
    }

    @Override
    public JsWriter addNameSpace(String nameSpace) throws IOException {
        this.nameSpace = nameSpace;
        String[] nameSpaceArray = nameSpace.split("\\.");
        List<String> lines = new ArrayList<String>();
        String nameSpaceLevels = nameSpaceArray[0];
        lines.add("var " + nameSpaceLevels + " = " + nameSpaceLevels + " || {};");
        for (int i=1;i < nameSpaceArray.length;i++){
            nameSpaceLevels = nameSpaceLevels + "." + nameSpaceArray[i];
            lines.add(nameSpaceLevels + " = " + nameSpaceLevels + " || {};");
        }
        Files.write(jsFile, lines, Charset.forName("UTF-8"));

        return this;
    }

    @Override
    public JsWriter addJsModel(List<JsModel> jsModelList, String modelName) throws IOException {
        InputStream is = getClass().getResourceAsStream(PATH_TO_TEMPLATES + PATH_TO_JS_MODEL_TEMPLATE);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null){
            line = line.replace("$name-space", this.nameSpace);
            line = line.replace("$model-name", modelName);
            line = line.replace("$get-method-path", findMethodPath(jsModelList, JsModel.RestRequestMethod.GET));
            line = line.replace("$save-method-path", findMethodPath(jsModelList, JsModel.RestRequestMethod.PUT));
            line = line.replace("$create-method-path", findMethodPath(jsModelList, JsModel.RestRequestMethod.POST));
            line = line.replace("$delete-method-path", findMethodPath(jsModelList, JsModel.RestRequestMethod.DELETE));
            addLine(line);
        }

        return this;
    }

    @Override
    public File writeFile() throws IOException {
        Files.write(jsFile, this.lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);

        return jsFile.toFile();
    }

    private String findMethodPath(List<JsModel> jsModelList, JsModel.RestRequestMethod method){
        for (JsModel jsModel:jsModelList){
            if (jsModel.getMethod().equals(method)){
                return jsModel.getPath();
            }
        }

        return "";
    }

    private void addLine(String line){
        this.lines.add(line);
    }
}
