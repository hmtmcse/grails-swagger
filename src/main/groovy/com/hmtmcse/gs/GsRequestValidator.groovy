package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.model.CustomRequestParamProcessor
import com.hmtmcse.swagger.definition.SwaggerConstant
import org.grails.web.json.JSONObject

class GsRequestValidator {


    public GsInternalResponse validate(GsParamsPairData gsParamsPairData, GsApiActionDefinition gsApiActionDefinition) {
        GsInternalResponse internalResponse = validateAndCastRequestParameter(gsApiActionDefinition.getRequestProperties(), gsParamsPairData.params, gsParamsPairData)
        if (internalResponse.isSuccess) {
            gsParamsPairData.params = internalResponse.response
            internalResponse.gsParamsPairData = gsParamsPairData.initFilteredGrailsParams()
        }
        return internalResponse
    }


    private GsInternalResponse validateAndCastRequestParameter(LinkedHashMap<String, GsApiRequestProperty> requestProperties, Map params, GsParamsPairData gsParamsPairData) {

        GsInternalResponse gsInternalResponse = new GsInternalResponse()
        gsInternalResponse.response = [:]


        if (!requestProperties) {
            return gsInternalResponse.setIsSuccess(true)
        }


        def param
        String requestedKey
        GsInternalResponse nestedGsInternalResponse
        gsInternalResponse.isSuccess = true
        requestProperties.each { String fieldName, GsApiRequestProperty requestProperty ->

            param = GsUtil.getMapValue(requestProperty.getRequestedKey(), params)
            requestedKey = requestProperty.getRequestedKey()
            if (requestProperty.isRequired && param == null) {
                gsInternalResponse.addRequestValidationError(requestedKey, (requestProperty.errorMessage != null ? requestProperty.errorMessage : GsConfigHolder.requiredFieldMissing()))
                gsInternalResponse.isSuccess = false
                return
            }


            if (requestProperty.relationalEntity) {
                nestedGsInternalResponse = validateAndCastRequestParameter(requestProperty.relationalEntity.requestProperties, params[requestedKey], gsParamsPairData)
                if (nestedGsInternalResponse.isSuccess) {
                    gsInternalResponse.response[fieldName] = nestedGsInternalResponse.response
                } else {
                    gsInternalResponse.isSuccess = nestedGsInternalResponse.isSuccess
                    Map error = [:]
                    error.put("fieldName", requestedKey)
                    error.put("message", "")
                    error.put("errors", gsInternalResponse.errorDetails)
                    gsInternalResponse.errorDetails.add(error)
                }
                return
            }


            if (requestProperty.customRequestParamProcessor != null && requestProperty.customRequestParamProcessor instanceof CustomRequestParamProcessor) {
                try {
                    gsInternalResponse.response[fieldName] = requestProperty.customRequestParamProcessor.process(requestedKey, gsParamsPairData, requestProperty)
                    return
                } catch (GsRequestParamException e) {
                    gsInternalResponse.addRequestValidationError(requestedKey, e.getMessage())
                    gsInternalResponse.isSuccess = false
                    return
                }
            }


            if ((param == null || (param instanceof JSONObject && param.size() == 0)) && requestProperty.defaultValue != GsConstant.NOT_DEFAULT_VALUE) {
                param = requestProperty.defaultValue
            }

            if (param != null && requestProperty.dataType && requestProperty.dataType.startsWith("ARRAY_") && !(param instanceof List)){
                gsInternalResponse.addRequestValidationError(requestedKey, (requestProperty.errorMessage != null ? requestProperty.errorMessage : GsConfigHolder.invalidList()))
                gsInternalResponse.isSuccess = false
                return
            }

            if (param != null && requestProperty.isTypeCast) {
                param = GsReflectionUtil.castToGSObject(requestProperty.dataType, param, requestProperty)
            }

            gsInternalResponse.response[fieldName] = param
        }
        return gsInternalResponse
    }


    public static GsRequestValidator instance() {
        return new GsRequestValidator()
    }
}
