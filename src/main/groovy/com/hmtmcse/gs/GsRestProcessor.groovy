package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON
import org.grails.web.converters.Converter

class GsRestProcessor implements GsExceptionHandler {

    GsRestfulService gsRestfulService
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
        return render(gsRestfulService.gsDelete(definition, params) as JSON)
    }

    private deleteForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.DELETE_RESPONSE
        return definition
    }

    def delete(GsApiActionDefinition definition){
        definition.enableWhere = true
        return "delete${returnFor}"(definition)
    }


    private customProcessorForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCustomProcessor(definition, params) as JSON)
    }


    private customProcessorForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.DELETE_RESPONSE
        return definition
    }


    def customProcessor(GsApiActionDefinition definition){
        definition.enableWhere = true
        return "customProcessor${returnFor}"(definition)
    }


    private jsonResponseTo(GsApiResponseData gsApiResponseData){
        return render(gsApiResponseData.toMap() as JSON)
    }


    private def responseTo(GsApiResponseData gsApiResponseData){
        return jsonResponseTo(gsApiResponseData)
    }

    public void swaggerInit(){}

}
