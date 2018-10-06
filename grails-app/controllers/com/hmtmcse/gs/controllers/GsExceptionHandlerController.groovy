package com.hmtmcse.gs.controllers

import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.GsRestProcessor
import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON

class GsExceptionHandlerController extends GsRestProcessor {

    def e404() { }

    def e500() { }


    def invalid() {
        render(GsApiResponseData.failed(GsConfigHolder.invalidRequest()).toMap() as JSON)
    }

    def invalidRequestType() {
        render(GsApiResponseData.failed(GsConfigHolder.invalidRequestType()).toMap() as JSON)
    }

}
