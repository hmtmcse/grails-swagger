package com.hmtmcse.gs.model

import com.hmtmcse.gs.GsRequestParamException
import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsParamsPairData

interface CustomRequestParamProcessor {
    public Object process(String fieldName, GsParamsPairData gsParamsPairData, GsApiRequestProperty propertyDefinition) throws GsRequestParamException
}