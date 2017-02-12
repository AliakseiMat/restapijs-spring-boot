package com.restapijs.scaner;

import com.restapijs.exception.ClassScannerException;
import com.restapijs.model.JsModel;
import com.restapijs.model.JsModelConfig;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseimatsarski on 11/13/16.
 */
public interface ClassScanner {

    Map<String, List<JsModel>> scanForModels() throws ClassScannerException;

    Map<String, JsModelConfig> scanForModelsConfig() throws ClassScannerException;
}
