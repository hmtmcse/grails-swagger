package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiVersionActionsData
import grails.util.Holders
import org.grails.core.DefaultGrailsControllerClass

class GsUrlMappingUtil {

    private static String parentController = "GsRestful"


    static List<GsApiVersionActionsData> getUrlMappingData() {
        Holders.grailsApplication.controllerClasses.each{ DefaultGrailsControllerClass controller ->
            if (controller.name.startsWith(GsConfigService.controllerStartWith())){
                println(controller.name)
                println(controller.actions)
            }
        }
        return []
    }
}
