package com.hmtmcse.gs.data

class GsControllerActionData {

    public String controllerName
    public String apiVersion
    public String url
    public List<GsAction> actions = new ArrayList<>()

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

}
