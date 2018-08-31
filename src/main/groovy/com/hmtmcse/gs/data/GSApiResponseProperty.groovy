package com.hmtmcse.gs.data

class GSApiResponseProperty {

    public String name
    public String alias = null
    public Closure<String> closure = null

    GSApiResponseProperty(String name, String alias) {
        this.name = name
        this.alias = alias
    }

    GSApiResponseProperty(String name) {
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
