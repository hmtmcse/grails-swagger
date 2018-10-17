package com.hmtmcse.gs.data

class GsApiResponseProperty extends GsRequestResponseProperty{



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


    String getMapKey(){
        return alias != null ? alias : name
    }
}
