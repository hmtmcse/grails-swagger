package com.hmtmcse.gs.model

import com.hmtmcse.gs.data.GsApiResponseProperty

interface CustomParamProcessor {
    public Object process(String fieldName, Object domainRow, GsApiResponseProperty propertyDefinition)
}