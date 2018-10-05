package com.hmtmcse.gs.data

import org.grails.core.DefaultGrailsControllerClass

class GsControllerActionData {

    public String controllerName
    public String controllerRealName
    public String controllerUrlName
    public String apiVersion
    public String url
    public List<GsAction> actions = new ArrayList<>()
    public DefaultGrailsControllerClass controllerClass

    String getControllerName() {
        return controllerName
    }

    void setControllerName(String controllerName) {
        this.controllerName = controllerName
    }

    List<GsAction> getActions() {
        return actions
    }

    void setActions(List<GsAction> actions) {
        this.actions = actions
    }

    void addAction(GsAction action) {
        this.actions.add(action)
    }

    String getApiVersion() {
        return apiVersion
    }

    void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion
    }

    String getUrl() {
        return url
    }

    void setUrl(String url) {
        this.url = url
    }

    String getControllerRealName() {
        return controllerRealName
    }

    void setControllerRealName(String controllerRealName) {
        this.controllerRealName = controllerRealName
    }

    String getControllerUrlName() {
        return controllerUrlName
    }

    void setControllerUrlName(String controllerUrlName) {
        this.controllerUrlName = controllerUrlName
    }
}
