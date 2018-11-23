package com.hmtmcse.gs

import com.hmtmcse.gs.data.GsApiResponseProperty


class GsResponseMarshallerService {

    def makeResponseData(GsApiActionDefinition definition,  def queryResult, def defaultResponse = [:]) {

    }



    private def valueFromDomain(String key, def domain, GsApiResponseProperty gsApiResponseProperty) {
        try {
            return domain[key]
        } catch (Exception e) {
            return gsApiResponseProperty.getDefaultValue()
        }
    }




    def responseMapGenerator(Map<String, GsApiResponseProperty> responseProperties, def queryResult, def defaultResponse = [:]) {
        List resultList = []
        Map resultMap = [:]
        if (queryResult) {
            if (queryResult instanceof List) {
                queryResult.each { data ->
                    resultMap = [:]
                    responseProperties.each { String fieldName, GsApiResponseProperty response ->
                        resultMap.put(response.getMapKey(), valueFromDomain(fieldName, data, response))
                    }
                    resultList.add(resultMap)
                }
                return resultList
            } else {
                responseProperties.each { String fieldName, GsApiResponseProperty response ->
                    resultMap.put(response.getMapKey(), valueFromDomain(fieldName, queryResult, response))
                }
                return resultMap
            }
        }
        return defaultResponse
    }


}
