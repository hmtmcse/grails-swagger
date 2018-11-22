package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsDomain
import com.hmtmcse.gs.data.GsDomainBelongTo
import com.hmtmcse.gs.data.GsDomainHasMany
import com.hmtmcse.gs.data.GsDomainOneToOne
import com.hmtmcse.gs.data.GsDomainProperty
import com.hmtmcse.swagger.definition.SwaggerConstant
import grails.util.Holders
import org.grails.core.DefaultGrailsControllerClass
import org.grails.datastore.mapping.model.MappingContext
import org.grails.datastore.mapping.model.PersistentEntity
import org.grails.datastore.mapping.model.PersistentProperty
import org.grails.datastore.mapping.model.types.Association


class GsReflectionUtil {

    static def getPropertyValue(DefaultGrailsControllerClass controller, String name) {
        try {
            return controller.metaClass.getProperty(controller.newInstance(), name)
        } catch (Exception e) {
            return null
        }
    }

    static Boolean isExistProperty(Class clazz, String name) {
        try {
            clazz.metaClass.getProperty(clazz.newInstance(), name)
            return true
        } catch (Exception e) {
            return false
        }
    }

    static Boolean isExistMethod(Class clazz, String method) {
        try {
            clazz.metaClass.getMetaMethod(method)
            return true
        } catch (Exception e) {
            return false
        }
    }

    static PersistentEntity getPersistentEntity(Class clazz) {
        MappingContext concreteMappingContext = Holders.grailsApplication.getMappingContext()
        return concreteMappingContext.getPersistentEntity(clazz.name)
    }


    static Map getDomainToSwaggerDataType(Class clazz, Boolean listAssociation = true) {
        Map properties = [:]
        if (clazz == null) {
            return properties
        }
        PersistentEntity persistentEntity = getPersistentEntity(clazz)
        String dataType
        persistentEntity?.getPersistentProperties()?.each { PersistentProperty persistentProperty ->
            dataType = GsConfigHolder.javaToSwaggerDataType.get(persistentProperty.type.name) ?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(persistentProperty.name, dataType)
        }

        PersistentProperty identity = persistentEntity.getIdentity()
        if (identity) {
            dataType = GsConfigHolder.javaToSwaggerDataType.get(identity.type.name) ?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(identity.name, dataType)
        }

        if (listAssociation) {
            for (Association association : persistentEntity.getAssociations()) {
                properties.put(association.name, getDomainToSwaggerDataType(association.associatedEntity.javaClass, false))
            }
        }
        return properties
    }

    private static String entityRelationType(Association association) {
        String erString = association.toString()
        if (erString == null) {
            return ""
        }
        String[] erSplit = erString.split(":")
        if (erString.length() >= 1) {
            return erSplit[0]
        }
        return ""
    }

    static GsDomain getDomainToDomainProperties(Class clazz, Boolean listAssociation = true) {
        GsDomain gsDomain = new GsDomain()
        Map properties = [:]
        if (clazz == null) {
            return gsDomain
        }
        PersistentEntity persistentEntity = getPersistentEntity(clazz)
        String dataType
        persistentEntity?.getPersistentProperties()?.each { PersistentProperty persistentProperty ->
            dataType = GsConfigHolder.javaToSwaggerDataType.get(persistentProperty.type.name) ?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(persistentProperty.name, dataType)
            gsDomain.allProperties.add(new GsDomainProperty(persistentProperty.name, dataType))
        }

        PersistentProperty identity = persistentEntity.getIdentity()
        if (identity) {
            dataType = GsConfigHolder.javaToSwaggerDataType.get(identity.type.name) ?: SwaggerConstant.SWAGGER_DT_OBJECT
            gsDomain.allProperties.add(new GsDomainProperty(identity.name, dataType))
        }


        if (listAssociation) {
            GsDomainHasMany gsDomainHasMany
            GsDomainBelongTo gsDomainBelongTo
            GsDomainOneToOne gsDomainOneToOne
            for (Association association : persistentEntity.getAssociations()) {
                switch (entityRelationType(association)) {
                    case GsConstant.ER_MANY_TO_MANY:
                        gsDomainHasMany = new GsDomainHasMany()
                        gsDomainHasMany.isOwningSide = association.isOwningSide()
                        gsDomainHasMany.name = association.name
                        gsDomainHasMany.allProperties = getDomainToDomainProperties(association.associatedEntity.javaClass, false).allProperties
                        gsDomain.hasMany.add(gsDomainHasMany)
                        break
                    case GsConstant.ER_MANY_TO_ONE:
                        gsDomainBelongTo = new GsDomainBelongTo()
                        gsDomainBelongTo.name = association.name
                        gsDomainBelongTo.allProperties = getDomainToDomainProperties(association.associatedEntity.javaClass, false).allProperties
                        gsDomain.belongsTo.add(gsDomainBelongTo)
                        break
                    case GsConstant.ER_ONE_TO_ONE:
                        gsDomainOneToOne = new GsDomainOneToOne()
                        gsDomainOneToOne.name = association.name
                        gsDomainOneToOne.allProperties = getDomainToDomainProperties(association.associatedEntity.javaClass, false).allProperties
                        gsDomain.oneToOne.add(gsDomainOneToOne)
                        break
                }
            }
        }
        return gsDomain
    }


    private static def castToGSObject(String dataType, def data) {
        switch (dataType) {
            case SwaggerConstant.SWAGGER_DT_BOOLEAN:
                return data?.toBoolean()
            case SwaggerConstant.SWAGGER_DT_INTEGER:
                return data?.toInteger()
            case SwaggerConstant.SWAGGER_DT_INTEGER_64:
                return data?.toLong()
        }
        return data
    }

    static def castFromDomainSwaggerMap(Map params, Map domainFieldsType) {
        domainFieldsType?.each { String name, def dataType ->
            if (params.containsKey(name)) {
                if (dataType instanceof String) {
                    params[name] = castToGSObject(dataType, params[name])
                }
            }
        }
        if (params.containsKey(GsConstant.PROPERTY_NAME) && params.containsKey(GsConstant.PROPERTY_VALUE)) {
            String fieldName = params[GsConstant.PROPERTY_NAME]
            params.put(GsConstant.PROPERTY_VALUE, castToGSObject(domainFieldsType[fieldName], params[GsConstant.PROPERTY_VALUE]))
        }
        return params
    }

    static def getNewObject(DefaultGrailsControllerClass controller) {
        try {
            return controller.newInstance()
        } catch (Exception e) {
            return null
        }
    }

    static def getNewObjectWithServiceInstance(DefaultGrailsControllerClass controller) {
        try {
            Object object = getNewObject(controller)
            if (object) {
                object.metaClass.getProperties().each { MetaProperty metaProperty ->
                    if (metaProperty.name.endsWith("Service")) {
                        Class clazz = Class.forName(metaProperty.type.name)
                        object[metaProperty.name] = clazz.newInstance()
                    }
                }
            }
            return object
        } catch (Exception e) {
            return null
        }
    }

    static def setPropertyValue(DefaultGrailsControllerClass controller, String name, Object value) {
        controller.metaClass.setProperty(controller.newInstance(), name, value)
    }


    static Map getMetaClassToSwaggerDataType(MetaClass metaClass) {
        Map properties = [:]
        String dataType
        List<MetaProperty> allProperties = metaClass?.getProperties()
        allProperties?.reverse()?.each { MetaProperty metaProperty ->
            dataType = GsConfigHolder.javaToSwaggerDataType.get(metaProperty.type.name) ?: SwaggerConstant.SWAGGER_DT_OBJECT
            properties.put(metaProperty.name, dataType)
        }
        if (properties.get("class")) {
            properties.remove("class")
        }
        return properties
    }


}
