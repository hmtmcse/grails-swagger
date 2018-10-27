package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsAction
import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsControllerActionData
import com.hmtmcse.gs.data.GsRequestResponseProperty
import com.hmtmcse.swagger.definition.*

import java.lang.reflect.InvocationTargetException

class SwaggerUIGeneratorService {

    private SwaggerDefinition swaggerDefinition = new SwaggerDefinition()
    private String failedResponseName = "FailedResponse"

    def generate() {
        try{
            startSwagger("localhost:8080", GsUrlMappingUtil.apiPrefix())
            GsUrlMappingUtil.getUrlMappingData().each { GsControllerActionData controllerActionData ->
                swaggerJsonByControllerData(controllerActionData)
                addDefaultFailedResponse()
            }
        }catch(Exception e){
           println(e.getMessage())
        }
        return swaggerDefinition.getDefinition()
    }

    void addDefaultFailedResponse(){
        SwaggerProperty swaggerProperty = GsApiResponseData.swaggerResponseProperty(GsConfigHolder.defaultFailedResponse)
        if (swaggerProperty){
            addToDefinition(failedResponseName, SwaggerConstant.SWAGGER_DT_OBJECT, swaggerProperty)
        }else{
            failedResponseName = null
        }
    }




    void swaggerJsonByControllerData(GsControllerActionData controllerActionData){
        String tagName = ""
        String description = ""
        def controllerObj = GsReflectionUtil.getNewObject(controllerActionData.controllerClass)
        if (controllerObj){
            controllerObj?.swaggerInit()
            tagName = controllerObj?.tagName ?: GsUtil.makeHumReadble(controllerActionData.controllerUrlName)
            description = controllerObj?.tagDescription
        }
        processApiActionDefinition(controllerActionData, tagName)
        swaggerDefinition.addTag(tagName, description)
    }


    void processApiActionDefinition(GsControllerActionData controllerActionData, String tagName) {
        def controllerObj = GsReflectionUtil.getNewObjectWithServiceInstance(controllerActionData.controllerClass)
        GsApiActionDefinition gsApiActionDefinition = null
        String url = "/${controllerActionData.apiVersion}/${controllerActionData.url}/"
        String actionUrl
        if (controllerObj) {
            controllerObj.metaClass.render = {null}
            controllerObj.returnFor = GsConstant.RETURN_FOR_DEFINITION
            SwaggerPath swaggerPath
            SwaggerProperty swaggerProperty
            SwaggerPathResponse response
            controllerActionData.actions.each { GsAction gsAction ->
                actionUrl = null
                try {
                    gsApiActionDefinition = controllerObj."$gsAction.actionRealName"()
                    if (gsApiActionDefinition){
                        gsApiActionDefinition.setModelDefinition(controllerActionData.apiVersion, controllerActionData.controllerUrlName, gsAction)
                        swaggerPath = pathGenerator(gsAction, gsApiActionDefinition )
                        swaggerPath.addTag(tagName)
                        actionUrl = "${url}${gsAction.name}"
                        swaggerDefinition.startPaths(actionUrl).addPath(gsAction.httpMethod, swaggerPath)
                        response = responseGenerator(gsAction, gsApiActionDefinition)
                        swaggerPath.addResponse(response)
                    }
                } catch (InvocationTargetException e) {
                    println(e.getMessage())
                } catch (NullPointerException e) {
                    println(e.getMessage())
                } catch (Exception e) {
                    println(e.getMessage())
                }
            }
        }
    }




    SwaggerPathResponse responseGenerator(GsAction gsAction, GsApiActionDefinition gsApiActionDefinition){
        SwaggerPathResponse response = new SwaggerPathResponse()

        String successResponseDefinition = "${SwaggerConstant.SUCCESS_RESPONSE}${gsApiActionDefinition.modelDefinition}"
        if (gsApiActionDefinition.successResponseFormat.response){
            SwaggerProperty swaggerProperty = propertiesProcessor(gsApiActionDefinition.getResponseProperties(), null, gsApiActionDefinition.domainFields())
            SwaggerProperty successResponse = GsApiResponseData.swaggerResponseProperty(gsApiActionDefinition.successResponseFormat)
            successResponse.objectProperty(GsConfigHolder.responseKey(), swaggerProperty)
            addToDefinition(successResponseDefinition, SwaggerConstant.SWAGGER_DT_OBJECT, successResponse)
            response.start(SwaggerConstant.SUCCESS_RESPONSE)
            response.description(GsUtil.makeHumReadble(SwaggerConstant.SUCCESS_RESPONSE)).schemaOnly(successResponseDefinition)
        }else{
            SwaggerProperty successResponse = GsApiResponseData.swaggerResponseProperty(gsApiActionDefinition.successResponseFormat)
            addToDefinition(successResponseDefinition, SwaggerConstant.SWAGGER_DT_OBJECT, successResponse)
            response.start(SwaggerConstant.SUCCESS_RESPONSE)
            response.description(GsUtil.makeHumReadble(SwaggerConstant.SUCCESS_RESPONSE)).schemaOnly(successResponseDefinition)
        }


        String failedResponseDefinition = "${SwaggerConstant.FAILED_RESPONSE}${gsApiActionDefinition.modelDefinition}"
        if (failedResponseName && gsApiActionDefinition.failedResponseFormat == null){
            response.start(SwaggerConstant.DEFAULT_FAILED_RESPONSE)
            response.description(GsUtil.makeHumReadble(SwaggerConstant.DEFAULT_FAILED_RESPONSE)).schemaOnly(failedResponseName)
        }

        return response
    }


    SwaggerPath pathGenerator(GsAction gsAction, GsApiActionDefinition gsApiActionDefinition) {

        SwaggerPath swaggerPath = swaggerDefinition.path()
        String message = "Please Use Http GET Request for "
        SwaggerPathParameter parameter = null
        SwaggerProperty swaggerProperty = null
        String requestDefinition = "${SwaggerConstant.REQUEST}${gsApiActionDefinition.modelDefinition}"
        if (gsAction.httpMethod && gsAction.httpMethod.equals(GsConstant.GET)) {
            switch (gsApiActionDefinition.responseType){
                case GsConstant.LIST_RESPONSE:
                    swaggerPath.parameters(GsDataFilterHandler.swaggerGetReadRequest(true, gsApiActionDefinition.whereAllowedPropertyList).getPropertyList())
                    break
                case GsConstant.DELETE_RESPONSE:
                case GsConstant.DETAILS_RESPONSE:
                    swaggerPath.parameters(GsDataFilterHandler.swaggerGetReadRequest(false, gsApiActionDefinition.whereAllowedPropertyList).getPropertyList())
                    break
            }
        } else if (gsAction.httpMethod && gsAction.httpMethod.equals(GsConstant.POST)) {
            message = "Please Use Http POST Request for "
            parameter = swaggerDefinition.pathParameter(SwaggerConstant.IN_BODY, gsApiActionDefinition.parameterName ?: SwaggerConstant.IN_BODY)
            parameter.required().schema(requestDefinition)

            switch (gsApiActionDefinition.responseType){
                case GsConstant.LIST_RESPONSE:
                    addToDefinition(requestDefinition, SwaggerConstant.SWAGGER_DT_OBJECT, GsDataFilterHandler.swaggerPostReadRequest(true, gsApiActionDefinition.whereAllowedPropertyList))
                    break
                case GsConstant.CREATE_RESPONSE:
                    swaggerProperty = propertiesProcessor(gsApiActionDefinition.getRequestProperties(), null, gsApiActionDefinition.domainFields())
                    swaggerDefinition.addDefinition(requestDefinition, SwaggerConstant.SWAGGER_DT_OBJECT).addProperties(swaggerProperty)
                    break
                case GsConstant.DELETE_RESPONSE:
                case GsConstant.DETAILS_RESPONSE:
                    addToDefinition(requestDefinition, SwaggerConstant.SWAGGER_DT_OBJECT, GsDataFilterHandler.swaggerPostReadRequest(false, gsApiActionDefinition.whereAllowedPropertyList))
                    break
                case GsConstant.UPDATE_RESPONSE:
                    swaggerProperty = propertiesProcessor(gsApiActionDefinition.getRequestProperties(), null, gsApiActionDefinition.domainFields())
                    swaggerProperty.addFromExistingObjectProperty(GsConstant.WHERE, GsDataFilterHandler.swaggerPostReadRequest(false, gsApiActionDefinition.whereAllowedPropertyList))
                    swaggerDefinition.addDefinition(requestDefinition, SwaggerConstant.SWAGGER_DT_OBJECT).addProperties(swaggerProperty)
                    break
            }

            swaggerPath.addConsumeType(SwaggerConstant.APPLICATION_JSON)
            if (parameter){
                swaggerPath.addParameter(parameter)
            }
        } else if (gsAction.httpMethod && gsAction.httpMethod.equals(GsConstant.DELETE)) {
            message = "Please Use Http DELETE Request for "
            parameter = swaggerDefinition.pathParameter(SwaggerConstant.IN_BODY, gsApiActionDefinition.parameterName ?: SwaggerConstant.IN_BODY)
            parameter.required().schema(requestDefinition)

            swaggerProperty = propertiesProcessor(gsApiActionDefinition.getRequestProperties(), null, gsApiActionDefinition.domainFields())
            swaggerProperty.addFromExistingObjectProperty(GsConstant.WHERE, GsDataFilterHandler.swaggerPostReadRequest(false, gsApiActionDefinition.whereAllowedPropertyList))
            swaggerDefinition.addDefinition(requestDefinition, SwaggerConstant.SWAGGER_DT_OBJECT).addProperties(swaggerProperty)

            swaggerPath.addConsumeType(SwaggerConstant.APPLICATION_JSON)
            swaggerPath.addParameter(parameter)


        } else if (gsAction.httpMethod && gsAction.httpMethod.equals(GsConstant.PUT)) {
            message = "Please Use Http PUT Request for "
        } else {
            message = "Please Use Http Request for "
        }




        if (!gsApiActionDefinition.summary) {
            gsApiActionDefinition.summary = GsUtil.makeHumReadble(message + gsAction.name)
        }
        swaggerPath.summary(gsApiActionDefinition.summary)

        if (gsApiActionDefinition.description) {
            swaggerPath.description(gsApiActionDefinition.description)
        }

        return swaggerPath

    }


    SwaggerProperty propertiesProcessor(Map<String, GsRequestResponseProperty> responsePropertyMap, String inType, Map domainFields) {
        SwaggerProperty swaggerProperty = new SwaggerProperty()
        responsePropertyMap.each { String name, GsRequestResponseProperty field ->

            if (field.referenceDefinition) {

            } else {
                if (field.dataType == null || field.dataType.equals("")) {
                    field.dataType = SwaggerConstant.SWAGGER_DT_STRING
                    if (domainFields.get(field.name)) {
                        field.dataType = domainFields.get(field.name)
                    }
                }
                swaggerProperty.property(field.name, field.dataType)
                if (field.format) {
                    swaggerProperty.format(field.format)
                }
            }


            if (field.example) {
                swaggerProperty.example(field.example)
            }
            if (field.description) {
                swaggerProperty.description(field.description)
            }
            if (inType) {
                swaggerProperty.in(inType)
            }
            swaggerProperty.addToList()
        }
        return swaggerProperty
    }



    void startSwagger(String host, String basePath){
        swaggerDefinition = new SwaggerDefinition()
        swaggerDefinition.host(host)
        swaggerDefinition.basePath("/" + basePath)
        swaggerDefinition.scheme("http")
    }



    void addToDefinition(String name, String type, SwaggerProperty swaggerProperty){
        swaggerDefinition.addDefinition(name, type).addProperties(swaggerProperty)
    }
}
