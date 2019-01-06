package com.hmtmcse.gs.model

import com.hmtmcse.gs.data.GsFilteredData

interface RequestPreProcessor {
    public GsFilteredData process(GsFilteredData gsFilteredData)
}