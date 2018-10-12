package com.hmtmcse.gs.system

import com.hmtmcse.gs.GsUrlMappingUtil
import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsControllerActionData


class UrlMappings {


    static mappings = {
        "/gsExceptionHandler"(controller: "gsExceptionHandlerController", action: "invalid")
        "/gsExceptionHandler/**"(controller: "gsExceptionHandlerController", action: "invalid")
        "/${GsUrlMappingUtil.apiPrefix()}"(controller: "gsExceptionHandler", action: "invalid")
        "/${GsUrlMappingUtil.apiPrefix()}/**"(controller: "gsExceptionHandler", action: "invalid")
        GsUrlMappingUtil.getUrlMappingData().each { GsControllerActionData urls ->
            urls.actions.each { GsAction gsAction ->
                "${gsAction.httpMethod}"("${urls.relativeURL}${gsAction.name}"(controller:urls.controllerRealName, action: gsAction.actionRealName))
            }
            "/${urls.controllerName}"(controller: "gsExceptionHandler", action: "invalid")
            "/${urls.controllerName}/**"(controller: "gsExceptionHandler", action: "invalid")
        }
    }
}
