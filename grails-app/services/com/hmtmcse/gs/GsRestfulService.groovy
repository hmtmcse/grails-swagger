package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import grails.converters.JSON
import grails.web.servlet.mvc.GrailsParameterMap


class GsRestfulService {

    GsApiResponseData gsRead(GsApiActionDefinition definition){
        return GsApiResponseData.failed("Failed")
    }


    def gsReadList(GsApiActionDefinition definition, GrailsParameterMap params){
        registerJsonMarshaller(definition)
        return definition.domain.list()
    }

    def gsCreate(GsApiActionDefinition definition){}

    def gsUpdate(GsApiActionDefinition definition){}

    def gsDelete(GsApiActionDefinition definition){}


    private void registerJsonMarshaller(GsApiActionDefinition definition){
        JSON.registerObjectMarshaller(definition.domain){
            return mapDomainDataToDefinition(it, definition.getResponseProperties())
        }
    }

    private Map mapDomainDataToDefinition(def domain, Map<String, GsApiResponseProperty> responseProperties){
        def map = [:]
        def value
        responseProperties.each {String key, GsApiResponseProperty property ->
            value = domain[key]
            map.put(property.getMapKey(), getValueFromDomain(key, domain, property))
        }
        return map
    }

    private def getValueFromDomain(String key, def domain, GsApiResponseProperty gsApiResponseProperty){
        try{
          return domain[key]
        }catch(Exception e){
            return gsApiResponseProperty.getDefaultValue()
        }
    }


}
