package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsFilteredData
import com.hmtmcse.gs.data.GsParamsPairData
import org.springframework.validation.FieldError

class GsInternalResponse {

    String message = null
    List errorDetails = []
    Map filteredParams = [:]
    Integer code = null
    Closure where = null
    Boolean isSuccess = false
    Object domain = null
    Object response = null
    Integer total = null
    Integer count = null
    Boolean isExist = null
    String tempData = null
    GsParamsPairData gsParamsPairData = null
    GsFilteredData gsFilteredData = null


    void copyFilterAndPagination(){
        if (gsFilteredData && gsParamsPairData && gsParamsPairData.filteredGrailsParameterMap){
            gsParamsPairData.filteredGrailsParameterMap.offset =  gsFilteredData.offset
            gsParamsPairData.params.offset =  gsFilteredData.offset

            gsParamsPairData.filteredGrailsParameterMap.max =  gsFilteredData.max
            gsParamsPairData.params.max =  gsFilteredData.max

            gsParamsPairData.filteredGrailsParameterMap.orderProperty =  gsFilteredData.orderProperty
            gsParamsPairData.params.orderProperty =  gsFilteredData.orderProperty

            gsParamsPairData.filteredGrailsParameterMap.order =  gsFilteredData.order
            gsParamsPairData.params.order =  gsFilteredData.order

            gsParamsPairData.filteredGrailsParameterMap.propertyName =  gsFilteredData.propertyName
            gsParamsPairData.params.propertyName =  gsFilteredData.propertyName

            gsParamsPairData.filteredGrailsParameterMap.propertyValue =  gsFilteredData.propertyValue
            gsParamsPairData.params.propertyValue =  gsFilteredData.propertyValue
        }
    }

    static GsInternalResponse instance(){
        return new GsInternalResponse()
    }

     GsInternalResponse params(String key, Object value){
         filteredParams.put(key, value)
        return this
    }

    GsInternalResponse setGsParamsPairData(GsParamsPairData gsParamsPairData) {
        this.gsParamsPairData = gsParamsPairData
        return this
    }

    GsInternalResponse setGsFilteredData(GsFilteredData gsFilteredData) {
        this.gsFilteredData = gsFilteredData
        return this
    }

    GsInternalResponse addErrorDetail(String fieldName, String message){
        Map error = [:]
        error.put("fieldName", fieldName)
        error.put("message", message)
        this.message = message
        errorDetails.add(error)
        return this
    }


    GsInternalResponse addRequestValidationError(String fieldName, String message){
        if (tempData && !tempData.equals("")){
            fieldName = tempData + "." + fieldName
        }
        return addErrorDetail(fieldName, message)
    }

    private getErrorMessage(LinkedHashMap<String, GsApiRequestProperty> requestMap, String fieldName){
        if (requestMap && requestMap.get(fieldName)){
           return requestMap.get(fieldName).errorMessage ?: GsConfigHolder.invalidFieldData()
        }
        return GsConfigHolder.invalidFieldData()
    }

    GsInternalResponse processDomainError(List errors, LinkedHashMap<String, GsApiRequestProperty> requestMap = null){
        errors?.each { FieldError fieldError ->
            addErrorDetail(fieldError.field, getErrorMessage(requestMap, fieldError.field))
        }
        return this
    }

    Boolean getIsSuccess() {
        return isSuccess
    }

    GsInternalResponse setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess
        return this
    }

    GsInternalResponse processNonUniqueResult(List errors){
        errors?.each { FieldError fieldError ->
            addErrorDetail(fieldError.field, GsConfigHolder.invalidFieldData())
        }
        return this
    }
}
