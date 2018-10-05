package com.hmtmcse.swagger

import org.grails.core.DefaultGrailsControllerClass

class GrailsSwagger {

    def xyl(DefaultGrailsControllerClass controller){
        controller.metaClass.setProperty(controller, "isDefinition", true)
        controller.metaClass.getProperty(controller, "isDefinition")
        controller.metaClass.invokeMethod(controller.newInstance(), "getList")
    }
}
