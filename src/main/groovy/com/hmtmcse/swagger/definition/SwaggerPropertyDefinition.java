package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerPropertyDefinition {



    public HashMap<String, HashMap<Object, Object>> definition = new HashMap<>();
    private String name;

    public SwaggerPropertyDefinition addDefinition(String name, String type){
        this.name = name;
        definition.put(name, SwaggerMap.object().setGet("type", type));
        return this;
    }


    public SwaggerPropertyDefinition addProperties(SwaggerProperty swaggerProperty){
        definition.get(name).put("properties", swaggerProperty.getDefinition());
        return this;
    }


    public HashMap getDefinition(){
        return this.definition;
    }
}
