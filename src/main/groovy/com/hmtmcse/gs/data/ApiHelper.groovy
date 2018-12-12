package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsRestfulService

class ApiHelper {

    GsRestfulService help
    GsApiResponseData response = new GsApiResponseData()
    GsFilteredData gsFilteredData

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

    GsFilteredData getGsFilteredData() {
        return gsFilteredData
    }

    void setGsFilteredData(GsFilteredData gsFilteredData) {
        this.gsFilteredData = gsFilteredData
    }
}
