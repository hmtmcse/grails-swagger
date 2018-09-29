package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerPaths {

    private HashMap<String, HashMap<Object, Object>> definition = new HashMap<>();

    private String url;

    public SwaggerPaths start(String url){
        this.url = url;
        definition.put(url, new HashMap<>());
        return this;
    }

    public SwaggerPaths addPath(String method, SwaggerPath swaggerPath){
        definition.get(url).put(method, swaggerPath.getDefinition());
        return this;
    }


    public HashMap getDefinition(){
        return this.definition;
    }

}
