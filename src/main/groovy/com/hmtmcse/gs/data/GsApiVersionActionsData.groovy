package com.hmtmcse.gs.data

class GsApiVersionActionsData {

    public String versionPrefix
    public List<GsControllerActionData> controllerActionData = new ArrayList<>()

    String getVersionPrefix() {
        return versionPrefix
    }

    void setVersionPrefix(String versionPrefix) {
        this.versionPrefix = versionPrefix
    }

    List<GsControllerActionData> getControllerActionData() {
        return controllerActionData
    }

    void setControllerActionData(List<GsControllerActionData> controllerActionData) {
        this.controllerActionData = controllerActionData
    }
}
