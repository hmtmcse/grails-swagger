package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsParamsPairData
import grails.converters.JSON


class GsRestfulService {


    GsApiResponseData gsRead(GsApiActionDefinition definition){
        return GsApiResponseData.failed("Failed")
    }


    def readRequestProcessor(GsApiActionDefinition definition, Map params){
        switch (definition.responseType){
            case GsConstant.LIST_RESPONSE:
                return readListProcessor(definition, params)
            case GsConstant.DETAILS_RESPONSE:
                return readDetailsProcessor(definition, params)
            default:
                return GsApiResponseData.failed(GsConfigHolder.failedMessage()).toMap()
        }
    }

    def readListProcessor(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try{
            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params)
            Map pagination = gsDataFilterHandler.readPaginationWithSortProcessor(gsParamsPairData)
            Closure listCriteria = gsDataFilterHandler.readCriteriaProcessor(gsParamsPairData)
            responseData.isSuccess = true
            responseData.total = definition.domain.createCriteria().count(listCriteria)
            def queryResult = definition.domain.createCriteria().list(pagination, listCriteria)

            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult, [])
            if (definition.successResponseFormat == null){
                definition.successResponseFormat = GsApiResponseData.successResponseWithTotal([], 0)
            }
        }catch(Exception e){
            println(e.getMessage())
            responseData.isSuccess = false
            responseData.message = GsConfigHolder.failedMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }

    def readDetailsProcessor(GsApiActionDefinition definition, Map params){
        GsInternalResponse responseData = GsInternalResponse.instance()
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try{
            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params)
            Closure listCriteria = gsDataFilterHandler.readCriteriaProcessor(gsParamsPairData, false, "details")
            responseData.isSuccess = true
            def queryResult = definition.domain.createCriteria().get(listCriteria)
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult)
            if (definition.successResponseFormat == null){
                definition.successResponseFormat = GsApiResponseData.successResponse([])
            }
        }catch(Exception e){
            println(e.getMessage())
            responseData.isSuccess = false
            responseData.message = responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }

    def gsReadList(GsApiActionDefinition definition, Map params){
        return readRequestProcessor(definition, params)
    }


    def responseMapGenerator(Map<String, GsApiResponseProperty> responseProperties, def queryResult, def defaultResponse = [:]) {
        List resultList = []
        Map resultMap = [:]
        if (queryResult) {
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
        return defaultResponse
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
        if (definition.successResponseFormat == null){
            definition.successResponseFormat = GsConfigHolder.defaultSuccessResponse
        }
        return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
    }

    def gsDetails(GsApiActionDefinition definition, Map params){
        return readRequestProcessor(definition, params)
    }

    def gsUpdate(GsApiActionDefinition definition, Map params){}

    def gsDelete(GsApiActionDefinition definition, Map params){}


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
