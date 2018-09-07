package com.hmtmcse.gs.data

class GsApiResponseData {

    Boolean isSuccess = false
    String message = null
    def response = null
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
        return new GsApiResponseData(false, message)
    }

    static GsApiResponseData successMessage(String message, Integer code = 0){
        return new GsApiResponseData(true, message)
    }

    static GsApiResponseData successResponse(def response, Integer code = 0){
        return instance(true).setCode(code).setResponse(response)
    }


    static GsApiResponseData instance(Boolean isSuccess){
        return new GsApiResponseData(isSuccess)
    }

    Map toMap(){
        Map responseMap = [
                "isSuccess" : isSuccess,
        ]
        if (message){responseMap.message = message}
        if (response){responseMap.response = response}
        if (code){responseMap.code = code}
        return responseMap
    }
}
