package com.hmtmcse.gs.data

class GsApiRequestProperty extends GsRequestResponseProperty{

    public Boolean isRequired = false
    public String errorMessage = null
    public String helpText = null


    Boolean getIsRequired() {
        return isRequired
    }

    void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired
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

}
