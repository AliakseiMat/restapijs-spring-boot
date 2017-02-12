package com.restapijs.model;

/**
 * Created by aliakseimatsarski on 11/13/16.
 */
public class JsModel {

    RestRequestMethod method;

    String path;

    String name;

    public JsModel(RestRequestMethod method, String path, String name) {
        this.method = method;
        this.path = path;
    }

    public JsModel() {
    }

    public RestRequestMethod getMethod() {
        return method;
    }

    public void setMethod(RestRequestMethod method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum RestRequestMethod {
        GET,
        HEAD,
        POST,
        PUT,
        PATCH,
        DELETE,
        OPTIONS,
        TRACE;
    }
}
