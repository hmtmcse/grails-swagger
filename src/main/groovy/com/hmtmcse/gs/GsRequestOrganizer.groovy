package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsDomain
import com.hmtmcse.gs.data.GsDomainProperty

trait GsRequestOrganizer<T>  {


    abstract LinkedHashMap<String, GsApiRequestProperty> getRequestProperties()
    abstract LinkedHashMap<String, GsApiRequestProperty> setRequestProperties(LinkedHashMap<String, GsApiRequestProperty> requestPropertyLinkedHashMap)
    abstract GsDomain getGsDomain()


    public T includeInRequest(List<String> fields) {
        fields?.each { String field ->
            requestProperties.put(field, new GsApiRequestProperty(field).setDataType(gsDomain.domainProperties.get(field)?.swaggerDataType))
        }
        return this as T
    }

    public T includeInRequest(String fieldName, GsApiRequestProperty gsApiRequestProperty) {
        requestProperties.put(fieldName, gsApiRequestProperty)
        return this as T
    }

    public GsApiRequestProperty addRequestProperty(String name, String dataType = null, String defaultValue = "") {
        dataType = (dataType != null ? dataType : gsDomain.domainProperties.get(name)?.swaggerDataType)
        requestProperties.put(name, new GsApiRequestProperty(name).setDataType(dataType).setDefaultValue(defaultValue))
        return requestProperties.get(name)
    }


    public T excludeFromRequest(List<String> fields) {
        fields?.each { String field ->
            requestProperties.remove(field)
        }
        return this as T
    }


    public T includeAllThenExcludeFromRequest(List<String> fields) {
        includeAllPropertyToRequest()
        excludeFromRequest(fields)
        return this as T
    }


    public T includeAllPropertyToRequest() {
        requestProperties = domainPropertyToRequestProperty(gsDomain.domainProperties)
        return this as T
    }


    LinkedHashMap<String, GsApiRequestProperty> domainPropertyToRequestProperty(LinkedHashMap<String, GsDomainProperty> domainProperties) {
        GsApiRequestProperty gsApiRequestProperty
        LinkedHashMap<String, GsApiRequestProperty> requestPropertyLinkedHashMap = new LinkedHashMap<>()
        if (domainProperties == null) {
            return requestPropertyLinkedHashMap
        }

        domainProperties.each { String name, GsDomainProperty gsDomainProperty ->
            gsApiRequestProperty = new GsApiRequestProperty(gsDomainProperty.name).setDataType(gsDomainProperty.swaggerDataType)
            if (gsDomainProperty.isRelationalEntity) {
                gsApiRequestProperty.relationalEntity = new GsRelationalEntityRequest()
                gsApiRequestProperty.relationalEntity.requestProperties = domainPropertyToRequestProperty(gsDomainProperty.relationalProperties)
            }
            requestPropertyLinkedHashMap.put(gsDomainProperty.name, gsApiRequestProperty)
        }
        return requestPropertyLinkedHashMap
    }


}