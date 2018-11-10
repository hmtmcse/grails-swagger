package com.hmtmcse.gs.data

class GsUrlMappingConfig {

    public String controllerRegex = ""
    public String actionRegex = ""
    public String camelCaseTo = ""

    String getControllerRegex() {
        return controllerRegex
    }

    void setControllerRegex(String controllerRegex) {
        this.controllerRegex = controllerRegex
    }

    String getActionRegex() {
        return actionRegex
    }

    void setActionRegex(String actionRegex) {
        this.actionRegex = actionRegex
    }

    String getCamelCaseTo() {
        return camelCaseTo
    }

    void setCamelCaseTo(String camelCaseTo) {
        this.camelCaseTo = camelCaseTo
    }
}
