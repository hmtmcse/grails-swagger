package com.hmtmcse.gs.model

import com.hmtmcse.gs.GsApiActionDefinition
import com.hmtmcse.gs.data.GsApiResponseData


interface CustomProcessor {
    public GsApiResponseData process(GsApiActionDefinition actionDefinition, Map params);
}