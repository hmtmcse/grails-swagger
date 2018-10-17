package com.hmtmcse.gs.data

class GsApiResponseProperty extends GsRequestResponseProperty{


    public GsApiResponseData successResponseFormat = GsApiResponseData.successResponse("Success Data")

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

    GsApiResponseData getSuccessResponseFormat() {
        return successResponseFormat
    }

    GsApiResponseProperty setSuccessResponseFormat(GsApiResponseData successResponseFormat) {
        this.successResponseFormat = successResponseFormat
        return this
    }
}
