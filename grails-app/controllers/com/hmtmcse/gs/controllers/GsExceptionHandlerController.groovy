package com.hmtmcse.gs.controllers

import com.hmtmcse.gs.GsConfigService
import com.hmtmcse.gs.GsRestProcessor
import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON

class GsExceptionHandlerController extends GsRestProcessor {


    def e404() { }

    def e500() { }

    def invalid() {
        render(GsApiResponseData.failed(GsConfigService.invalidRequest()).toMap() as JSON)
    }


}
