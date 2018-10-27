package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty


class GsApiActionDefinition<T> {


    private Map<String, GsApiResponseProperty> responseProperties = new LinkedHashMap<>()
    private Map<String, GsApiRequestProperty> requestProperties = new LinkedHashMap<>()
    public String description = null
    public String modelDefinition = null
    public String summary = null
    public String definitionFor = null
    public String responseType = null
    public Closure queryConditions = null
    public Boolean enableFilter = true
    public Boolean enableWhere = false
    public Class<T> domain

    public String parameterDescription = null
    public String parameterName = null
    public GsApiResponseData successResponseFormat = GsConfigHolder.defaultSuccessResponse
    public GsApiResponseData failedResponseFormat = null
    public List whereAllowedPropertyList = []
    public Map whereAllowedPropertyMap = [:]



    public GsApiActionDefinition(Class<T> domain){
        this.domain = domain
    }


    public Map domainFields(){
        return  GsReflectionUtil.getDomainToSwaggerDataType(this.domain)
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

    public GsApiActionDefinition<T> addRequestProperty(String name, String alias = null, String defaultValue = ""){
        requestProperties.put(name,
                new GsApiRequestProperty(name).setAlias(alias).setDefaultValue(defaultValue))
        return this
    }


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

    public setModelDefinition(String apiVersion, String controller, GsAction gsAction){
        this.modelDefinition = "${GsUtil.makeHumReadble(apiVersion)}${GsUtil.makeHumReadble(controller)}${GsUtil.makeHumReadble(gsAction.httpMethod)}${GsUtil.makeHumReadble(gsAction.name)}"
    }
}
