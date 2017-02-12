package com.restapijs.model;

/**
 * Created by aliakseimatsarski on 1/29/17.
 */
public class JsModelConfig {

    private String name;

    private String fileName;

    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public JsModelConfig() {
    }

    public JsModelConfig(String name, String fileName, String path) {

        this.name = name;
        this.fileName = fileName;
        this.path = path;
    }
}
