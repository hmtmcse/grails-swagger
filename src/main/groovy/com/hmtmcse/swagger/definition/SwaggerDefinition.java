package com.hmtmcse.swagger.definition;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwaggerDefinition {

    public String swagger = "2.0";
    public String host = "http://localhost";
    public String basePath = "";
    public List<String> schemes = Arrays.asList("http", "https");

    public String tags;
    private SwaggerInfo info = null;

    public String paths;
    public String securityDefinitions;
    public String definitions;
    public String externalDocs;


    public HashMap<Object, Object> definition;

    public SwaggerDefinition(){
        definition = new HashMap<>();
    }


    public SwaggerDefinition setExternalDocs(String url, String description){
        definition.put("externalDocs",
                SwaggerMap.string()
                        .set("url", url)
                        .set("description", description)
                        .get()
        );
        return this;
    }

    public SwaggerInfo setInfo(String title){
        this.info = new SwaggerInfo(title);
        return this.info;
    }

    public HashMap<Object, Object> getDefinition(){
        definition.put("swagger", swagger);
        definition.put("host", host);
        definition.put("basePath", basePath);
        definition.put("schemes", schemes);
        if (info != null){
            definition.put("info", info.getDefinition());
        }
        return this.definition;
    }

}
