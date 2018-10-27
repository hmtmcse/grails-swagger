package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsParamsPairData
import grails.converters.JSON


class GsRestfulService {


    GsApiResponseData gsRead(GsApiActionDefinition definition){
        return GsApiResponseData.failed("Failed")
    }


    void readRequestProcessor(GsApiActionDefinition definition, Map params){

    }

    def readListProcessor(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try{

            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params)
            Map pagination = gsDataFilterHandler.readPaginationWithSortProcessor(gsParamsPairData)
            Map allowedFields = gsDataFilterHandler.filterAllowedField(definition.whereAllowedPropertyList, gsParamsPairData.params)

            Closure listCriteria = null
            responseData.isSuccess = true
            responseData.total = definition.domain.createCriteria().count() {
                if (listCriteria) {
                    criteria listCriteria
                }
            }

            def queryResult = definition.domain.createCriteria().list(pagination) {
                if (listCriteria) {
                    criteria listCriteria
                }
            }
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult)
            if (definition.successResponseFormat == null){
                definition.successResponseFormat = GsApiResponseData.successResponseWithTotal([], 0)
            }
        }catch(Exception e){
            println(e.getMessage())
            responseData.isSuccess = false
            responseData = GsApiResponseData.failed(GsConfigHolder.failedMessage())
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }

    void readDetailsProcessor(GsApiActionDefinition definition, Map params){

    }

    def gsReadList(GsApiActionDefinition definition, Map params){
        return readListProcessor(definition, params)
        GsApiResponseData responseData = GsApiResponseData.failed(GsConfigHolder.failedMessage())
        try{
            responseData.isSuccess = true
            registerJsonMarshaller(definition)
            params = processListParamsData(params)
            Map response = [:]
            response.total = definition.domain.createCriteria().count(){}
            response.data = definition.domain.createCriteria().list(params){}
            responseData = GsApiResponseData.successResponse(response)
        }catch(Exception e){
            println(e.getMessage())
            responseData = GsApiResponseData.failed(GsConfigHolder.failedMessage())
        }
        return responseData.toMap()
    }


    def responseMapGenerator(Map<String, GsApiResponseProperty> responseProperties, def queryResult) {
        List resultList = []
        Map resultMap = [:]
        if (queryResult instanceof List) {
            queryResult.each { data ->
                resultMap = [:]
                responseProperties.each { String fieldName, GsApiResponseProperty response ->
                    resultMap.put(response.getMapKey(), valueFromDomain(fieldName, data, response))
                }
                resultList.add(resultMap)
            }
            return resultList
        } else {
            responseProperties.each { String fieldName, GsApiResponseProperty response ->
                resultMap.put(response.getMapKey(), valueFromDomain(fieldName, queryResult, response))
            }
            return resultMap
        }
    }


    private GsInternalResponse saveUpdate(Object domain, Map params) {
        GsInternalResponse gsInternalResponse = GsInternalResponse.instance()
        domain.properties = params
        domain.validate()
        if (domain.hasErrors()) {
            return gsInternalResponse.processDomainError(domain.errors.allErrors)
        } else {
            gsInternalResponse.domain = domain.save(flush: true)
        }
        return gsInternalResponse.setIsSuccess(true)
    }


    def gsCreate(GsApiActionDefinition definition, Map params){
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        GsInternalResponse gsInternalResponse = gsDataFilterHandler.saveUpdateDataFilter(definition, params)
        if (gsInternalResponse.isSuccess){
            gsInternalResponse = saveUpdate(definition.domain.newInstance(), gsInternalResponse.filteredParams)
        }
        return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
    }

    def gsUpdate(GsApiActionDefinition definition, Map params){}

    def gsDelete(GsApiActionDefinition definition, Map params){}

    def gsDetails(GsApiActionDefinition definition, Map params){}

    def gsCustomQuery(GsApiActionDefinition definition, Map params){}

    def gsCustomQueryAndResponse(GsApiActionDefinition definition, Map params){}


    private void registerJsonMarshaller(GsApiActionDefinition definition){
        JSON.registerObjectMarshaller(definition.domain){
            return mapDomainDataToDefinition(it, definition.getResponseProperties())
        }
    }

    private Map processListParamsData(Map params){
        Map refineParams = [:]
        refineParams.max = params.max ?: GsConfigHolder.itemsPerPage()
        refineParams.offset = params.offset ?: 0
        if (!params.sort) {
            refineParams.sort = GsConfigHolder.sortColumn()
            refineParams.order = GsConfigHolder.sortOrder()
        }
        return refineParams
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
