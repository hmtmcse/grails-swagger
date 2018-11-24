package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty

class GsRelationalGsRequestEntity extends GsApiRequestProperty implements GsRequestOrganizer {


    GsRelationalGsRequestEntity(String name, String alias) {
        super(name, alias)
    }

    GsRelationalGsRequestEntity(String name) {
        super(name)
    }
}
