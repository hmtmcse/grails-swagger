package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsParamsPairData

class GsRequestValidator {


    public GsInternalResponse validate(GsParamsPairData gsParamsPairData, GsApiActionDefinition gsApiActionDefinition) {
        GsInternalResponse internalResponse = validateAndCastRequestParameter(gsApiActionDefinition.getRequestProperties(), gsParamsPairData.params)
        if (internalResponse.isSuccess) {
            gsParamsPairData.params = internalResponse.response
            internalResponse.gsParamsPairData = gsParamsPairData.initFilteredGrailsParams()
            internalResponse.filteredParams = internalResponse.response
        }
        return internalResponse
    }


    private GsInternalResponse validateAndCastRequestParameter(LinkedHashMap<String, GsApiRequestProperty> requestProperties, Map params) {

        GsInternalResponse gsInternalResponse = new GsInternalResponse()
        gsInternalResponse.response = [:]


        if (!requestProperties || !params) {
            return gsInternalResponse
        }


        def param
        GsInternalResponse nestedGsInternalResponse
        gsInternalResponse.isSuccess = true
        requestProperties.each { String fieldName, GsApiRequestProperty requestProperty ->

            param = params.get(fieldName)
            if (requestProperty.isRequired && param == null) {
                gsInternalResponse.addRequestValidationError(fieldName, GsConfigHolder.requiredFieldMissing())
                gsInternalResponse.isSuccess = false
                return
            }


            if (requestProperty.relationalEntity) {
                nestedGsInternalResponse = validateAndCastRequestParameter(requestProperty.relationalEntity.requestProperties, params[fieldName])
                if (nestedGsInternalResponse.isSuccess) {
                    gsInternalResponse.response[fieldName] = nestedGsInternalResponse.response
                } else {
                    gsInternalResponse.isSuccess = nestedGsInternalResponse.isSuccess
                    Map error = [:]
                    error.put("fieldName", fieldName)
                    error.put("message", "")
                    error.put("errors", gsInternalResponse.errorDetails)
                    gsInternalResponse.errorDetails.add(error)
                }
                return
            }


            if (param == null && requestProperty.defaultValue != null) {
                param = requestProperty.defaultValue
            }

            if (param != null && requestProperty.isTypeCast) {
                param = GsReflectionUtil.castToGSObject(requestProperty.dataType, param)
            }

            if (param == null) {
                gsInternalResponse.addRequestValidationError(fieldName, GsConfigHolder.requiredFieldMissing())
                gsInternalResponse.isSuccess = false
                return
            }
            gsInternalResponse.response[fieldName] = param
        }

        return gsInternalResponse
    }


    public static GsRequestValidator instance() {
        return new GsRequestValidator()
    }
}
