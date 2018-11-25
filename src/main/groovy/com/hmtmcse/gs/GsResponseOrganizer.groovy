package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsDomain
import com.hmtmcse.gs.data.GsDomainProperty

trait GsResponseOrganizer<T> {

    public String relationalEntityName = null

    abstract LinkedHashMap<String, GsApiResponseProperty> getResponseProperties()
    abstract LinkedHashMap<String, GsApiResponseProperty> setResponseProperties(LinkedHashMap<String, GsApiResponseProperty> responsePropertyLinkedHashMap)
    abstract GsDomain getGsDomain()


    public void setRelationalEntityName(String relationalEntityName){
        this.relationalEntityName = relationalEntityName
    }

    public T includeInResponse(List<String> fields) {
        fields?.each { String field ->
            responseProperties.put(field, new GsApiResponseProperty(field))
        }
        return this as T
    }


    public T excludeFromResponse(List<String> fields) {
        fields?.each { String field ->
            responseProperties.remove(field)
        }
        return this as T

    }


    public T includeAllThenExcludeFromResponse(List<String> fields) {
        includeAllPropertyToResponse()
        excludeFromResponse(fields)
        return this as T
    }


    public GsApiResponseProperty addResponseProperty(String name, String alias = null, String defaultValue = "") {
        this.responseProperties.put(name, new GsApiResponseProperty(name).setAlias(alias).setDefaultValue(defaultValue))
        return this.responseProperties.get(name)
    }


    public T includeAllPropertyToResponse() {
        if (relationalEntityName != null) {
            GsDomainProperty gsDomainProperty = gsDomain.domainProperties.get(relationalEntityName)
            if (gsDomainProperty != null) {
                GsApiResponseProperty gsApiResponseProperty = new GsApiResponseProperty(gsDomainProperty.name).setDataType(gsDomainProperty.swaggerDataType)
                gsApiResponseProperty.relationalEntity = new GsRelationalEntityResponse()
                gsApiResponseProperty.relationalEntity.responseProperties = domainPropertyToResponseProperty(gsDomainProperty.relationalProperties)
                responseProperties.put(gsDomainProperty.name, gsApiResponseProperty)
            }
        } else {
            responseProperties = domainPropertyToResponseProperty(gsDomain.domainProperties)
        }
        return this as T
    }


    LinkedHashMap<String, GsApiResponseProperty> domainPropertyToResponseProperty(LinkedHashMap<String, GsDomainProperty> domainProperties) {
        GsApiResponseProperty gsApiResponseProperty
        LinkedHashMap<String, GsApiResponseProperty> responsePropertyLinkedHashMap = new LinkedHashMap<>()
        if (domainProperties == null) {
            return responsePropertyLinkedHashMap
        }
        domainProperties.each { String name, GsDomainProperty gsDomainProperty ->
            gsApiResponseProperty = new GsApiResponseProperty(gsDomainProperty.name).setDataType(gsDomainProperty.swaggerDataType)
            if (gsDomainProperty.isRelationalEntity) {
                gsApiResponseProperty.relationalEntity = new GsRelationalEntityResponse()
                gsApiResponseProperty.relationalEntity.responseProperties = domainPropertyToResponseProperty(gsDomainProperty.relationalProperties)
            }
            responsePropertyLinkedHashMap.put(gsDomainProperty.name, gsApiResponseProperty)
        }
        return responsePropertyLinkedHashMap
    }

}