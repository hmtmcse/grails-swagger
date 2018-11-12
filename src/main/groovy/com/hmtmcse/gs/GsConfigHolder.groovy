package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsUrlMappingConfig
import com.hmtmcse.swagger.definition.SwaggerConstant


class GsConfigHolder {

    public static Map javaToSwaggerDataType = [
            "java.lang.Integer" : SwaggerConstant.SWAGGER_DT_INTEGER,
            "java.lang.Long" : SwaggerConstant.SWAGGER_DT_INTEGER_64,
            "java.lang.String" : SwaggerConstant.SWAGGER_DT_STRING,
            "java.util.Date" : SwaggerConstant.SWAGGER_DT_STRING_DATE,
            "java.lang.Double" : SwaggerConstant.SWAGGER_DT_NUMBER,
            "java.lang.Float" : SwaggerConstant.SWAGGER_DT_NUMBER,
            "java.lang.Boolean" : SwaggerConstant.SWAGGER_DT_BOOLEAN,
            "array" : SwaggerConstant.SWAGGER_DT_ARRAY,
            "object" : SwaggerConstant.SWAGGER_DT_OBJECT,
    ]

    public static GsApiResponseData defaultFailedResponse = GsApiResponseData.failedWithDetails("Operation Failed", [])
    public static GsApiResponseData defaultSuccessResponse = GsApiResponseData.successMessage("Operation Success")

    public static Integer itemsPerPage() {
        return 20
    }

    public static String sortColumn() {
        return "id"
    }

    public static String sortOrder() {
        return "desc"
    }

    public static String failedMessage() {
        return "Unable to Process Request"
    }

    static String controllerStartWith() {
        return "Api"
    }

    public static String getUID(){
        return UUID.randomUUID().toString().toUpperCase();
    }

    static String invalidRequest() {
        return "Invalid API Request"
    }

    static String invalidRequestType() {
        return "Invalid API Request Type"
    }

    static String multipleMatchInDetails() {
        return "If Multiple Match Found then will return first one"
    }

    static String responseKey() {
        return "response"
    }

    static String requiredFieldMissing() {
        return "Required Field Missing"
    }

    static String invalidFieldData() {
        return "Invalid/Missing Field Or Data"
    }

    static String requestedConditionEmpty() {
        return "Requested Condition Empty"
    }

    static GsUrlMappingConfig getUrlMappingCongit() {
        return new GsUrlMappingConfig()
    }

    static String controllerCustomUrlRegex() {
        return "(site|admin)([A-Z]\\w+)"
    }
}
