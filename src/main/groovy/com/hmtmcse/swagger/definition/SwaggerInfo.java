package com.hmtmcse.swagger.definition;

public class SwaggerInfo extends SwaggerCommonDefinition{

    public SwaggerInfo(String title){
        definition.put("title", title);
    }

    public SwaggerInfo contactEmail(String email){
        definition.put("contact", SwaggerMap.string().setGet("email", email));
        return this;
    }

    public SwaggerInfo license(String name, String url){
        definition.put("license", SwaggerMap.string().set("name", name).setGet("url", url));
        return this;
    }

    public SwaggerInfo setExternalDocs(String url, String name){
        definition.put("license",
                SwaggerMap.string()
                        .set("url", url)
                        .set("name", name)
                        .get()
        );
        return this;
    }

    public SwaggerInfo description(String description) {
        definition.put("description", description);
        return this;
    }

    public SwaggerInfo version(String version) {
        definition.put("version", version);
        return this;
    }

    public SwaggerInfo termsOfService(String termsOfService) {
        definition.put("termsOfService", termsOfService);
        return this;
    }

}
