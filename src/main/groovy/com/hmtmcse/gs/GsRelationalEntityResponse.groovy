package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsDomain

class GsRelationalEntityResponse extends GsApiResponseProperty implements GsResponseOrganizer<GsRelationalEntityResponse> {

    public LinkedHashMap<String, GsApiResponseProperty> responseProperties = new LinkedHashMap<>()
    public GsDomain gsDomain = new GsDomain()

    @Override
    LinkedHashMap<String, GsApiResponseProperty> getResponseProperties() {
        return this.responseProperties
    }

    @Override
    GsDomain getGsDomain() {
        return this.gsDomain
    }

    @Override
    LinkedHashMap<String, GsApiResponseProperty> setResponseProperties(LinkedHashMap<String, GsApiResponseProperty> responsePropertyLinkedHashMap) {
        return this.responseProperties = responsePropertyLinkedHashMap
    }
}
