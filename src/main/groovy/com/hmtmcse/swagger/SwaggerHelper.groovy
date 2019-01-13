package com.hmtmcse.swagger

class SwaggerHelper {

    private String itemType
    private String propertyIn
    private LinkedHashMap itemProperties = [:]


    SwaggerHelper initItem(String itemType, String propertyIn){
        this.itemType = itemType
        this.propertyIn = propertyIn
        return this
    }

    SwaggerHelper addProperties(String name, String dataType){
        itemProperties.put(name, [
                "name" : name,
                "type" : dataType,
                "in" : propertyIn,
        ])
        return this
    }


    LinkedHashMap getAllProperties(){
        return itemProperties
    }

    LinkedHashMap getAllPropertiesWithType(){
        return [
                "type": itemType,
                "properties" : itemProperties
        ]
    }


    public static SwaggerHelper instance(){
        return new SwaggerHelper()
    }
}
