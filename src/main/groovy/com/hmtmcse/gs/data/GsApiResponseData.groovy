package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsApiActionDefinition
import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.GsInternalResponse
import com.hmtmcse.gs.GsReflectionUtil
import com.hmtmcse.swagger.definition.SwaggerProperty

class GsApiResponseData {


    Object response = null
    String message = null
    List errorDetails = null
    Integer code = null
    Integer total = null
    Integer count = null
    Boolean isExist = null
    Boolean isSuccess = false

    GsApiResponseData(){}

    GsApiResponseData(Boolean isSuccess, String message, Integer code = null) {
        this.isSuccess = isSuccess
        this.message = message
        this.code = code
    }

    GsApiResponseData(Boolean isSuccess, Integer code = null) {
        this.isSuccess = isSuccess
        this.code = code
    }

    String getMessage() {
        return message
    }

    void setMessage(String message) {
        this.message = message
    }

    def getResponse() {
        return response
    }

    GsApiResponseData setResponse(response) {
        this.response = response
        return this
    }

    Integer getCode() {
        return code
    }

    GsApiResponseData setCode(Integer code) {
        this.code = code
        return this
    }

    static GsApiResponseData failed(String message, Integer code = null){
        return new GsApiResponseData(false, message).setCode(code)
    }

    static GsApiResponseData failedWithDetails(String message, List errorDetails, Integer code = null){
        return new GsApiResponseData(false, message).setCode(code).setErrorDetails(errorDetails)
    }

    static GsApiResponseData successMessage(String message, Integer code = null){
        return new GsApiResponseData(true, message).setCode(code)
    }

    static GsApiResponseData successResponse(Object response, Integer code = null){
        return instance(true).setCode(code).setResponse(response)
    }

    static GsApiResponseData successResponseWithTotal(Object response, Integer total, Integer code = null){
        return instance(true).setCode(code).setResponse(response).setTotal(total)
    }


    static GsApiResponseData instance(Boolean isSuccess){
        return new GsApiResponseData(isSuccess)
    }

    GsApiResponseData setErrorDetails(List errorDetails) {
        this.errorDetails = errorDetails
        return this

    }

    Map toMap(){
        LinkedHashMap<String, Object> responseMap = [
                "isSuccess" : isSuccess,
        ]

        if (code != null){responseMap.code = code}
        if (total != null){responseMap.total = total}

        if (isExist != null){responseMap.isExist = isExist}
        if (count != null){responseMap.count = count}

        if (message != null){responseMap.message = message}
        if (response != null){
            responseMap.put(GsConfigHolder.responseKey(), response)
        }
        if (errorDetails){responseMap.errorDetails = errorDetails}
        return responseMap
    }

    List getErrorDetails() {
        return errorDetails
    }

    static SwaggerProperty swaggerResponseProperty(GsApiResponseData apiResponseData, Boolean isExample = true) {
        SwaggerProperty swaggerProperty = null
        if (apiResponseData) {
            LinkedHashMap dataTypes = GsReflectionUtil.getMetaClassToSwaggerDataType(apiResponseData.metaClass)
            swaggerProperty = new SwaggerProperty()
            dataTypes?.each {
                try {
                    if (apiResponseData[it.key] != null) {
                        swaggerProperty.property(it.key.toString(), it.value.toString())
                        if (isExample) {
                            swaggerProperty.example(apiResponseData[it.key])
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return swaggerProperty
    }

    static Map processAPIResponse(GsApiActionDefinition gsApiActionDefinition, GsInternalResponse gsInternalResponse) {
        GsApiResponseData gsApiResponseData
        if (gsInternalResponse.isSuccess) {
            gsApiResponseData = gsApiActionDefinition.successResponseFormat ?: GsConfigHolder.defaultSuccessResponse
            gsApiResponseData.isSuccess = true
        } else {
            gsApiResponseData = gsApiActionDefinition.failedResponseFormat ?: GsConfigHolder.defaultFailedResponse
            gsApiResponseData.isSuccess = false
        }

        if (gsApiResponseData.message  != null && gsInternalResponse.message != null) {
            gsApiResponseData.message = gsInternalResponse.message
        }

        if (gsApiResponseData.total != null && gsInternalResponse.total != null) {
            gsApiResponseData.total = gsInternalResponse.total
        }

        if (gsApiResponseData.response != null && gsInternalResponse.response != null) {
            gsApiResponseData.response = gsInternalResponse.response
        }

        if (gsApiResponseData.errorDetails != null && gsInternalResponse.errorDetails != null) {
            gsApiResponseData.errorDetails = gsInternalResponse.errorDetails
        }

        if (gsApiResponseData.count != null && gsInternalResponse.count != null) {
            gsApiResponseData.count = gsInternalResponse.count
        }

        if (gsApiResponseData.isExist != null && gsInternalResponse.isExist != null) {
            gsApiResponseData.isExist = gsInternalResponse.isExist
        }
        return gsApiResponseData.toMap()
    }

    Integer getTotal() {
        return total
    }

    GsApiResponseData setTotal(Integer total) {
        this.total = total
        return this
    }
}
