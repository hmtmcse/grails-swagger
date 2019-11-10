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
    String status = null
    Integer httpCode = HTTPConst.SUCCESS
    Integer total = null
    Integer offset = null
    Integer itemPerPage = null
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

    Map toV2Map(){
        Map responseMap = [
                "status" : status ?: (isSuccess ? HTTPConst.SUCCESS_STATUS : HTTPConst.ERROR_STATUS)
        ]
        if (count != null){
            Map count = ["count": count]
            responseMap.data = count
        }

        if (response != null){
            responseMap.data = response
        }

        if (isSuccess){
            if (message != null){responseMap.message = message}
        }else{
            Map error = [:]
            if (message != null){error.message = message}
            if (errorDetails){error.fields = errorDetails}
            responseMap.error = error
        }

        if (total != null && offset != null && itemPerPage != null){
            Map pagination = [
                    "total": total,
                    "offset": offset,
                    "itemPerPage": itemPerPage
            ]
            responseMap.pagination = pagination
        }

        return responseMap
    }

    Map toMap(){
        return "to${GsConfigHolder.systemVersion}Map"()
    }

    Map toV1Map(){
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
        if (gsApiActionDefinition.responsePostProcessor){
            GsResponsePostData responsePostData = new GsResponsePostData(gsInternalResponse.isSuccess, gsInternalResponse.response, gsInternalResponse.queryResult)
            responsePostData = gsApiActionDefinition.responsePostProcessor.process(gsApiActionDefinition, responsePostData)
            gsInternalResponse.isSuccess = responsePostData.isSuccess
            gsInternalResponse.response = responsePostData.response
            gsInternalResponse.message = responsePostData.message
        }
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

        if (gsApiResponseData.offset != null && gsInternalResponse.offset != null) {
            gsApiResponseData.offset = gsInternalResponse.offset
        }

        if (gsApiResponseData.itemPerPage != null && gsInternalResponse.itemPerPage != null) {
            gsApiResponseData.itemPerPage = gsInternalResponse.itemPerPage
        }

        if (gsApiResponseData.httpCode != null && gsInternalResponse.httpCode != null) {
            gsApiResponseData.httpCode = gsInternalResponse.httpCode
        }
        return gsApiResponseData."to${GsConfigHolder.systemVersion}Map"()
    }

    Integer getTotal() {
        return total
    }

    GsApiResponseData setTotal(Integer total) {
        this.total = total
        return this
    }
}
