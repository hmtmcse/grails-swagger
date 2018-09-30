package com.hmtmcse.swagger.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwaggerDefinition extends SwaggerCommonDefinition{

    private String swagger = "2.0";
    private List<String> schemes = new ArrayList<>();

    private List<SwaggerTags> tags = new ArrayList<>();
    private SwaggerInfo swaggerInfo = null;
    private SwaggerPropertyDefinition propertyDefinition = null;

    private SwaggerPaths paths;
    private String securityDefinitions;
    private String externalDocs;
    private String host;
    private String basePath;


    public SwaggerDefinition(){
        definition = new HashMap<>();
        schemes.add("http");
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


    public SwaggerPaths startPaths(String url){
        if (this.paths == null){
            this.paths = new SwaggerPaths();
        }
        this.paths.start(url);
        return this.paths;
    }

    public SwaggerInfo info(String title){
        this.swaggerInfo = new SwaggerInfo(title);
        return this.swaggerInfo;
    }


    public SwaggerTags addTag(String name, String description){
        SwaggerTags swaggerTags = new SwaggerTags(name, description);
        swaggerTags.index = tags.size();
        tags.add(swaggerTags);
        return tags.get(swaggerTags.index);
    }

    public SwaggerPropertyDefinition addDefinition(String name, String type){
        if (this.propertyDefinition == null){
            this.propertyDefinition = new SwaggerPropertyDefinition();
        }
        return this.propertyDefinition.addDefinition(name, type);
    }

    public SwaggerProperty property(String name, String type){
        return new SwaggerProperty().property(name, type);
    }

    public SwaggerPath path(){
        return new SwaggerPath();
    }

    public SwaggerPathParameter pathParameter(String in, String name){
        return new SwaggerPathParameter().start(in, name);
    }

    public SwaggerPathResponse pathResponse(String httpCode){
        return new SwaggerPathResponse().start(httpCode);
    }

    public SwaggerDefinition host(String host){
        this.host = host;
        return this;
    }

    public SwaggerDefinition basePath(String basePath){
        this.basePath = basePath;
        return this;
    }


    public SwaggerDefinition scheme(String scheme){
        schemes.add(scheme);
        return this;
    }

    public SwaggerDefinition swagger(String swagger){
        this.swagger = swagger;
        return this;
    }

    public HashMap<Object, Object> getDefinition(){
        definition.put("swagger", swagger);
        definition.put("host", host);
        definition.put("basePath", basePath);
        if (swaggerInfo != null){
            definition.put("info", swaggerInfo.getDefinition());
        }
        definition.put("schemes", schemes);
        if (tags.size() != 0){
            definition.put("tags", getHashMapList(tags));
        }

        if (paths != null){
            definition.put("paths", paths.getDefinition());
        }

        if (propertyDefinition != null){
            definition.put("definitions", propertyDefinition.getDefinition());
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
