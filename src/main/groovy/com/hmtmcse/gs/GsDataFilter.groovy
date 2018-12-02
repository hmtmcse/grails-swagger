package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsDomain
import com.hmtmcse.gs.data.GsDomainProperty
import com.hmtmcse.gs.data.GsWhereFilterProperty

trait GsDataFilter<T> {

    abstract List<GsWhereFilterProperty> getWhereAllowedPropertyList()
    abstract List<GsWhereFilterProperty> setWhereAllowedPropertyList(List<GsWhereFilterProperty> list)
    abstract LinkedHashMap<String, GsWhereFilterProperty> getWhereAllowedPropertyMap()
    abstract LinkedHashMap<String, GsWhereFilterProperty> setWhereAllowedPropertyMap(LinkedHashMap<String, GsWhereFilterProperty> map)
    abstract GsDomain getGsDomain()
    public LinkedHashMap<String, Boolean> allowedCondition = new LinkedHashMap<>()
    public Boolean enableQueryFilter = false
    public Boolean enablePaginationAndSorting = false

    public T allowedConditions(List<String> fields) {
        fields?.each { String field ->
            allowedCondition.put(field, true)
        }
        return this as T
    }

    public T allowedConditionEqualAndOr() {
        return allowedConditions([
                GsConstant.EQUAL,
                GsConstant.AND,
                GsConstant.OR
        ])
    }

    public T allowedConditionOnlyEqual() {
        return allowedConditions([
                GsConstant.EQUAL
        ])
    }

    public T allowedAllCondition() {
        return allowedConditions([
                GsConstant.EQUAL,
                GsConstant.AND,
                GsConstant.ORDER,
                GsConstant.NOT_EQUAL,
                GsConstant.LESS_THAN,
                GsConstant.LESS_THAN_EQUAL,
                GsConstant.GETTER_THAN,
                GsConstant.GETTER_THAN_EQUAL,
                GsConstant.IN_LIST,
                GsConstant.BETWEEN,
                GsConstant.LIKE,
                GsConstant.SELECT,
                GsConstant.COUNT
        ])
    }

    public T includeInWhereFilter(List<String> fields) {
        GsWhereFilterProperty gsWhereFilterProperty
        fields?.each { String field ->
            gsWhereFilterProperty = new GsWhereFilterProperty(field)
            gsWhereFilterProperty.dataType = gsDomain.domainProperties.get(field).swaggerDataType
            whereAllowedPropertyMap.put(field, gsWhereFilterProperty)
            whereAllowedPropertyList.add(gsWhereFilterProperty)
        }
        return this as T
    }


    public GsWhereFilterProperty addToWhereFilterProperty(String name, String dataType = null, Boolean isTypeCast = false) {
        GsWhereFilterProperty gsWhereFilterProperty = new GsWhereFilterProperty(name)
        gsWhereFilterProperty.dataType = (dataType ? dataType : gsDomain.domainProperties.get(name).swaggerDataType)
        whereAllowedPropertyMap.put(name, gsWhereFilterProperty)
        whereAllowedPropertyList.add(gsWhereFilterProperty)
        return whereAllowedPropertyMap.get(name)
    }


    public T excludeFromWhereFilter(List<String> fields) {
        fields?.each { String field ->
            whereAllowedPropertyMap.remove(field)
        }
        includeAllToListFromMap()
        return this as T
    }


    public T includeAllThenExcludeFromWhereFilter(List<String> fields) {
        includeAllPropertyToWhereFilter()
        excludeFromWhereFilter(fields)
        return this as T
    }


    void includeAllToListFromMap() {
        whereAllowedPropertyList = []
        whereAllowedPropertyMap.each { String name, GsWhereFilterProperty filterProperty ->
            whereAllowedPropertyList.add(filterProperty)
        }
    }

    public T includeAllPropertyToWhereFilter() {
        whereAllowedPropertyMap = domainPropertyToWhereFilterProperty(gsDomain.domainProperties)
        includeAllToListFromMap()
        return this as T
    }


    LinkedHashMap<String, GsWhereFilterProperty> domainPropertyToWhereFilterProperty(LinkedHashMap<String, GsDomainProperty> domainProperties) {
        GsWhereFilterProperty gsWhereFilterProperty
        LinkedHashMap<String, GsWhereFilterProperty> whereFilterPropertyLinkedHashMap = new LinkedHashMap<>()
        if (domainProperties == null) {
            return whereFilterPropertyLinkedHashMap
        }
        domainProperties.each { String name, GsDomainProperty gsDomainProperty ->
            gsWhereFilterProperty = new GsWhereFilterProperty(name).setDataType(gsDomainProperty.swaggerDataType)
            whereFilterPropertyLinkedHashMap.put(name, gsWhereFilterProperty)
        }
        return whereFilterPropertyLinkedHashMap
    }

}