package com.hmtmcse.gs

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


    static GsInternalResponse instance(){
        return new GsInternalResponse()
    }

     GsInternalResponse params(String key, Object value){
         filteredParams.put(key, value)
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

    GsInternalResponse processDomainError(List errors){
        errors?.each { FieldError fieldError ->
            addErrorDetail(fieldError.field, GsConfigHolder.invalidFieldData())
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
