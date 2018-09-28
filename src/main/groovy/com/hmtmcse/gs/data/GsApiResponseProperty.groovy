package com.hmtmcse.gs.data

class GsApiResponseProperty {

    public String name
    public String alias = null
    public String dataType = null
    public String defaultValue = ""
    public Closure<String> closure = null

    GsApiResponseProperty(String name, String alias) {
        this.name = name
        this.alias = alias
    }

    GsApiResponseProperty(String name) {
        this.name = name
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getAlias() {
        return alias
    }

    GsApiResponseProperty setAlias(String alias) {
        this.alias = alias
        return this;
    }

    Closure getClosure() {
        return closure
    }

    void setClosure(Closure closure) {
        this.closure = closure
    }

    String getMapKey(){
        return alias != null ? alias : name
    }

    String getDefaultValue() {
        return defaultValue
    }

    void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue
    }
}
