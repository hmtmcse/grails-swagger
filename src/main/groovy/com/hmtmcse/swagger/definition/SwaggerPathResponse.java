package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerPathResponse {


    private HashMap<String, HashMap<Object, Object>> definition = new HashMap<>();
    private String httpCode;

    public SwaggerPathResponse start(String httpCode){
        this.httpCode = httpCode;
        definition.put(httpCode, new HashMap<>());
        return this;
    }


    public SwaggerPathResponse description(String description){
        definition.get(httpCode).put("description", description);
        return this;
    }

    public SwaggerPathResponse schema(String type, String item){
        definition.get(httpCode).put("schema", SwaggerMap.object()
                .set("type", type)
                .set("items", SwaggerMap.string().setGet("$ref", "#/definitions/" + item)).get());
        return this;
    }

    public HashMap getDefinition(){
        return this.definition;
    }
}
