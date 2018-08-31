package com.hmtmcse.gs.controller

import com.hmtmcse.gs.GsRestfulService
import grails.converters.JSON

class GsRestfulController {

    GsRestfulService gsRestfulService

    private mapResponseTo(Boolean isSuccess, String message = null, def response = null, Integer codes = 0){
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


    def gsSuccessMessage(String message){
        return jsonResponseTo(true, message)
    }

    def gsFailedMessage(String message){
        return jsonResponseTo(false, message)
    }


    def gsSuccessResponse(String message){
        return jsonResponseTo(true, message)
    }

}
