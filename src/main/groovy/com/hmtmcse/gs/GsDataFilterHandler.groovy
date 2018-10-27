package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsMapKeyValue
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerProperty

class GsDataFilterHandler {

    private static String inType = null




    Closure readCriteriaProcessor(GsParamsPairData gsParamsPairData){
        switch (gsParamsPairData.httpMethod) {
            case GsConstant.POST:
                if (gsParamsPairData.params && gsParamsPairData.params.where){
                    return createCriteriaBuilder(gsParamsPairData.params.where)
                }
                return {}
            case GsConstant.GET:
                return readGetMethodCriteriaProcessor(gsParamsPairData)
        }
    }


    Closure readGetMethodCriteriaProcessor(GsParamsPairData gsParamsPairData) {
        Map params = gsParamsPairData.params
        if (params && params.propertyName && params.propertyValue) {
            return {
                eq(params.propertyName, params.propertyValue)
            }
        }
        return {}
    }


    Map readPaginationWithSortProcessor(GsParamsPairData gsParamsPairData) {
        Map refineParams = [:]
        Map params = gsParamsPairData.params
        refineParams.max = params.max ?: GsConfigHolder.itemsPerPage()
        refineParams.offset = params.offset ?: 0
        if (!gsParamsPairData.httpMethod.equals(GsConstant.GET)) {
            return refineParams
        }
        if (!params.orderProperty || !params.order) {
            refineParams.sort = GsConfigHolder.sortColumn()
            refineParams.order = GsConfigHolder.sortOrder()
        } else {
            refineParams.sort = params.orderProperty
            refineParams.order = params.order
        }
        return refineParams
    }


    public GsParamsPairData getParamsPair(Map params) {
        GsParamsPairData gsParamsPairData = new GsParamsPairData()
        if (params.gsHttpRequestMethod) {
            switch (params.gsHttpRequestMethod.toLowerCase()) {
                case GsConstant.POST:
                    gsParamsPairData.httpMethod = GsConstant.POST
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData
                case GsConstant.GET:
                    gsParamsPairData.httpMethod = GsConstant.GET
                    gsParamsPairData.params = params
                    return gsParamsPairData
            }
        }
        return gsParamsPairData
    }

    public Map filterAllowedField(List allowedFields, Map params) {
        Map refineParams = [:]
        allowedFields.each { String fieldName ->
            refineParams.put(fieldName, params[fieldName])
        }
        return refineParams
    }


    public GsInternalResponse saveUpdateDataFilter(GsApiActionDefinition definition, Map params) {
        GsInternalResponse internalResponse = GsInternalResponse.instance()
        GsParamsPairData gsParamsPairData = getParamsPair(params)
        Map paramsData = gsParamsPairData.params
        definition.getRequestProperties().each { String name, GsApiRequestProperty properties ->
            if (paramsData.containsKey(name)) {
                internalResponse.params(name, paramsData[name])
            } else {
                if (properties.isRequired) {
                    internalResponse.addErrorDetail(name, properties.errorMessage ?: GsConfigHolder.requiredFieldMissing())
                    return internalResponse
                }
            }
        }
        internalResponse.isSuccess = true
        return internalResponse
    }

    public static GsDataFilterHandler instance(){
        return new GsDataFilterHandler()
    }


    public Closure createCriteriaBuilder(Map where, Boolean andOr = false, String details = null) {
        GsMapKeyValue gsMapKeyValue
        Closure criteria = {
            if (where[GsConstant.ORDER_PROPERTY] && where[GsConstant.ORDER] && (where[GsConstant.ORDER].equals(GsConstant.ASC) || where[GsConstant.ORDER].equals(GsConstant.DESC))) {
                order(where[GsConstant.ORDER_PROPERTY], where[GsConstant.ORDER])
            } else {
                order(GsConfigHolder.sortColumn(), GsConfigHolder.sortOrder())
            }

            if (!andOr) {
                if (where[GsConstant.SELECT]) {
                    projections {
                        where[GsConstant.SELECT]?.each {
                            property(it)
                        }
                    }
                }

                if (where[GsConstant.AND]) {
                    and {
                        createCriteriaBuilder(where[GsConstant.AND], true, details)
                    }
                }

                if (where[GsConstant.OR]) {
                    or {
                        createCriteriaBuilder(where[GsConstant.OR], true, details)
                    }
                }
            }

            gsMapKeyValue = getMapKeyValue(where, GsConstant.EQUAL)
            if (gsMapKeyValue) {
                eq(gsMapKeyValue.key, gsMapKeyValue.value)
            }

            if (details == null){
                gsMapKeyValue = getMapKeyValue(where, GsConstant.NOT_EQUAL)
                if (gsMapKeyValue) {
                    ne(gsMapKeyValue.key, gsMapKeyValue.value)
                }

                gsMapKeyValue = getMapKeyValue(where, GsConstant.LESS_THAN)
                if (gsMapKeyValue) {
                    lt(gsMapKeyValue.key, gsMapKeyValue.value)
                }


                gsMapKeyValue = getMapKeyValue(where, GsConstant.LESS_THAN_EQUAL)
                if (gsMapKeyValue) {
                    le(gsMapKeyValue.key, gsMapKeyValue.value)
                }


                gsMapKeyValue = getMapKeyValue(where, GsConstant.GETTER_THAN)
                if (gsMapKeyValue) {
                    gt(gsMapKeyValue.key, gsMapKeyValue.value)
                }


                gsMapKeyValue = getMapKeyValue(where, GsConstant.GETTER_THAN_EQUAL)
                if (gsMapKeyValue) {
                    ge(gsMapKeyValue.key, gsMapKeyValue.value)
                }

                gsMapKeyValue = getMapKeyValue(where, GsConstant.LIKE)
                if (gsMapKeyValue) {
                    like(gsMapKeyValue.key, gsMapKeyValue.value)
                }
            }
        }
        return criteria
    }


    GsMapKeyValue getMapKeyValue(Map map, String key){
        GsMapKeyValue gsMapKeyValue = new GsMapKeyValue()
        if (!map[key]){
            return null
        }
        map = map[key]
        map.keySet().each {
            gsMapKeyValue.key = it
        }
        gsMapKeyValue.value = map.get(gsMapKeyValue.key)
        return gsMapKeyValue
    }

    public Closure conditonBuilder(Map params){
        Closure conditions = {
            if (params[GsConstant.SELECT]){
            }
        }
        return conditions
    }


    public static SwaggerProperty getRequestParams(Boolean isList = true, Boolean isJson = false , List allowedProperty = []){
        SwaggerProperty swaggerProperty = new SwaggerProperty()
        if (isList){
            swaggerProperty.property(GsConstant.OFFSET, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
            swaggerProperty.property(GsConstant.MAX, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
            swaggerProperty.property(GsConstant.ORDER_PROPERTY, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            swaggerProperty.property(GsConstant.ORDER, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
        }
        if (!isJson){
            swaggerProperty.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            swaggerProperty.property(GsConstant.PROPERTY_VALUE, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)

            if (allowedProperty.size()) {
                swaggerProperty.otherProperty(GsConstant.ALLOWED_PROPERTY, allowedProperty.join(", ")).addToListWithType(inType)
            }
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
