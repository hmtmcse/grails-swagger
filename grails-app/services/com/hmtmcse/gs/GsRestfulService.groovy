package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseData


class GsRestfulService {

    GsApiResponseData gsRead(GsApiActionDefinition definition){
        return GsApiResponseData.failed("Failed")
    }

    def gsReadList(GsApiActionDefinition definition){}

    def gsCreate(GsApiActionDefinition definition){}

    def gsUpdate(GsApiActionDefinition definition){}

    def gsDelete(GsApiActionDefinition definition){}



}
