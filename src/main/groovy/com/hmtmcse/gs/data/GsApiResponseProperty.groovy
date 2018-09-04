package com.hmtmcse.gs.data

class GsApiResponseProperty {

    public String name
    public String alias = null
    public String dataType = null
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

    void setAlias(String alias) {
        this.alias = alias
    }

    Closure getClosure() {
        return closure
    }

    void setClosure(Closure closure) {
        this.closure = closure
    }
}
