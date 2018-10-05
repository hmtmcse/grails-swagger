package com.hmtmcse.gs

import org.grails.core.DefaultGrailsControllerClass

class GsReflectionUtil {

    static def getPropertyValue(DefaultGrailsControllerClass controller, String name){
        return controller.metaClass.getProperty(controller.newInstance(), name)
    }
}
