package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerProperty

class GsDataFilterHandler {

    public Map paginations(Map params){
        Map refineParams = [:]
        refineParams[GsConstant.MAX] = params[GsConstant.MAX] ?: GsConfigHolder.itemsPerPage()
        refineParams[GsConstant.OFFSET] = params[GsConstant.OFFSET] ?: 0
        return refineParams
    }


    public Closure createCriteriaBuilder(Map params){
        Closure criteria = {
            if (params[GsConstant.ORDER_COLUMN] && params[GsConstant.ORDER] && (params[GsConstant.ORDER].equals(GsConstant.ASC) || params[GsConstant.ORDER].equals(GsConstant.DESC))){
                order(params[GsConstant.ORDER_COLUMN], params[GsConstant.ORDER])
            }
            if (params[GsConstant.SELECT]){
                projections {
                    params[GsConstant.SELECT]?.each {
                        property(it)
                    }
                }
            }
        }
        return criteria
    }

    public Closure conditonBuilder(Map params){
        Closure conditions = {
            if (params[GsConstant.SELECT]){
            }
        }
        return conditions
    }


    public static SwaggerProperty getRequestParams(Boolean isList = true, String json = null){
        SwaggerProperty swaggerProperty = new SwaggerProperty()
        if (isList){
            swaggerProperty.property(GsConstant.OFFSET, SwaggerConstant.SWAGGER_DT_INTEGER)
            swaggerProperty.property(GsConstant.MAX, SwaggerConstant.SWAGGER_DT_INTEGER)
        }
        if (json == null){
            swaggerProperty.property(GsConstant.ORDER_PROPERTY, SwaggerConstant.SWAGGER_DT_STRING)
            swaggerProperty.property(GsConstant.ORDER, SwaggerConstant.SWAGGER_DT_STRING)
            swaggerProperty.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING)
            swaggerProperty.property(GsConstant.PROPERTY_VALUE, SwaggerConstant.SWAGGER_DT_STRING)
        }
        return swaggerProperty
    }


    public static SwaggerProperty swaggerWhere(Boolean isList = true, List allowedProperty = []) {
        SwaggerProperty property = new SwaggerProperty()
        property.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING)
                .example(GsConstant.PROPERTY_VALUE)
        if (allowedProperty.size()) {
            property.otherProperty(GsConstant.ALLOWED_PROPERTY, allowedProperty.join(", "))
        }

        SwaggerProperty conditions = new SwaggerProperty()
        String name = GsConstant.EQUAL
        name += "|" + GsConstant.AND
        name += "|" + GsConstant.OR


        SwaggerProperty where = new SwaggerProperty()
        if (isList){
            where = getRequestParams(isList, "Yes")

            name += "|" + GsConstant.ORDER
            name += "|" + GsConstant.BETWEEN
            name += "|" + GsConstant.NOT_EQUAL
            name += "|" + GsConstant.LESS_THAN
            name += "|" + GsConstant.LESS_THAN_EQUAL
            name += "|" + GsConstant.GETTER_THAN
            name += "|" + GsConstant.GETTER_THAN_EQUAL
            name += "|" + GsConstant.IN_LIST
            name += "|" + GsConstant.LIKE
        }else{
            where.description(GsConfigHolder.multipleMatchInDetails())
        }

        conditions.objectProperty(name, property)

        where.objectProperty(GsConstant.WHERE, where)
        return where
    }


    public static SwaggerProperty swaggerPostReadRequest(Boolean isList = true, List allowedProperty = []) {

    }

    public static SwaggerProperty swaggerPostWriteRequest(List allowedProperty = []) {

    }

}
