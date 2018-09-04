package com.hmtmcse.gs.data

class GsApiRequestProperty {

    public String name
    public Boolean isRequired = false
    public String alias = null
    public String errorMessage = null
    public String helpText = null
    public Closure<String> closure = null
    public String dataType = null

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    Boolean getIsRequired() {
        return isRequired
    }

    void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired
    }

    String getAlias() {
        return alias
    }

    void setAlias(String alias) {
        this.alias = alias
    }

    String getErrorMessage() {
        return errorMessage
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage
    }

    String getHelpText() {
        return helpText
    }

    void setHelpText(String helpText) {
        this.helpText = helpText
    }

    Closure getClosure() {
        return closure
    }

    void setClosure(Closure closure) {
        this.closure = closure
    }
}
