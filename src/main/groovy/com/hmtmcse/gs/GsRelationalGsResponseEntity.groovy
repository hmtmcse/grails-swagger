package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty

class GsRelationalGsResponseEntity extends GsApiResponseProperty implements GsResponseOrganizer {

    public Map<String, GsApiResponseProperty> responseProperties = new LinkedHashMap<>()

    GsRelationalGsResponseEntity(String name, String alias) {
        super(name, alias)
    }


    GsRelationalGsResponseEntity(String name) {
        super(name)
    }

}
