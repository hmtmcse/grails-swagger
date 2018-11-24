package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiRequestProperty

class GsRelationalEntityRequest extends GsApiRequestProperty implements GsRequestOrganizer {


    GsRelationalEntityRequest(String name, String alias) {
        super(name, alias)
    }

    GsRelationalEntityRequest(String name) {
        super(name)
    }
}
