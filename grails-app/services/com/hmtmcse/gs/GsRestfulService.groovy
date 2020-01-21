package com.hmtmcse.gs

import com.hmtmcse.gs.data.ApiHelper
import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsFilteredData
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.model.CustomResponseParamProcessor
import com.hmtmcse.gs.model.CustomProcessor
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap

class GsRestfulService {


    private def valueFromDomain(String key, def domain, GsApiResponseProperty gsApiResponseProperty) {
        try {
            return domain[key]
        } catch (Exception e) {
            return gsApiResponseProperty.getDefaultValue()
        }
    }

    def readListProcessor(GsApiActionDefinition definition, GrailsParameterMap params) {
        Closure closure = { GsFilteredData filteredData ->
            return definition.domain.createCriteria().list(filteredData.offsetMaxSort, filteredData.whereClosure)
        }
        return readListProcessWithClosure(definition, params, closure)
    }


    def readListProcessWithClosure(GsApiActionDefinition definition, GrailsParameterMap params, Closure closure) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            GsFilterResolver gsFilterResolver = new GsFilterResolver()
            GsFilteredData filteredData = gsFilterResolver.resolve(definition, params)
            responseData.isSuccess = true
            def queryResult = closure(filteredData)
            responseData.queryResult = queryResult
            responseData.total = (queryResult ? queryResult.totalCount : 0)
            responseData.offset = filteredData.offset
            responseData.itemPerPage = filteredData.max
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult, [])
            if (definition.successResponseFormat == null) {
                definition.successResponseFormat = GsApiResponseData.successResponseWithTotal([], 0)
            }
        } catch (GsValidationException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        } catch (Exception e) {
            println(e.getMessage())
            responseData.isSuccess = false
            responseData.message = GsConfigHolder.failedMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def readDetailsProcessor(GsApiActionDefinition definition, GrailsParameterMap params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            responseData.queryResult = queryResult
            responseData.isSuccess = true
            responseData.response = responseMapGenerator(definition.getResponseProperties(), queryResult)
            if (definition.successResponseFormat == null) {
                definition.successResponseFormat = GsApiResponseData.successResponse([])
            }
        } catch (GsValidationException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }


    def gsReadList(GsApiActionDefinition definition, GrailsParameterMap params) {
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


    @Transactional
    private GsInternalResponse saveUpdate(Object domain, Map params, GsApiActionDefinition definition = null) {
        GsInternalResponse gsInternalResponse = GsInternalResponse.instance()
        domain.properties = params
        domain.validate()
        if (domain.hasErrors()) {
            LinkedHashMap<String, GsApiRequestProperty> requestMap = null
            if (definition){
                requestMap = definition.getRequestProperties()
            }
            return gsInternalResponse.processDomainError(domain.errors.allErrors, requestMap)
        } else {
            gsInternalResponse.domain = domain.save()
            gsInternalResponse.queryResult = gsInternalResponse.domain
        }
        return gsInternalResponse.setIsSuccess(true)
    }


    def gsCreate(GsApiActionDefinition definition, GrailsParameterMap params) {
        return saveUpdateProcessor(definition, params, definition.domain.newInstance())
    }

    GsInternalResponse filterAndValidateRequest(GsApiActionDefinition definition, GrailsParameterMap params) {
        GsFilteredData gsFilteredData = GsFilterResolver.instance().resolve(definition, params)
        GsRequestValidator gsRequestValidator = GsRequestValidator.instance()
        GsInternalResponse gsInternalResponse = gsRequestValidator.validate(gsFilteredData.gsParamsPairData, definition).setGsFilteredData(gsFilteredData)
        if (gsInternalResponse.isSuccess) {
            gsFilteredData.gsParamsPairData = gsInternalResponse.gsParamsPairData
            gsFilteredData = GsFilterResolver.resolveRequestPreProcessor(definition, gsFilteredData)
            gsInternalResponse.gsParamsPairData = gsFilteredData.gsParamsPairData
        }
        return gsInternalResponse
    }


    def saveUpdateProcessor(GsApiActionDefinition definition, GrailsParameterMap params, def domain) {
        GsInternalResponse gsInternalResponse = filterAndValidateRequest(definition, params)
        if (gsInternalResponse.isSuccess) {
            gsInternalResponse = saveUpdate(domain, gsInternalResponse.gsParamsPairData.params, definition)
        }

        if (definition.successResponseFormat == null) {
            definition.successResponseFormat = GsConfigHolder.defaultSuccessResponse
        } else if (gsInternalResponse.isSuccess && gsInternalResponse.domain) {
            gsInternalResponse.response = makeApiResponse(definition, gsInternalResponse.domain, definition.successResponseFormat.response)
        }
        return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
    }


    def gsDetails(GsApiActionDefinition definition, GrailsParameterMap params) {
        return readDetailsProcessor(definition, params)
    }


    def gsCount(GsApiActionDefinition definition, GrailsParameterMap params) {
        return readCountProcessor(definition, params)
    }


    def readCountProcessor(GsApiActionDefinition definition, GrailsParameterMap params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = countByCondition(definition, params)
            responseData.queryResult = queryResult
            responseData.isSuccess = true
            responseData.response = [count: queryResult]
            definition.successResponseFormat = GsApiResponseData.successResponse([])
        } catch (GrailsSwaggerException e) {
            responseData.isSuccess = false
            responseData.message = e.getMessage()
        }
        return GsApiResponseData.processAPIResponse(definition, responseData)
    }

    def countByCondition(GsApiActionDefinition definition, GrailsParameterMap params) throws GrailsSwaggerException {
        def queryResult = null
        try {
            GsParamsPairData gsParamsPairData = GsFilterResolver.instance().getParamsPair(params, definition)
            Map where = [:]
            if (definition.enableWhere && gsParamsPairData.params && gsParamsPairData.params.where && gsParamsPairData.params.where instanceof Map) {
                gsParamsPairData.params.where[GsConstant.COUNT] = true
            } else {
                where.put(GsConstant.COUNT, true)
            }
            queryResult = definition.domain.count()
        } catch (GsValidationException e) {
            throw new GrailsSwaggerException(e.getMessage())
        } catch (Exception e) {
            String message = GsExceptionParser.exceptionMessage(e)
            throw new GrailsSwaggerException(message)
        }
        return queryResult
    }


    def readGetByCondition(GsApiActionDefinition definition, GrailsParameterMap params) throws GrailsSwaggerException {
        def queryResult = null
        try {
            GsFilterResolver gsFilterResolver = new GsFilterResolver()
            GsFilteredData filteredData = gsFilterResolver.resolve(definition, params)
            queryResult = definition.domain.createCriteria().get(filteredData.whereClosure)
        } catch (GsValidationException e) {
            throw new GrailsSwaggerException(e.getMessage())
        } catch (Exception e) {
            String message = GsExceptionParser.exceptionMessage(e)
            throw new GrailsSwaggerException(message)
        }
        return queryResult
    }


    def gsUpdate(GsApiActionDefinition definition, GrailsParameterMap params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            responseData.queryResult = queryResult
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


    def gsDelete(GsApiActionDefinition definition, GrailsParameterMap params) {
        GsInternalResponse responseData = GsInternalResponse.instance()
        try {
            def queryResult = readGetByCondition(definition, params)
            responseData.queryResult = queryResult
            if (queryResult == null) {
                responseData.message = GsConfigHolder.requestedConditionEmpty()
            } else {
                queryResult.delete()
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
        if (definition.failedResponseFormat == null) {
            definition.failedResponseFormat = GsConfigHolder.defaultFailedResponse
        }
        if (queryResult != null){
            return GsApiResponseData.successResponse(responseMapGenerator(definition.getResponseProperties(), queryResult, defaultResponse))
        }
        return definition.failedResponseFormat
    }

    GsApiResponseData responseMessageToApi(GsApiActionDefinition definition, Boolean isSuccess) {
        if (definition.failedResponseFormat == null) {
            definition.failedResponseFormat = GsConfigHolder.defaultFailedResponse
        }
        if (definition.successResponseFormat == null) {
            definition.successResponseFormat = GsConfigHolder.defaultSuccessResponse
        }
        return isSuccess ? definition.successResponseFormat : definition.failedResponseFormat
    }


    def gsCustomProcessor(GsApiActionDefinition definition, GrailsParameterMap params) {

        GsInternalResponse gsInternalResponse = filterAndValidateRequest(definition, params)
        if (!gsInternalResponse.isSuccess) {
            return GsApiResponseData.processAPIResponse(definition, gsInternalResponse)
        }

        GsApiResponseData gsApiResponseData = null
        ApiHelper apiHelper = new ApiHelper()
        apiHelper.help = this
        apiHelper.gsFilteredData = gsInternalResponse.gsFilteredData
        gsInternalResponse.copyFilterAndPagination()
        if (definition.customProcessor != null && definition.customProcessor instanceof CustomProcessor) {
            gsApiResponseData = definition.customProcessor.process(definition, gsInternalResponse.gsParamsPairData, apiHelper)
        }

        if (gsApiResponseData) {
            return gsApiResponseData.toMap()
        }

        return GsApiResponseData.failed(GsConfigHolder.failedMessage()).toMap()
    }


}
