package com.hmtmcse.gs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.hmtmcse.gs.data.GsDomainProperty
import com.hmtmcse.gs.data.GsFilteredData
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.data.GsWhereData
import com.hmtmcse.gs.data.GsWhereFilterProperty
import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerProperty
import grails.web.servlet.mvc.GrailsParameterMap

class GsFilterResolver {



    public static GsFilterResolver instance(){
        return new GsFilterResolver()
    }


    public GsParamsPairData getParamsPair(GrailsParameterMap params, GsApiActionDefinition definition) {
        return getParamsPair(params, definition.gsDomain.domainProperties)
    }

    public GsParamsPairData getParamsPair(GrailsParameterMap params, LinkedHashMap<String, GsDomainProperty> domainProperties = null) {
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
                    gsParamsPairData.params = domainProperties == null ? params : castFromDomainSwaggerMap(params, domainProperties)
                    return gsParamsPairData.initFilteredGrailsParams()
            }
        }
        return gsParamsPairData
    }

    static def castFromDomainSwaggerMap(Map params, LinkedHashMap<String, GsDomainProperty> domainProperties) {
        GsDomainProperty gsDomainProperty
        params.each { String key, def value ->
            if (domainProperties.containsKey(key)){
                gsDomainProperty =  domainProperties.get(key)
                params[key] = GsReflectionUtil.castToGSObject(gsDomainProperty.swaggerDataType, value)
            }
        }
        if (params.containsKey(GsConstant.PROPERTY_NAME) && params.containsKey(GsConstant.PROPERTY_VALUE)) {
            String fieldName = params[GsConstant.PROPERTY_NAME]
            if (domainProperties.containsKey(fieldName)){
                gsDomainProperty =  domainProperties.get(fieldName)
                params[GsConstant.PROPERTY_VALUE] = GsReflectionUtil.castToGSObject(gsDomainProperty.swaggerDataType, params[GsConstant.PROPERTY_VALUE])
            }
        }
        return params
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
            if (paramsPairData.httpMethod.equals(GsConstant.POST) && paramsPairData.params && paramsPairData.params.where) {
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
        if (whereData == null) {
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
        whereData.class.declaredFields.each {
            if (!it.synthetic){
                if (whereData[it.name] && !gsDataFilter.allowedCondition.get(it.name)){
                    throw new GsValidationException(GsConfigHolder.unauthorizedFieldOrCondition(it.name, "Criteria"))
                }
            }
        }
    }

    public GsFilteredData resolve(GsApiActionDefinition definition, GrailsParameterMap params){
        GsParamsPairData paramsPairData = getParamsPair(params, definition.gsDomain.domainProperties)
        GsFilteredData gsFilteredData = resolveFilterData(paramsPairData, definition)
        gsFilteredData.gsParamsPairData = paramsPairData
        validateWhereAllowedCondition(gsFilteredData.where, definition)
        gsFilteredData.whereClosure = resolveWhereClosure(gsFilteredData.where, definition)
        return gsFilteredData
    }











    public SwaggerProperty resolveSwaggerDefinition(GsApiActionDefinition gsApiActionDefinition, String inType, SwaggerProperty swaggerProperty = null) {
        swaggerProperty = (swaggerProperty ?: new SwaggerProperty())

        if (gsApiActionDefinition.enablePaginationAndSorting) {
            swaggerProperty = swaggerPagination(swaggerProperty, inType)
            swaggerProperty = swaggerSorting(swaggerProperty, inType)

        }

        if (gsApiActionDefinition.enableQueryFilter) {
            swaggerProperty = swaggerQueryFilter(swaggerProperty, inType)
        }

        if (gsApiActionDefinition.whereAllowedPropertyList.size()) {
            swaggerProperty = swaggerAllowedFields(swaggerProperty, gsApiActionDefinition.whereAllowedPropertyList, inType)
        }

        return swaggerProperty
    }

    private SwaggerProperty swaggerAllowedFields(SwaggerProperty swaggerProperty, List<GsWhereFilterProperty> whereAllowedPropertyList, String inType) {
//        swaggerProperty.otherProperty(GsConstant.ALLOWED_PROPERTY, whereAllowedPropertyList?.name?.join(", ")).addToListWithType(inType)
        return swaggerProperty
    }

    private SwaggerProperty swaggerPagination(SwaggerProperty swaggerProperty, String inType = null) {
        swaggerProperty.property(GsConstant.OFFSET, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
        swaggerProperty.property(GsConstant.MAX, SwaggerConstant.SWAGGER_DT_INTEGER).addToListWithType(inType)
        return swaggerProperty
    }


    private SwaggerProperty swaggerSorting(SwaggerProperty swaggerProperty, String inType = null) {
        if (inType && inType.equals(SwaggerConstant.IN_QUERY)) {
            swaggerProperty.property(GsConstant.ORDER_PROPERTY, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            swaggerProperty.property(GsConstant.ORDER, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
        }
        return swaggerProperty
    }


    private SwaggerProperty swaggerQueryFilter(SwaggerProperty swaggerProperty, String inType = null) {
        if (inType && inType.equals(SwaggerConstant.IN_QUERY)) {
            swaggerProperty.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
            swaggerProperty.property(GsConstant.PROPERTY_VALUE, SwaggerConstant.SWAGGER_DT_STRING).addToListWithType(inType)
        }
        return swaggerProperty
    }

    public SwaggerProperty swaggerWhere(GsApiActionDefinition gsApiActionDefinition, SwaggerProperty swaggerProperty, String inType = null) {
        if (!gsApiActionDefinition.allowedCondition) {
            return swaggerProperty
        }

        SwaggerProperty operators = new SwaggerProperty()
        gsApiActionDefinition.allowedCondition.each { String key, Boolean value ->
            if (key.equals(GsConstant.COUNT)) {
                operators.property(GsConstant.COUNT, SwaggerConstant.SWAGGER_DT_BOOLEAN).example(true)
            } else if (key.equals(GsConstant.AND)) {
                operators.objectProperty(key, new SwaggerProperty().objectProperty(GsConstant.EQUAL, whereCondition(GsConstant.EQUAL))).description("You may use all of allowed Operator here. Like as notEqual, lessThan etc.")
            } else if (key.equals(GsConstant.OR)) {
                operators.objectProperty(key, new SwaggerProperty().objectProperty(GsConstant.NOT_EQUAL, whereCondition(GsConstant.NOT_EQUAL))).description("You may use all of allowed Operator here. Like as equal, lessThan etc.")
            } else {
                operators.objectProperty(key, whereCondition(key))
            }
        }

        swaggerProperty.objectProperty(GsConstant.WHERE, operators).addToListWithType(inType)
        if (gsApiActionDefinition.whereAllowedPropertyList.size()) {
            operators = swaggerAllowedFields(operators, gsApiActionDefinition.whereAllowedPropertyList, inType)
        }
        return swaggerProperty
    }


    private SwaggerProperty whereCondition(String condition) {
        SwaggerProperty property = new SwaggerProperty()
        SwaggerProperty nestedCondition
        switch (condition){
            case GsConstant.EQUAL:
            case GsConstant.NOT_EQUAL:
            case GsConstant.LESS_THAN:
            case GsConstant.LESS_THAN_EQUAL:
            case GsConstant.GETTER_THAN:
            case GsConstant.GETTER_THAN_EQUAL:
                property.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).example(GsConstant.PROPERTY_VALUE)
                property.property(GsConstant.PROPERTY_NAME_X, SwaggerConstant.SWAGGER_DT_INTEGER).example(10)
                break
            case GsConstant.ORDER:
                property.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).example(GsConstant.ASC + "|" + GsConstant.DESC)
                break
            case GsConstant.LIKE:
                property.property(GsConstant.PROPERTY_NAME, SwaggerConstant.SWAGGER_DT_STRING).example( "%" + GsConstant.PROPERTY_VALUE + "%")
                break
            case GsConstant.IN_LIST:
                property.arrayPropertyList(GsConstant.PROPERTY_NAME,SwaggerConstant.SWAGGER_DT_STRING, GsConstant.PROPERTY_VALUE_X)
                break
            case GsConstant.BETWEEN:
                nestedCondition = new SwaggerProperty()
                nestedCondition.property(GsConstant.PROPERTY_VALUE, SwaggerConstant.SWAGGER_DT_STRING).example(GsConstant.PROPERTY_VALUE)
                property.objectProperty(GsConstant.PROPERTY_NAME, nestedCondition)
                break
        }
        return property
    }


}
