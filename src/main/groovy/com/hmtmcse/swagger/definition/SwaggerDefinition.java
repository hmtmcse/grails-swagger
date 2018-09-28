package com.hmtmcse.swagger.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwaggerDefinition extends SwaggerCommonDefinition{

    public String swagger = "2.0";
    public String host = "http://localhost";
    public String basePath = "";
    public List<String> schemes = Arrays.asList("http", "https");

    public List<SwaggerTags> tags = new ArrayList<>();
    private SwaggerInfo info = null;

    public String paths;
    public String securityDefinitions;
    public String definitions;
    public String externalDocs;


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

    public SwaggerTags setTag(String name, String description){
        SwaggerTags swaggerTags = new SwaggerTags(name, description);
        swaggerTags.index = tags.size();
        tags.add(swaggerTags);
        return tags.get(swaggerTags.index);
    }


    public HashMap<Object, Object> getDefinition(){
        definition.put("swagger", swagger);
        definition.put("host", host);
        definition.put("basePath", basePath);
        definition.put("schemes", schemes);
        if (info != null){
            definition.put("info", info.getDefinition());
        }
        if (tags.size() != 0){
            definition.put("tags", getHashMapList(tags));
        }
        return this.definition;
    }


    private List<HashMap<Object, Object>> getHashMapList(List list){
        List<HashMap<Object, Object>> hasList = new ArrayList<HashMap<Object, Object>>();
        SwaggerCommonDefinition swaggerCommonDefinition;
        for (int i = 0; i < list.size(); i++){
            swaggerCommonDefinition = (SwaggerCommonDefinition) list.get(i);
            hasList.add(swaggerCommonDefinition.getDefinition());
        }
        return hasList;
    }

}
