package com.restapijs.jswriter;

import com.restapijs.model.JsModel;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by aliakseimatsarski on 11/28/16.
 */
public interface JsWriter {

    JsWriter addNameSpace(String nameSpace) throws IOException;

    JsWriter addJsModel(List<JsModel> jsModel, String modelName) throws IOException;

    File writeFile() throws IOException;
}
