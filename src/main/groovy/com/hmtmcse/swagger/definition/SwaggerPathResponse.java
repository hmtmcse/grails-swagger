package com.hmtmcse.swagger.definition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class SwaggerPathResponse {


    private LinkedHashMap<String, LinkedHashMap<Object, Object>> definition = new LinkedHashMap<>();
    private String httpCode;
    private LinkedHashMap<String, List<Object>> schemaAnyOf = new LinkedHashMap<>();

    public SwaggerPathResponse start(String httpCode){
        this.httpCode = httpCode;
        definition.put(httpCode, new LinkedHashMap<>());
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

    public SwaggerPathResponse schemaOnly(String item){
        definition.get(httpCode).put("schema", SwaggerMap.object().setGet("$ref", "#/definitions/" + item));
        return this;
    }

    public SwaggerPathResponse schemaAnyOf(String name){
        schemaAnyOf.computeIfAbsent("anyOf", k -> new ArrayList<>());
        schemaAnyOf.get("anyOf").add(SwaggerMap.object().setGet("$ref", "#/definitions/" + name));
        definition.get(httpCode).put("schema", schemaAnyOf);
        return this;
    }

    public LinkedHashMap getDefinition(){
        return this.definition;
    }
}
