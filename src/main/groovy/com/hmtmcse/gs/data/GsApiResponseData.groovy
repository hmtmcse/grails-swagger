package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.GsReflectionUtil
import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerProperty

class GsApiResponseData {

    Boolean isSuccess = false
    String message = null
    Object response = null
    Integer code = 0

    GsApiResponseData(Boolean isSuccess, String message, Integer code = 0) {
        this.isSuccess = isSuccess
        this.message = message
        this.code = code
    }

    GsApiResponseData(Boolean isSuccess, Integer code = 0) {
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

    static GsApiResponseData failed(String message, Integer code = 0){
        return new GsApiResponseData(false, message).setCode(code)
    }

    static GsApiResponseData successMessage(String message, Integer code = 0){
        return new GsApiResponseData(true, message).setCode(code)
    }

    static GsApiResponseData successResponse(Object response, Integer code = 0){
        return instance(true).setCode(code).setResponse(response)
    }


    static GsApiResponseData instance(Boolean isSuccess){
        return new GsApiResponseData(isSuccess)
    }

    Map toMap(){
        Map<String, Object> responseMap = [
                "isSuccess" : isSuccess,
        ]
        if (message){responseMap.message = message}
        if (response){
            responseMap.put(GsConfigHolder.responseKey(), response)
        }
        if (code){responseMap.code = code}
        return responseMap
    }


    static SwaggerProperty swaggerResponseProperty(GsApiResponseData apiResponseData, Boolean isExample = true) {
        SwaggerProperty swaggerProperty = null
        if (apiResponseData) {
            Map dataTypes = GsReflectionUtil.getMetaClassToSwaggerDataType(apiResponseData.metaClass)
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

}
