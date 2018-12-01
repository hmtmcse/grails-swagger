package com.hmtmcse.gs

import com.hmtmcse.gs.data.ApiHelper
import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.model.CustomResponseParamProcessor
import com.hmtmcse.gs.model.CustomProcessor
import grails.web.servlet.mvc.GrailsParameterMap

class GsRestfulService {


    private def valueFromDomain(String key, def domain, GsApiResponseProperty gsApiResponseProperty) {
        try {
            return domain[key]
        } catch (Exception e) {
            return gsApiResponseProperty.getDefaultValue()
        }
    }


    def readListProcessor(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try {
            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params, definition.domainFields())
            Map pagination = gsDataFilterHandler.readPaginationWithSortProcessor(gsParamsPairData)
            Closure listCriteria = gsDataFilterHandler.readCriteriaProcessor(gsParamsPairData)
            responseData.isSuccess = true
            def queryResult = definition.domain.createCriteria().list(pagination, listCriteria)
            responseData.total = (queryResult ? queryResult.totalCount : 0)
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult, [])
            if (definition.successResponseFormat == null) {
                definition.successResponseFormat = GsApiResponseData.successResponseWithTotal([], 0)
            }
        } catch (Exception e) {
            println(e.getMessage())
            responseData.isSuccess = false
            responseData.message = GsConfigHolder.failedMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def readDetailsProcessor(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            responseData.isSuccess = true
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult)
            if (definition.successResponseFormat == null) {
                definition.successResponseFormat = GsApiResponseData.successResponse([])
            }
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def gsReadList(GsApiActionDefinition definition, Map params) {
        return readListProcessor(definition, params)
    }


    private Map responseMap (Map<String, GsApiResponseProperty> responseProperties, def domainData, def defaultResponse = [:]){
        Map resultMap = [:]
        responseProperties.each { String fieldName, GsApiResponseProperty response ->
            if (response.customResponseParamProcessor != null && response.customResponseParamProcessor instanceof  CustomResponseParamProcessor){
                resultMap.put(response.getMapKey(), response.customResponseParamProcessor.process(fieldName, domainData, response))
            }else if (response.relationalEntity == null){
                resultMap.put(response.getMapKey(), valueFromDomain(fieldName, domainData, response))
            }else{
                resultMap.put(response.getMapKey(), responseMapGenerator(response.relationalEntity.responseProperties, valueFromDomain(fieldName, domainData, response), defaultResponse))
            }
        }
        return resultMap
    }


    def responseMapGenerator(Map<String, GsApiResponseProperty> responseProperties, def queryResult, def defaultResponse = [:]) {
        List resultList = []
        if (queryResult) {
            if (queryResult instanceof List || queryResult instanceof Set) {
                queryResult.each { data ->
                    resultList.add(responseMap(responseProperties, data, defaultResponse))
                }
                return resultList
            } else {
                return responseMap(responseProperties, queryResult, defaultResponse)
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


    def gsCreate(GsApiActionDefinition definition, Map params) {
        return saveUpdateProcessor(definition, params, definition.domain.newInstance())
    }

    GsInternalResponse filterAndValidateRequest(GsApiActionDefinition definition, Map params){
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params, definition.domainFields())
        GsRequestValidator gsRequestValidator = GsRequestValidator.instance()
        return gsRequestValidator.validate(gsParamsPairData, definition)
    }


    def saveUpdateProcessor(GsApiActionDefinition definition, Map params, def domain) {
        GsInternalResponse gsInternalResponse = filterAndValidateRequest(definition, params)
        if (gsInternalResponse.isSuccess) {
            gsInternalResponse = saveUpdate(domain, gsInternalResponse.filteredParams)
        }

        if (definition.successResponseFormat == null) {
            definition.successResponseFormat = GsConfigHolder.defaultSuccessResponse
        } else if (gsInternalResponse.isSuccess && gsInternalResponse.domain) {
            gsInternalResponse.response = makeApiResponse(definition, gsInternalResponse.domain, definition.successResponseFormat.response)
        }
        return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
    }


    def gsDetails(GsApiActionDefinition definition, Map params) {
        return readDetailsProcessor(definition, params)
    }


    def gsCount(GsApiActionDefinition definition, Map params) {
        return readCountProcessor(definition, params)
    }


    def readCountProcessor(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = countByCondition(definition, params)
            responseData.isSuccess = true
            responseData.response = [count: queryResult]
            definition.successResponseFormat = GsApiResponseData.successResponse([])
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }

    def countByCondition(GsApiActionDefinition definition, Map params) throws GrailsSwaggerException {
        def queryResult = null
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try {
            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params, definition.domainFields())
            Map where = [:]
            if (definition.enableWhere && gsParamsPairData.params && gsParamsPairData.params.where && gsParamsPairData.params.where instanceof Map) {
                gsParamsPairData.params.where[GsConstant.COUNT] = true
            } else {
                where.put(GsConstant.COUNT, true)
            }
            queryResult = definition.domain.count()
        } catch (Exception e) {
            String message = GsExceptionParser.exceptionMessage(e)
            throw new GrailsSwaggerException(message)
        }
        return queryResult
    }


    def readGetByCondition(GsApiActionDefinition definition, Map params) throws GrailsSwaggerException {
        def queryResult = null
        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        try {
            GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params, definition.domainFields())
            Closure listCriteria = gsDataFilterHandler.readCriteriaProcessor(gsParamsPairData, false, "details")
            queryResult = definition.domain.createCriteria().get(listCriteria)
        } catch (Exception e) {
            String message = GsExceptionParser.exceptionMessage(e)
            throw new GrailsSwaggerException(message)
        }
        return queryResult
    }


    def gsUpdate(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            if (queryResult == null) {
                responseData.message = GsConfigHolder.requestedConditionEmpty()
            } else {
                return saveUpdateProcessor(definition, params, queryResult)
            }
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def gsDelete(GsApiActionDefinition definition, Map params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            if (queryResult == null) {
                responseData.message = GsConfigHolder.requestedConditionEmpty()
            } else {
                queryResult.delete(flush: true)
                responseData.isSuccess = true
            }
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        } catch (Exception e) {
            responseData.isSuccess = false
            responseData.message = GsConfigHolder.failedMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def gsBulkUpdate(GsApiActionDefinition definition, Map params) {}


    def gsBulkDelete(GsApiActionDefinition definition, Map params) {}


    def resolveConditions(GsApiActionDefinition definition, Map params) {
        return true
    }


    private def makeApiResponse(GsApiActionDefinition definition, def queryResult, def defaultResponse = [:]) {
        return responseMapGenerator(definition.getResponseProperties(), queryResult, defaultResponse)
    }


    GsApiResponseData responseToApi(GsApiActionDefinition definition, def queryResult, def defaultResponse = [:]) {
        return GsApiResponseData.successResponse(responseMapGenerator(definition.getResponseProperties(), queryResult, defaultResponse))
    }


    def gsCustomProcessor(GsApiActionDefinition definition, GrailsParameterMap params) {

        GsInternalResponse gsInternalResponse = filterAndValidateRequest(definition, params)
        if (!gsInternalResponse.isSuccess) {
            return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
        }

        resolveConditions(definition, params)

        GsApiResponseData gsApiResponseData = null
        ApiHelper apiHelper = new ApiHelper()
        apiHelper.help = this


        GsDataFilterHandler gsDataFilterHandler = GsDataFilterHandler.instance()
        GsParamsPairData gsParamsPairData = gsDataFilterHandler.getParamsPair(params, null)
        if (definition.customProcessor != null && definition.customProcessor instanceof CustomProcessor) {
            gsApiResponseData = definition.customProcessor.process(definition, gsParamsPairData, apiHelper)
        }

        if (gsApiResponseData) {
            return gsApiResponseData.toMap()
        }

        return GsApiResponseData.failed(GsConfigHolder.failedMessage()).toMap()
    }


}
