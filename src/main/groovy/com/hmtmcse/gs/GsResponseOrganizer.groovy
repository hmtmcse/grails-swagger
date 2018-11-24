package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty

trait GsResponseOrganizer<T> {

    public Map<String, GsApiResponseProperty> responseProperties = new LinkedHashMap<>()

    public void includeInResponse(){}

    public void excludeFromResponse(){}

    public GsApiResponseProperty addResponseProperty(){}

    public void includeAllToResponseProperty(){}

}