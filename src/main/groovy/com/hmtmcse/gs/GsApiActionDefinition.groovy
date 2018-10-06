package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty

import java.lang.reflect.Modifier


class GsApiActionDefinition<T> {


    private Map<String, GsApiResponseProperty> responseProperties = new HashMap<>()
    private Map<String, GsApiRequestProperty> requestProperties = new HashMap<>()
    public String definitionFor = null
    public String description = null
    public Closure queryConditions = null
    public Boolean enableFilter = true
    public Class<T> domain


    public GsApiActionDefinition(Class<T> domain){
        this.domain = domain
    }


    public Map<String, Object> domainFields(){
        Map<String, Object> domainAllField = new HashMap<>()

        GsReflectionUtil.getAllProperty(this.domain)
        return domainAllField
    }




    public static GsApiActionDefinition instance(Class<T> domain){
        return new GsApiActionDefinition(domain)
    }

    public GsApiActionDefinition<T> addResponseProperty(String name, String alias = null, String defaultValue = ""){
        responseProperties.put(name,
                new GsApiResponseProperty(name).setAlias(alias).setDefaultValue(defaultValue))
        return this
    }

    public GsApiActionDefinition<T> addResponseProperty(GsApiResponseProperty responseProperty){
        responseProperties.put(responseProperty.getName(), responseProperty)
        return this
    }

    public GsApiResponseProperty responsePropertyDefine(String name){
        return new GsApiResponseProperty(name)
    }

    public addRequestProperty(){}


    public excludeProperty(List<String> fields){

    }


    public GsApiActionDefinition<T> includeOnlyProperty(List<String> fields){
        fields?.each { String field ->
            responseProperties.put(field, new GsApiResponseProperty(field))
        }
        return this
    }


    public getRequestProperties(){
        return requestProperties
    }


    public getResponseProperties(){
        return responseProperties
    }

}
