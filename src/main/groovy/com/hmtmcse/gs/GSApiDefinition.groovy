package com.hmtmcse.gs

import com.hmtmcse.gs.data.GSApiRequestProperty
import com.hmtmcse.gs.data.GSApiResponseProperty

class GSApiDefinition {


    private Map<String, GSApiResponseProperty> responseProperties = new HashMap<>()
    private Map<String, GSApiRequestProperty> requestProperties = new HashMap<>()
    public String definitionFor = null
    public String description = null
    public Closure queryConditions = null


    public addResponseProperty(){}

    public addRequestProperty(){}


    public excludeProperty(List<String> fields){

    }


    public includeOnlyProperty(List<String> fields){
        fields?.each { String field ->
            responseProperties.put(field, new GSApiResponseProperty(field))
        }
    }


    public getRequestProperties(){
        return requestProperties
    }


    public getResponseProperties(){
        return responseProperties
    }

}
