package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsRelationalEntityResponse
import com.hmtmcse.gs.model.CustomResponseParamProcessor

class GsApiResponseProperty extends GsRequestResponseProperty {


    public GsRelationalEntityResponse relationalEntity = null
    public CustomResponseParamProcessor customResponseParamProcessor = null

    GsApiResponseProperty() {}

    GsApiResponseProperty(String name, String alias) {
        this.name = name
        this.alias = alias
    }

    GsApiResponseProperty(String name) {
        this.name = name
    }


    GsApiResponseProperty setAlias(String alias) {
        super.alias = alias
        return this
    }


    GsApiResponseProperty setDefaultValue(String defaultValue) {
        super.defaultValue = alias
        return this
    }


    List<GsApiResponseProperty> getHasMany() {
        return hasMany
    }

    GsApiResponseProperty setDataType(String dataType) {
        super.dataType = dataType
        return this
    }
}
