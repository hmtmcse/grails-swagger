package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON
import org.grails.web.converters.Converter

class GsRestProcessor implements GsExceptionHandler {

    GsRestfulService gsRestfulService
    public Boolean isDefinition = false
    public String tagName = null
    public String tagDescription = null
    public String returnFor = GsConstant.RETURN_FOR_API


    private listForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsReadList(definition, params) as JSON)
    }

    private listForDefinition(GsApiActionDefinition definition){
        return definition
    }

    def list(GsApiActionDefinition definition){
        definition.responseType = GsConstant.LIST_RESPONSE
        definition.enableWhere = true
        return "list${returnFor}"(definition)
    }


    private detailsForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsDetails(definition, params) as JSON)
    }

    private detailsForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.DETAILS_RESPONSE
        return definition
    }

    def details(GsApiActionDefinition definition){
        definition.enableWhere = true
        return "details${returnFor}"(definition)
    }


    private createForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCreate(definition, params) as JSON)
    }

    private createForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.CREATE_RESPONSE
        return definition
    }

    def create(GsApiActionDefinition definition){
        return "create${returnFor}"(definition)
    }


    private updateForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsUpdate(definition, params) as JSON)
    }

    private updateForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.UPDATE_RESPONSE
        return definition
    }

    def update(GsApiActionDefinition definition){
        definition.enableWhere = true
        return "update${returnFor}"(definition)
    }


    private deleteForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsDetails(definition, params) as JSON)
    }

    private deleteForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.DELETE_RESPONSE
        return definition
    }

    def delete(GsApiActionDefinition definition){
        definition.enableWhere = true
        return "delete${returnFor}"(definition)
    }


    private customQueryForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCustomQuery(definition, params) as JSON)
    }

    private customQueryForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.CUSTOM_QUERY_RESPONSE
        return definition
    }

    def customQuery(GsApiActionDefinition definition){
        return "customQuery${returnFor}"(definition)
    }


    private customQueryAndResponseForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCustomQueryAndResponse(definition, params) as JSON)
    }

    private customQueryAndResponseForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.CUSTOM_QUERY_AND_RESPONSE
        return definition
    }

    def customQueryAndResponse(GsApiActionDefinition definition){
        return "customQueryAndResponse${returnFor}"(definition)
    }









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




    def renderAsJson(def data){
        if (!isDefinition){
            return render(data as JSON)
        }
    }

    def exception(String message){
        throw new GsException(message)
    }

    def exception(){
        throw new GsException()
    }

    def get(GsApiActionDefinition definition){
        JSON.registerObjectMarshaller(definition.domain){
            def output = [:]
            output['name'] = it.name
            return output
        }
        return  render(definition.domain.list() as JSON)
    }

    public void swaggerInit(){}

}
