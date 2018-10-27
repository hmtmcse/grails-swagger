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
    Boolean isSuccess = false

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
        if (message){responseMap.message = message}
        if (response){
            responseMap.put(GsConfigHolder.responseKey(), response)
        }
        if (code){responseMap.code = code}
        if (total){responseMap.total = total}
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
        if (gsInternalResponse.isSuccess) {
            GsApiResponseData successResponseFormat = gsApiActionDefinition.successResponseFormat ?: GsConfigHolder.defaultSuccessResponse
            successResponseFormat.isSuccess = true
            if (successResponseFormat.message && gsInternalResponse.message){
                successResponseFormat.message =  gsInternalResponse.message
            }
            return successResponseFormat.toMap()
        } else {
            GsApiResponseData failedResponseFormat = gsApiActionDefinition.failedResponseFormat ?: GsConfigHolder.defaultFailedResponse
            failedResponseFormat.isSuccess = false
            if (failedResponseFormat.message && gsInternalResponse.message){
                failedResponseFormat.message =  gsInternalResponse.message
            }
            if (failedResponseFormat.errorDetails != null && gsInternalResponse.errorDetails != null){
                failedResponseFormat.errorDetails =  gsInternalResponse.errorDetails
            }
            return failedResponseFormat.toMap()
        }
        return null
    }

}
