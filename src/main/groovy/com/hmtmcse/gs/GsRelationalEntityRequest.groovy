package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsDomain

class GsRelationalEntityRequest extends GsApiRequestProperty implements GsRequestOrganizer<GsRelationalEntityRequest> {

    public LinkedHashMap<String, GsApiRequestProperty> requestProperties = new LinkedHashMap<>()
    public GsDomain gsDomain = new GsDomain()


    @Override
    LinkedHashMap<String, GsApiRequestProperty> getRequestProperties() {
        return this.requestProperties
    }


    @Override
    LinkedHashMap<String, GsApiRequestProperty> setRequestProperties(LinkedHashMap<String, GsApiRequestProperty> requestPropertyLinkedHashMap) {
        return this.requestProperties = requestPropertyLinkedHashMap
    }

    @Override
    GsDomain getGsDomain() {
        return this.gsDomain
    }
}
