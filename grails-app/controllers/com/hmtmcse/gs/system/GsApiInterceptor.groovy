package com.hmtmcse.gs.system

import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.GsUrlMappingUtil
import com.hmtmcse.gs.GsUtil
import com.hmtmcse.gs.data.GsApiResponseData
import grails.converters.JSON


class GsApiInterceptor {

    GsApiInterceptor(){
        match(controller: ~/^${GsUrlMappingUtil.apiPrefix()}.*/)
    }

    boolean before() {
        if (!GsUtil.setJsonToParams(request, params)){
            render(GsApiResponseData.failed(GsConfigHolder.failedMessage()).toMap() as JSON)
            return false
        }
        return true
    }

}
