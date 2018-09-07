package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON

class GsRestProcessor {

    GsRestfulService gsRestfulService
    Boolean isDefinition = false


    private jsonResponseTo(GsApiResponseData gsApiResponseData){
        return render(gsApiResponseData.toMap() as JSON)
    }


    private def responseTo(GsApiResponseData gsApiResponseData){
        return jsonResponseTo(gsApiResponseData)
    }

    def gsSuccessMessage(String message){
    }

    def gsFailedMessage(String message){

    }


    def gsSuccessResponse(String message){

    }

    def gsReadResponse(GsApiActionDefinition definition) {
        return isDefinition ? definition : responseTo(gsRestfulService.gsRead(definition))
    }

    def gsReadListResponse(GsApiActionDefinition definition){}

    def gsCreateResponse(GsApiActionDefinition definition){}

    def gsUpdateResponse(GsApiActionDefinition definition){}

    def gsDeleteResponse(GsApiActionDefinition definition){}



}
