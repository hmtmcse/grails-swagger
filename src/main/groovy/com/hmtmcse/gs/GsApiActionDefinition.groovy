package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty

class GsApiActionDefinition {


    private Map<String, GsApiResponseProperty> responseProperties = new HashMap<>()
    private Map<String, GsApiRequestProperty> requestProperties = new HashMap<>()
    public String definitionFor = null
    public String description = null
    public Closure queryConditions = null


    public addResponseProperty(){}

    public addRequestProperty(){}


    public excludeProperty(List<String> fields){

    }


    public includeOnlyProperty(List<String> fields){
        fields?.each { String field ->
            responseProperties.put(field, new GsApiResponseProperty(field))
        }
    }


    public getRequestProperties(){
        return requestProperties
    }


    public getResponseProperties(){
        return responseProperties
    }

}
