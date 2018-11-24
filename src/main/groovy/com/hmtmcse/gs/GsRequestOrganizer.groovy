package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty
import com.hmtmcse.gs.data.GsApiResponseProperty
import com.hmtmcse.gs.data.GsDomain

trait GsRequestOrganizer<T>  {


    public Map<String, GsApiRequestProperty> requestProperties = new LinkedHashMap<>()
    public GsDomain gsDomain = new GsDomain()


    public void includeInRequest(){}

    public void excludeFromRequest(){}

    public GsApiResponseProperty addRequestProperty(){}

    public void includeAllToRequestProperty(){}

}