package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty

trait GsRequestOrganizer {


    public Map<String, GsApiRequestProperty> requestProperties = new LinkedHashMap<>()


    public void includeInRequest(){}

    public void excludeFromRequest(){}

    public GsApiResponseProperty addRequestProperty(){}

    public void includeAllToRequestProperty(){}

}