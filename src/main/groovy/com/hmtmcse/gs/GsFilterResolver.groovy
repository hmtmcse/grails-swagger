package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsFilterData
import com.hmtmcse.gs.data.GsParamsPairData
import com.hmtmcse.gs.data.GsWhereData
import grails.web.servlet.mvc.GrailsParameterMap

class GsFilterResolver {


    private final Map whereSwaggerMap = [
            GsConstant.EQUAL: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.ORDER: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.NOT_EQUAL: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.LESS_THAN: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.LESS_THAN_EQUAL: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.GETTER_THAN: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.GETTER_THAN_EQUAL: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.LIKE: [
                    "propertyName" : "propertyValue",
                    "propertyName1" : "propertyValue",
            ],
            GsConstant.IN_LIST: ["propertyName", "propertyName1"],
            GsConstant.BETWEEN: [
                    "propertyName": ["propertyName" : "propertyValue"]
            ],
            GsConstant.SELECT: ["propertyName", "propertyName1"],
            GsConstant.COUNT: true,
    ]


    public GsParamsPairData getParamsPair(GrailsParameterMap params) {
        GsParamsPairData gsParamsPairData = new GsParamsPairData()
        gsParamsPairData.rawParams = params
        if (params.gsHttpRequestMethod) {
            switch (params.gsHttpRequestMethod.toLowerCase()) {
                case GsConstant.POST:
                    gsParamsPairData.httpMethod = GsConstant.POST
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.DELETE:
                    gsParamsPairData.httpMethod = GsConstant.DELETE
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.PUT:
                    gsParamsPairData.httpMethod = GsConstant.PUT
                    gsParamsPairData.params = params.gsApiData ?: [:]
                    return gsParamsPairData.initFilteredGrailsParams()
                case GsConstant.GET:
                    gsParamsPairData.httpMethod = GsConstant.GET
                    gsParamsPairData.params = params
                    return gsParamsPairData.initFilteredGrailsParams()
            }
        }
        return gsParamsPairData
    }


    public GsWhereData resolveWhereData(GrailsParameterMap params){

    }


    public GsWhereData resolveWhereClosure(GsWhereData whereData){

    }

    public GsFilterData resolvePagination(GrailsParameterMap params){

    }

}
