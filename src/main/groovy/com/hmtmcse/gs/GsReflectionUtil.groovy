package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsControllerActionData
import com.hmtmcse.swagger.definition.SwaggerConstant
import grails.util.Holders
import org.grails.core.DefaultGrailsControllerClass
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty

import java.lang.reflect.InvocationTargetException

class GsReflectionUtil {

    static def getPropertyValue(DefaultGrailsControllerClass controller, String name){
        try {
            return controller.metaClass.getProperty(controller.newInstance(), name)
        }catch(Exception e){
            return null
        }
    }

    static def isExistProperty(Class clazz, String name){
        try {
            clazz.metaClass.getProperty(clazz.newInstance(), name)
            return true
        }catch(Exception e){
            return false
        }
    }

    static PersistentEntity getPersistentEntity(Class clazz){
        MappingContext concreteMappingContext = Holders.grailsApplication.getMappingContext()
        return concreteMappingContext.getPersistentEntity(clazz.name)
    }


    static Map getDomainToSwaggerDataType(Class clazz){
        Map properties = [:]
        PersistentEntity persistentEntity = getPersistentEntity(clazz)
        String dataType
        persistentEntity?.getPersistentProperties()?.each { PersistentProperty persistentProperty ->
            dataType = GsConfigHolder.javaToSwaggerDataType.get(persistentProperty.type.name)?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(persistentProperty.name, dataType)
        }
        PersistentProperty identity = persistentEntity.getIdentity()
        if (identity){
            dataType = GsConfigHolder.javaToSwaggerDataType.get(identity.type.name)?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(identity.name, dataType)
        }
        return properties
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

    static GsApiActionDefinition apiActionDefinition(GsControllerActionData controllerActionData) {
        def controllerObj = getNewObject(controllerActionData.controllerClass)
        GsApiActionDefinition gsApiActionDefinition = null
        if (controllerObj) {
            controllerObj.metaClass.render = {null}
            controllerObj.returnFor = GsConstant.RETURN_FOR_DEFINITION
            controllerActionData.actions.each { GsAction gsAction ->
                try {
                    gsApiActionDefinition = controllerObj."$gsAction.actionRealName"()
                } catch (InvocationTargetException e) {
                    println(e.getMessage())
                } catch (NullPointerException e) {
                    println(e.getMessage())
                } catch (Exception e) {
                    println(e.getMessage())
                }
            }

        }
        return gsApiActionDefinition
    }


}
