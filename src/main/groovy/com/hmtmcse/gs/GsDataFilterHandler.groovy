package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsCriteriaData
import com.hmtmcse.gs.data.GsMapKeyValue
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerProperty

class GsDataFilterHandler {

    private static String inType = null




    public static GsDataFilterHandler instance() {
        return new GsDataFilterHandler()
    }




    public static SwaggerProperty getRequestParams(Boolean isList = true, Boolean isJson = false , List allowedProperty = []){
        SwaggerProperty swaggerProperty = new SwaggerProperty()
        if (isList){
            swaggerProperty.property(GsConstant.OFFSET, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
            swaggerProperty.property(GsConstant.MAX, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
            if (!isJson){
                swaggerProperty.property(GsConstant.ORDER_PROPERTY, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
                swaggerProperty.property(GsConstant.ORDER, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            }
        }
        if (!isJson){
            swaggerProperty.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            swaggerProperty.property(GsConstant.PROPERTY_VALUE, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)

            if (allowedProperty.size()) {
                swaggerProperty.otherProperty(GsConstant.ALLOWED_PROPERTY, allowedProperty?.name?.join(", ")).addToListWithType(inType)
            }
        }
        return swaggerProperty
    }


    public static SwaggerProperty swaggerWhere(Boolean isList = true, List allowedProperty = []) {
        SwaggerProperty property = new SwaggerProperty()
        property.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING)
                .example(GsConstant.PROPERTY_VALUE)
        if (allowedProperty.size()) {
            property.otherProperty(GsConstant.ALLOWED_PROPERTY, allowedProperty?.name?.join(", "))
        }

        SwaggerProperty conditions = new SwaggerProperty()
        String name = GsConstant.EQUAL
        name += "|" + GsConstant.AND
        name += "|" + GsConstant.OR


        SwaggerProperty where = new SwaggerProperty().property(GsConstant.WHERE)
        if (isList){
            where = getRequestParams(isList, true)

            name += "|" + GsConstant.ORDER
//            name += "|" + GsConstant.BETWEEN
            name += "|" + GsConstant.NOT_EQUAL
            name += "|" + GsConstant.LESS_THAN
            name += "|" + GsConstant.LESS_THAN_EQUAL
            name += "|" + GsConstant.GETTER_THAN
            name += "|" + GsConstant.GETTER_THAN_EQUAL
//            name += "|" + GsConstant.IN_LIST
            name += "|" + GsConstant.LIKE
        }else{
            where.description(GsConfigHolder.multipleMatchInDetails())
        }

        conditions.objectProperty(name, property)
        where.objectProperty(GsConstant.WHERE, conditions)
        return where
    }



    public static SwaggerProperty swaggerPostReadRequest(Boolean isList = true, List allowedProperty = []) {
        return swaggerWhere(isList, allowedProperty)
    }

    public static SwaggerProperty swaggerGetReadRequest(Boolean isList = true, List allowedProperty = []) {
        inType = SwaggerConstant.IN_QUERY
        return getRequestParams(isList, false, allowedProperty)
    }


    public static SwaggerProperty swaggerPostWriteRequest(List allowedProperty = []) {
        return swaggerWhere(false, allowedProperty)
    }

}
