package com.hmtmcse.swagger.definition;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class SwaggerPaths {

    private LinkedHashMap<String, LinkedHashMap<Object, Object>> definition = new LinkedHashMap<>();

    private String url;

    public SwaggerPaths start(String url){
        this.url = url;
        definition.put(url, new LinkedHashMap<>());
        return this;
    }

    public SwaggerPaths addPath(String method, SwaggerPath swaggerPath){
        definition.get(url).put(method, swaggerPath.getDefinition());
        return this;
    }

    public Boolean isExist(String url) {
        return definition.get(url) != null;
    }

    public HashMap getDefinition(){
        return this.definition;
    }

}
