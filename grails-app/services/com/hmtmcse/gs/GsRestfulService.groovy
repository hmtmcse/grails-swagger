package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import grails.converters.JSON
import grails.web.servlet.mvc.GrailsParameterMap


class GsRestfulService {

    GsConfigService gsConfigService

    GsApiResponseData gsRead(GsApiActionDefinition definition){
        return GsApiResponseData.failed("Failed")
    }


    def gsReadList(GsApiActionDefinition definition, GrailsParameterMap params){
        registerJsonMarshaller(definition)
        params = processParamsData(params)
        return definition.domain.list(params)
    }



    def gsCreate(GsApiActionDefinition definition){}

    def gsUpdate(GsApiActionDefinition definition){}

    def gsDelete(GsApiActionDefinition definition){}


    private void registerJsonMarshaller(GsApiActionDefinition definition){
        JSON.registerObjectMarshaller(definition.domain){
            return mapDomainDataToDefinition(it, definition.getResponseProperties())
        }
    }

    private GrailsParameterMap processParamsData(GrailsParameterMap params){
        params.max = params.max ?: gsConfigService.itemsPerPage()
        params.offset = params.offset ?: 0
        if (!params.sort) {
            params.sort = gsConfigService.sortColumn()
            params.order = gsConfigService.sortOrder()
        }
        return params
    }


    private Map mapDomainDataToDefinition(def domain, Map<String, GsApiResponseProperty> responseProperties){
        def map = [:]
        responseProperties.each {String key, GsApiResponseProperty property ->
            map.put(property.getMapKey(), valueFromDomain(key, domain, property))
        }
        return map
    }


    private def valueFromDomain(String key, def domain, GsApiResponseProperty gsApiResponseProperty){
        try{
          return domain[key]
        }catch(Exception e){
            return gsApiResponseProperty.getDefaultValue()
        }
    }


}
