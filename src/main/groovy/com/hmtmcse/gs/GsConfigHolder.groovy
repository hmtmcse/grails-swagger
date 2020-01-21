package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import com.hmtmcse.gs.data.GsUrlMappingConfig
import com.hmtmcse.swagger.definition.SwaggerConstant


class GsConfigHolder {


    public static String hostnameWithPort = "localhost:8080"
    public static String systemVersion = "V1"
    public static String swaggerDefinitionUrl = "http://localhost:8080/swaggerUi/definition"


    public static String JWT_HMAC_SECRET = "Th1sIsjWtTokenSecRet123@"

    public static Integer defaultItemsPerPage = 20
    public static String defaultSortColumn = "id"
    public static String defaultSortOrder = "desc"
    public static String defaultResponseKey = "response"


    public static String controllerStartWithDefault = "Api"
    public static String controllerCustomUrlRegexDefault = "(site|admin)([A-Z]\\w+)"

    public static String defaultFailedMessage = "Unable to Process Request"
    public static String unableToCastValue = "Unable to Type Cast Value"
    public static String unableToParseJSON = "Unable To Parse JSON"
    public static String invalidRequestDefaultMessage = "Invalid API Request"
    public static String invalidRequestTypeDefaultMessage = "Invalid API Request Type"
    public static String multipleMatchInDetailsDefaultMessage = "If Multiple Match Found then will return first one"
    public static String requiredFieldMissingDefaultMessage = "Required Field Missing"
    public static String invalidFieldDataDefaultMessage = "Invalid/Missing Field Or Data"
    public static String requestedConditionEmptyDefaultMessage = "Requested Condition Empty"
    public static String unAuthorizeFieldOrCondition = "You are Request for Unauthorized %s Name %s"
    public static String invalidListParams = "Invalid List"
    public static String defaultDateFormat = "dd-MM-yyyy"



    public static Map javaToSwaggerDataType = [
            "java.lang.Integer" : SwaggerConstant.SWAGGER_DT_INTEGER,
            "java.lang.Long" : SwaggerConstant.SWAGGER_DT_LONG,
            "java.lang.String" : SwaggerConstant.SWAGGER_DT_STRING,
            "java.util.Date" : SwaggerConstant.SWAGGER_DT_STRING_DATE,
            "java.lang.Double" : SwaggerConstant.SWAGGER_DT_DOUBLE,
            "java.lang.Float" : SwaggerConstant.SWAGGER_DT_FLOAT,
            "java.lang.Boolean" : SwaggerConstant.SWAGGER_DT_BOOLEAN,
            "array" : SwaggerConstant.SWAGGER_DT_ARRAY,
            "object" : SwaggerConstant.SWAGGER_DT_OBJECT,
    ]

    public static GsApiResponseData defaultFailedResponse = GsApiResponseData.failedWithDetails("Operation Failed", [])
    public static GsApiResponseData defaultSuccessResponse = GsApiResponseData.successMessage("Operation Success")

    public static Integer itemsPerPage() {
        return defaultItemsPerPage
    }

    public static String sortColumn() {
        return defaultSortColumn
    }

    public static String sortOrder() {
        return defaultSortOrder
    }

    public static String failedMessage() {
        return defaultFailedMessage
    }

    static String controllerStartWith() {
        return controllerStartWithDefault
    }

    static String defaultDateParseFormat() {
        return defaultDateFormat
    }

    public static String getUID(){
        return UUID.randomUUID().toString().toUpperCase();
    }

    static String invalidRequest() {
        return invalidRequestDefaultMessage
    }

    static String invalidRequestType() {
        return invalidRequestTypeDefaultMessage
    }

    static String multipleMatchInDetails() {
        return multipleMatchInDetailsDefaultMessage
    }

    static String responseKey() {
        return defaultResponseKey
    }

    static String requiredFieldMissing() {
        return requiredFieldMissingDefaultMessage
    }

    static String invalidFieldData() {
        return invalidFieldDataDefaultMessage
    }

    static String requestedConditionEmpty() {
        return requestedConditionEmptyDefaultMessage
    }

    static GsUrlMappingConfig getUrlMappingCongit() {
        return new GsUrlMappingConfig()
    }

    static String controllerCustomUrlRegex() {
        return controllerCustomUrlRegexDefault
    }

    static String unauthorizedFieldOrCondition(String name, String type = "field") {
        return String.format(unAuthorizeFieldOrCondition, type, name)
    }

    static String invalidList() {
        return invalidListParams
    }

}
