package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON

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
        definition.allowedAllCondition()
        definition.enablePaginationAndSorting()
        definition.enableQueryFilter()
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
        definition.allowedConditionEqualAndOr()
        definition.disablePaginationAndSorting()
        definition.enableQueryFilter()
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
        definition.disableCondition()
        definition.disablePaginationAndSorting()
        definition.disableQueryFilter()
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
        definition.allowedConditionEqualAndOr()
        definition.disablePaginationAndSorting()
        definition.enableQueryFilter()
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
        definition.allowedConditionEqualAndOr()
        definition.disablePaginationAndSorting()
        definition.enableQueryFilter()
        return "delete${returnFor}"(definition)
    }


    private countForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCount(definition, params) as JSON)
    }

    private countForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.COUNT_RESPONSE
        return definition
    }

    def count(GsApiActionDefinition definition){
        return "count${returnFor}"(definition)
    }


    private customProcessorForApi(GsApiActionDefinition definition){
        return render(gsRestfulService.gsCustomProcessor(definition, params) as JSON)
    }


    private customProcessorForDefinition(GsApiActionDefinition definition){
        definition.responseType = GsConstant.CUSTOM_PROCESSOR
        return definition
    }


    def customProcessor(GsApiActionDefinition definition){
        definition.enableWhere = false
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
