package com.hmtmcse.gs.data

class GsAction{
    String name
    String actionRealName
    String httpMethod

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getHttpMethod() {
        return httpMethod
    }

    void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod
    }

    String getActionRealName() {
        return actionRealName
    }

    void setActionRealName(String actionRealName) {
        this.actionRealName = actionRealName
    }
}
