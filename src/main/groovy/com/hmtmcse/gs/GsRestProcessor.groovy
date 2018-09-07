package com.hmtmcse.gs

import grails.converters.JSON

class GsRestProcessor {

    GsRestfulService gsRestfulService
    Boolean isDefinition = false

    private Map mapResponseTo(Boolean isSuccess, String message = null, def response = null, Integer codes = 0){
        Map responseMap = [
                "isSuccess" : isSuccess,
        ]
        if (message){responseMap.message = message}
        if (response){responseMap.response = response}
        if (codes){responseMap.codes = codes}
        return responseMap
    }


    private jsonResponseTo(Boolean isSuccess, String message = null, def response = null, Integer codes = 0){
        return render(mapResponseTo(isSuccess, message, response, codes) as JSON)
    }


    private def responseTo(Boolean isSuccess, String message = null, def response = null, Integer codes = 0){
        return jsonResponseTo(isSuccess, message, response, codes)
    }

    def gsSuccessMessage(String message){
        return jsonResponseTo(true, message)
    }

    def gsFailedMessage(String message){
        return jsonResponseTo(false, message)
    }


    def gsSuccessResponse(String message){
        return jsonResponseTo(true, message)
    }

    def gsRead(GsApiActionDefinition definition) {
        return isDefinition ? definition : {
            gsRestfulService.gsRead(definition)
        }
    }

    def gsReadList(GsApiActionDefinition definition){}

    def gsCreate(GsApiActionDefinition definition){}

    def gsUpdate(GsApiActionDefinition definition){}

    def gsDelete(GsApiActionDefinition definition){}



}
