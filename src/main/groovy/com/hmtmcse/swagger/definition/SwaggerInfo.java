package com.hmtmcse.swagger.definition;

import java.util.HashMap;

public class SwaggerInfo {

    public HashMap<Object, Object> definition = new HashMap<>();

    public SwaggerInfo(String title){
        definition.put("title", title);
    }

    public SwaggerInfo contactEmail(String email){
        definition.put("contact", SwaggerMap.string().setGet("email", email));
        return this;
    }

    public HashMap<Object, Object> getDefinition(){
        return this.definition;
    }
}
