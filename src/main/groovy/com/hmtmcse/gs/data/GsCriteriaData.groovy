package com.hmtmcse.gs.data

import com.hmtmcse.gs.GsApiActionDefinition

class GsCriteriaData {

    public List<GsWhereFilterProperty> whereAllowedPropertyList = []
    public LinkedHashMap<String, GsWhereFilterProperty> whereAllowedPropertyMap = [:]
    public GsApiActionDefinition gsApiActionDefinition
    public Map where
    public Boolean basicCriteria = false

}
