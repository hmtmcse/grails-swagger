package com.hmtmcse.swagger.definition;


public class SwaggerTags extends SwaggerCommonDefinition{

    public Integer index = 0;

    public SwaggerTags(String name, String description){
        definition.put("name", name);
        definition.put("description", description);
    }

    public SwaggerTags setExternalDocs(String url, String description){
        definition.put("externalDocs",
                SwaggerMap.string()
                        .set("url", url)
                        .set("description", description)
                        .get()
        );
        return this;
    }

}
