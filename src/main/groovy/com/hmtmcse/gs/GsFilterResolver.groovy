package com.hmtmcse.gs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.hmtmcse.gs.data.GsFilteredData
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.data.GsWhereData
import com.hmtmcse.gs.data.GsWhereFilterProperty
import grails.web.servlet.mvc.GrailsParameterMap

class GsFilterResolver {


//    private final Map whereSwaggerMap = [
//            GsConstant.EQUAL            : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.ORDER            : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.NOT_EQUAL        : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.LESS_THAN        : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.LESS_THAN_EQUAL  : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.GETTER_THAN      : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.GETTER_THAN_EQUAL: [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.LIKE             : [
//                    "propertyName" : "propertyValue",
//                    "propertyName1": "propertyValue",
//            ],
//            GsConstant.IN_LIST          : ["propertyName", "propertyName1"],
//            GsConstant.BETWEEN          : [
//                    "propertyName": ["propertyName": "propertyValue"]
//            ],
//            GsConstant.SELECT           : ["propertyName", "propertyName1"],
//            GsConstant.COUNT            : true,
//    ]


    public GsParamsPairData getParamsPair(GrailsParameterMap params) {
        GsParamsPairData gsParamsPairData = new GsParamsPairData()
        gsParamsPairData.rawParams = params
        if (params.gsHttpRequestMethod) {
            switch (params.gsHttpRequestMethod.toLowerCase()) {
                case GsConstant.POST:
                    gsParamsPairData.httpMethod = GsConstant.POST
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.DELETE:
                    gsParamsPairData.httpMethod = GsConstant.DELETE
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.PUT:
                    gsParamsPairData.httpMethod = GsConstant.PUT
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.GET:
                    gsParamsPairData.httpMethod = GsConstant.GET
                    gsParamsPairData.params = params
                    return gsParamsPairData.initFilteredGrailsParams()
            }
        }
        return gsParamsPairData
    }


    public GsFilteredData resolveFilterData(GsParamsPairData paramsPairData, GsDataFilterOrganizer gsDataFilter) {
        GsFilteredData gsFilterData = new GsFilteredData()
        if (gsDataFilter.enableQueryFilter && paramsPairData.params && paramsPairData.httpMethod.equals(GsConstant.GET)) {
            if (paramsPairData.params.propertyName && paramsPairData.params.propertyValue) {
                gsFilterData.propertyName = paramsPairData.params.propertyName
                gsFilterData.propertyValue = paramsPairData.params.propertyValue
                gsFilterData.where.equal.put(gsFilterData.propertyName.toString(), gsFilterData.propertyValue)
            }
        } else {
            if (paramsPairData.params && paramsPairData.params.where) {
                ObjectMapper objectMapper = new ObjectMapper()
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                try {
                    String json = paramsPairData.params.where.toString()
                    gsFilterData.where = objectMapper.readValue(json, GsWhereData.class)
                } catch (IOException e) {
                    println("Exception from JSON Where Parser: " + e.getMessage())
                }
            }
        }

        if (gsDataFilter.enablePaginationAndSorting) {
            gsFilterData.max = paramsPairData.params.max ?: GsConfigHolder.itemsPerPage()
            gsFilterData.offset = paramsPairData.params.offset ?: 0
            gsFilterData.offsetMaxSort = [max: gsFilterData.max, offset: gsFilterData.offset]

            if (!paramsPairData.params.orderProperty || !paramsPairData.params.order || !gsDataFilter.whereAllowedPropertyMap.get(paramsPairData.params.orderProperty)) {
                gsFilterData.orderProperty = GsConfigHolder.sortColumn()
                gsFilterData.order = GsConfigHolder.sortOrder()
                gsFilterData.offsetMaxSort.put("sort", gsFilterData.orderProperty)
                gsFilterData.offsetMaxSort.put("order", gsFilterData.order)
            } else {
                gsFilterData.orderProperty = paramsPairData.params.orderProperty
                gsFilterData.order = paramsPairData.params.order
                gsFilterData.where.order.put(gsFilterData.orderProperty.toString(), gsFilterData.order)
            }
        }
        return gsFilterData

    }


    public Closure resolveWhereClosure(GsWhereData whereData, GsDataFilterOrganizer gsDataFilter) throws GsValidationException {
        if (whereData == null || gsDataFilter.whereAllowedPropertyMap.size() == 0) {
            return {}
        }

        Closure criteria = {
            GsWhereFilterProperty whereFilterProperty
            def resolveAllCriteria
            resolveAllCriteria = { GsWhereData where ->
                where.equal.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        eq(key, (whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value))
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }

                where.notEqual.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        ne(key, whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value)
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }


                where.lessThan.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        lt(key, (whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value))
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }


                where.lessThanEqual.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        le(key, (whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value))
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }


                where.greaterThan.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        gt(key, (whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value))
                    }
                }


                where.greaterThanEqual.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        ge(key, (whereFilterProperty.isTypeCast ? GsReflectionUtil.castToGSObject(whereFilterProperty.dataType, value) : value))
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }


                where.like.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        like(key, value)
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }

                where.inList.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty && where.inList.get(key)) {
                        'in'(key, where.inList.get(key))
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }

                where.between.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty && where.between.get(key)) {
                        where.between.get(key).each { String field1, String field2 ->
                            between(key, field1, field2)
                        }
                    }
                }

                if (where.and) {
                    and {
                        resolveAllCriteria.call(where)
                    }
                }

                if (where.or) {
                    or {
                        resolveAllCriteria.call(where)
                    }
                }
            }

            if (whereData.order) {
                whereData.order.each { String key, Object value ->
                    whereFilterProperty = gsDataFilter.whereAllowedPropertyMap.get(key)
                    if (whereFilterProperty) {
                        order(key, value)
                    } else {
                        throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(key))
                    }
                }
            } else {
                order(GsConfigHolder.sortColumn(), GsConfigHolder.sortOrder())
            }

            if (whereData.count) {
                projections {
                    if (whereData.count) {
                        count()
                    }
                }
            }
            resolveAllCriteria.call(whereData)
        }
        return criteria
    }

    public void validateWhereAllowedCondition(GsWhereData whereData, GsDataFilterOrganizer gsDataFilter){
        whereData.properties.each {
            println(it)
        }
    }



    public GsFilteredData resolve(GsApiActionDefinition definition, GrailsParameterMap params){
        GsParamsPairData paramsPairData = getParamsPair(params)
        GsFilteredData gsFilteredData = resolveFilterData(paramsPairData, definition)
        validateWhereAllowedCondition(gsFilteredData.where, definition)
        gsFilteredData.whereClosure = resolveWhereClosure(gsFilteredData.where, definition)
        return gsFilteredData
    }

}
