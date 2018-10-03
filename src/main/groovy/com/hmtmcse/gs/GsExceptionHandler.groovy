package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON

trait GsExceptionHandler {

    def handleGsException(GsException e) {
        render(GsApiResponseData.failed(e.getMessage()).toMap() as JSON)
    }

}
