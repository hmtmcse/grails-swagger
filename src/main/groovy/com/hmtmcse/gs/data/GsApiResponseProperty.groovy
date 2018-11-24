package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsRelationalGsResponseEntity

class GsApiResponseProperty extends GsRequestResponseProperty {


    public GsRelationalGsResponseEntity relationalEntity = null

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

}
