package com.hmtmcse.swagger.definition;

public class SwaggerPathParameter extends SwaggerCommonDefinition{


    public SwaggerPathParameter start(String in, String name){
        definition.put("in", in);
        definition.put("name", name);
       return this;
    }

    public SwaggerPathParameter description(String description){
        definition.put("description", description);
        return this;
    }

    public SwaggerPathParameter required(){
        definition.put("required", true);
        return this;
    }


    public SwaggerPathParameter type(String type){
        definition.put("type", type);
        return this;
    }



    public SwaggerPathParameter schema(String name){
        definition.put("schema", SwaggerMap.string().setGet("$ref", "#/definitions/" + name));
        return this;
    }

}
