package com.hmtmcse.swagger.definition;

import java.util.*;

public class SwaggerProperty {


    private LinkedHashMap<String, LinkedHashMap<Object, Object>> definition = new LinkedHashMap<>();
    private String name;
    private List<Object> propertyList = new ArrayList<>();


    public SwaggerProperty property(String name, String type){
        this.name = name;
        definition.put(name, SwaggerMap.object().set("name", name).setGet("type", type));
       return this;
    }

    public SwaggerProperty objectProperty(String name, SwaggerProperty swaggerProperty){
        this.name = name;
        definition.put(name,
                SwaggerMap.object().set("name", name)
                        .set("type", SwaggerConstant.SWAGGER_DT_OBJECT)
                        .setGet("properties", swaggerProperty.getDefinition())
        );
        return this;
    }

    public SwaggerProperty arrayProperty(String name, SwaggerProperty swaggerProperty){
        arrayPropertyMap(name, swaggerProperty.getDefinition());
        return this;
    }


    public SwaggerProperty arrayPropertyMap(String name, LinkedHashMap map){
        this.name = name;
        definition.put(name,
                SwaggerMap.object()
                        .set("type", SwaggerConstant.SWAGGER_DT_ARRAY)
                        .setGet("items", SwaggerMap.object()
                                .set("type", SwaggerConstant.SWAGGER_DT_OBJECT).setGet("properties", map)
                        ));
        return this;
    }

    public SwaggerProperty arrayPropertyList(String name, String type, Object example) {
        return arrayPropertyList(name, type, example, null);
    }

    public SwaggerProperty arrayPropertyList(String name, String type, Object example, String format) {
        this.name = name;

        LinkedHashMap<Object, Object> swaggerMap = SwaggerMap.object().setGet("type", type);
        if (example != null) {
            swaggerMap.put("example", example);
        }

        if (format != null) {
            swaggerMap.put("format", format);
        }

        definition.put(name,
                SwaggerMap.object()
                        .set("type", SwaggerConstant.SWAGGER_DT_ARRAY)
                        .setGet("items", swaggerMap));
        return this;
    }

    public SwaggerProperty addFromExistingObjectProperty(String name, SwaggerProperty swaggerProperty){
        definition.put(name, swaggerProperty.getDefinitionWithType().get(name));
        return this;
    }



    public SwaggerProperty property(String name){
        this.name = name;
        definition.put(name, SwaggerMap.object().setGet("name", name));
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

    public SwaggerProperty example(Object example){
        definition.get(name).put("example", example);
        return this;
    }

    public SwaggerProperty in(String inQuery){
        definition.get(name).put("in", inQuery);
        return this;
    }

    public SwaggerProperty required(){
        definition.get(name).put("required", true);
        return this;
    }

    public SwaggerProperty setEnum(String[] enums){
        definition.get(name).put("enum", enums);
        return this;
    }

    public SwaggerProperty otherProperty(String key, String value){
        definition.get(name).put(key, value);
        return this;
    }

    public List<Object>getPropertyList(){
        return propertyList;
    }

    public void addToList(){
        propertyList.add(definition.get(name));
    }

    public void addToListWithType(String type){
        if (type != null){
            in(type);
        }
        propertyList.add(definition.get(name));
    }

    public LinkedHashMap<String, LinkedHashMap<Object, Object>> getDefinitionWithType(){
        return this.definition;
    }

    public LinkedHashMap getDefinition(){
        return this.definition;
    }

}
