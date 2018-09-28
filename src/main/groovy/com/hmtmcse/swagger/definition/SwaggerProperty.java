package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerProperty {


    private HashMap<String, HashMap<Object, Object>> definition = new HashMap<>();
    private String name;


    public SwaggerProperty property(String name, String type){
        this.name = name;
        definition.put(name, SwaggerMap.object().setGet("type", type));
       return this;
    }

    public SwaggerProperty format(String format){
        definition.get(name).put("format", format);
        return this;
    }

    public SwaggerProperty description(String description){
        definition.get(name).put("description", description);
        return this;
    }

    public SwaggerProperty ref(String ref){
        definition.get(name).put("$ref", ref);
        return this;
    }

    public SwaggerProperty example(String example){
        definition.get(name).put("example", example);
        return this;
    }

    public SwaggerProperty setEnum(String[] enums){
        definition.get(name).put("enum", enums);
        return this;
    }

    public HashMap getDefinition(){
        return this.definition;
    }

}
