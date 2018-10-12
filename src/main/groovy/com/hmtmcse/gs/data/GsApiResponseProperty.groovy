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
        this.alias = alias
        return this
    }


    String getMapKey(){
        return alias != null ? alias : name
    }

}
