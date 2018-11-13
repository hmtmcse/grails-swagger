package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsRestfulService

class ApiHelper {

    GsRestfulService help
    GsApiResponseData response = new GsApiResponseData()

    GsRestfulService getHelp() {
        return help
    }

    void setHelp(GsRestfulService help) {
        this.help = help
    }

    GsApiResponseData getResponse() {
        return response
    }

    void setResponse(GsApiResponseData response) {
        this.response = response
    }
}
