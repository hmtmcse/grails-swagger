package com.hmtmcse.gs.data

class GsControllerActionData {

    class GsAction{
        String name
        String httpMethod
    }

    public String controllerName
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
}
