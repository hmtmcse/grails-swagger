package com.hmtmcse.gs.system

import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.GsUrlMappingUtil
import com.hmtmcse.gs.GsUtil


class GsApiInterceptor {

    GsApiInterceptor(){
        match(controller: ~/^${GsUrlMappingUtil.apiPrefix()}.*/)
    }

    boolean before() {
        GsUtil.setJsonToParams(request, params)
        return true
    }

}
