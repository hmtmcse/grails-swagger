package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty


class GsApiActionDefinition<T> {


    private Map<String, GsApiResponseProperty> responseProperties = new HashMap<>()
    private Map<String, GsApiRequestProperty> requestProperties = new HashMap<>()
    public String definitionFor = null
    public String description = null
    public Closure queryConditions = null
    public Boolean enableFilter = true
    private Class<T> domain
    public T instance


    public GsApiActionDefinition(Class<T> domain){
        this.domain = domain
        this.instance = domain.newInstance()
    }


    public static GsApiActionDefinition instance(Class<T> domain){
        return new GsApiActionDefinition(domain)
    }

    public addResponseProperty(Object property){
       println( property.toString())

    }

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
