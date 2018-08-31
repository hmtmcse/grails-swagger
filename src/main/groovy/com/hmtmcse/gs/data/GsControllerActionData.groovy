package com.hmtmcse.gs.data

class GsControllerActionData {

    public String controllerName
    public List<String> actions = new ArrayList<>()

    String getControllerName() {
        return controllerName
    }

    void setControllerName(String controllerName) {
        this.controllerName = controllerName
    }

    List<String> getActions() {
        return actions
    }

    void setActions(List<String> actions) {
        this.actions = actions
    }
}
