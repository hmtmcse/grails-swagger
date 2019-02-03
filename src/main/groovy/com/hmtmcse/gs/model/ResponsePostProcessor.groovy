package com.hmtmcse.gs.model

import com.hmtmcse.gs.GsApiActionDefinition
import com.hmtmcse.gs.data.GsResponsePostData

interface ResponsePostProcessor {
    public GsResponsePostData process(GsApiActionDefinition definition, GsResponsePostData gsResponsePostData)
}