package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.model.CustomResponseParamProcessor


class GsCommonHelpService {

    GsApiActionDefinition responseDateFormat(GsApiActionDefinition gsApiActionDefinition, String field, String format = "yyyy-MM-dd") {
        gsApiActionDefinition.addResponseProperty(field).customResponseParamProcessor = new CustomResponseParamProcessor() {
            @Override
            Object process(String fieldName, Object domainRow, GsApiResponseProperty propertyDefinition) {
                Date date = domainRow[fieldName]
                return date.format(format)
            }
        }
        return gsApiActionDefinition
    }
}
