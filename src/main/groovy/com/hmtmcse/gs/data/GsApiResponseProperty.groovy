package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsRelationalEntityResponse

class GsApiResponseProperty extends GsRequestResponseProperty {


    public GsRelationalEntityResponse relationalEntity

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


    String getMapKey() {
        return alias != null ? alias : name
    }

    List<GsApiResponseProperty> getHasMany() {
        return hasMany
    }

    GsApiResponseProperty setDataType(String dataType) {
        super.dataType = dataType
        return this
    }
}
