package com.hmtmcse.gs

import org.grails.core.DefaultGrailsControllerClass

class GsReflectionUtil {

    static def getPropertyValue(DefaultGrailsControllerClass controller, String name){
        try {
            return controller.metaClass.getProperty(controller.newInstance(), name)
        }catch(Exception e){
            return null
        }
    }

    static def getNewObject(DefaultGrailsControllerClass controller){
        try {
            return controller.newInstance()
        }catch(Exception e){
            return null
        }
    }

    static def setPropertyValue(DefaultGrailsControllerClass controller, String name, Object value){
        controller.metaClass.setProperty(controller.newInstance(), name, value)
    }
}
