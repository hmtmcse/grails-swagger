package com.hmtmcse.gs.model

import com.hmtmcse.gs.data.GsApiResponseProperty

interface CustomResponseParamProcessor {
    public Object process(String fieldName, Object domainRow, GsApiResponseProperty propertyDefinition)
}