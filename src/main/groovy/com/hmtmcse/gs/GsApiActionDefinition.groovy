package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsDomain
import com.hmtmcse.gs.data.GsWhereFilterProperty
import com.hmtmcse.gs.model.CustomProcessor


class GsApiActionDefinition<T> implements GsResponseOrganizer<GsApiActionDefinition>, GsRequestOrganizer<GsApiActionDefinition>, GsDataFilter<GsApiActionDefinition> {


    private LinkedHashMap<String, GsApiResponseProperty> responseProperties = new LinkedHashMap<>()
    private LinkedHashMap<String, GsApiRequestProperty> requestProperties = new LinkedHashMap<>()
    public String description = null
    public String modelDefinition = null
    public String summary = null
    public String responseType = null
    public CustomProcessor customProcessor = null
    public Boolean enableWhere = false
    public Class<T> domain

    public String lastResponseRelationalEntityName = null
    public String parameterName = null
    public GsApiResponseData successResponseFormat = null
    public GsApiResponseData failedResponseFormat = null
    public List<GsWhereFilterProperty> whereAllowedPropertyList = []
    public LinkedHashMap<String, GsWhereFilterProperty> whereAllowedPropertyMap = [:]
    public GsDomain gsDomain = new GsDomain()


    public GsApiActionDefinition() {
        gsDomain = GsReflectionUtil.getDomainToDomainProperties(this.domain)
    }


    public GsApiActionDefinition(Class<T> domain) {
        this.domain = domain
        gsDomain = GsReflectionUtil.getDomainToDomainProperties(this.domain)
    }


    public Map domainFields() {
        return GsReflectionUtil.getDomainToSwaggerDataType(this.domain)
    }


    public static GsApiActionDefinition instance(Class<T> domain) {
        return new GsApiActionDefinition(domain)
    }

    public GsApiActionDefinition<T> successResponseAsData(def isMapOrList = [:], Integer code = null) {
        successResponseFormat = GsApiResponseData.successResponse(isMapOrList, code)
        return this
    }



    @Override
    LinkedHashMap<String, GsApiResponseProperty> getResponseProperties() {
        return this.responseProperties
    }


    @Override
    LinkedHashMap<String, GsApiResponseProperty> setResponseProperties(LinkedHashMap<String, GsApiResponseProperty> responsePropertyLinkedHashMap) {
        return this.responseProperties = responsePropertyLinkedHashMap
    }

    @Override
    LinkedHashMap<String, GsApiRequestProperty> getRequestProperties() {
        return this.requestProperties
    }

    @Override
    LinkedHashMap<String, GsApiRequestProperty> setRequestProperties(LinkedHashMap<String, GsApiRequestProperty> requestPropertyLinkedHashMap) {
        return this.requestProperties = requestPropertyLinkedHashMap
    }

    @Override
    List<GsWhereFilterProperty> getWhereAllowedPropertyList() {
        return this.whereAllowedPropertyList
    }

    @Override
    List<GsWhereFilterProperty> setWhereAllowedPropertyList(List<GsWhereFilterProperty> list) {
        return this.whereAllowedPropertyList = list
    }

    @Override
    LinkedHashMap<String, GsWhereFilterProperty> getWhereAllowedPropertyMap() {
        return this.whereAllowedPropertyMap
    }

    @Override
    LinkedHashMap<String, GsWhereFilterProperty> setWhereAllowedPropertyMap(LinkedHashMap<String, GsWhereFilterProperty> map) {
        return this.whereAllowedPropertyMap = map
    }

    @Override
    GsDomain getGsDomain() {
        return this.gsDomain
    }

    public setModelDefinition(String apiVersion, String controller, GsAction gsAction) {
        this.modelDefinition = "${GsUtil.makeHumReadble(apiVersion)}${GsUtil.makeHumReadble(controller)}${GsUtil.makeHumReadble(gsAction.httpMethod)}${GsUtil.makeHumReadble(gsAction.name)}"
    }


    public GsRelationalEntityResponse addRelationalEntityResponse(String name, String alias = null, String defaultValue = "") {
        GsApiResponseProperty gsApiResponseProperty = new GsApiResponseProperty(name).setAlias(alias).setDefaultValue(defaultValue)
        gsApiResponseProperty.relationalEntity = new GsRelationalEntityResponse()
        def relationalProperties = gsDomain.domainProperties.get(name)?.relationalProperties
        gsApiResponseProperty.relationalEntity.gsDomain.domainProperties = relationalProperties ?: new LinkedHashMap<>()
        this.responseProperties.put(name, gsApiResponseProperty)
        lastResponseRelationalEntityName = name
        return this.responseProperties.get(name).relationalEntity
    }


    public GsRelationalEntityResponse reResponseData() {
        return this.responseProperties.get(lastResponseRelationalEntityName).relationalEntity
    }

}
