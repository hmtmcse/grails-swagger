package com.hmtmcse.gs.data

class GsWhereFilterProperty extends GsRequestResponseProperty {

    public Boolean isTypeCast = false

    GsWhereFilterProperty(String name, String dataType) {
        this.name = name
        this.dataType = dataType
    }


    GsWhereFilterProperty(String name) {
        this.name = name
    }


    Boolean getIsTypeCast() {
        return isTypeCast
    }


    GsWhereFilterProperty typeCast() {
        this.isTypeCast = true
        return this
    }

    GsWhereFilterProperty setDataType(String dataType) {
        this.dataType = dataType
        return this
    }



}
