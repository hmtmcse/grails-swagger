package com.hmtmcse.gs.data

class GsApiResponseProperty extends GsRequestResponseProperty {


    public LinkedHashMap<String, GsApiNestedResponse> nested = new LinkedHashMap<>()
    

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


    private GsApiResponseProperty addHasManyOrOne(String name, Boolean isMany) {
        GsApiNestedResponse gsApiNestedResponse = new GsApiNestedResponse()
        gsApiNestedResponse.isList = isMany
        nested.put(name, gsApiNestedResponse)
        return nested.get(name).gsApiResponseProperty
    }


    GsApiResponseProperty addHasMany(String name) {
        return addHasManyOrOne(name, true)
    }


    GsApiResponseProperty addHasOne(String name) {
        return addHasManyOrOne(name, false)
    }

}
