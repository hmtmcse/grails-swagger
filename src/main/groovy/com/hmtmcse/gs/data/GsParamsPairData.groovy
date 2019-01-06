package com.hmtmcse.gs.data

import grails.web.servlet.mvc.GrailsParameterMap

class GsParamsPairData {

    public String httpMethod
    public Map params = [:]
    public GrailsParameterMap rawParams
    public GrailsParameterMap filteredGrailsParameterMap = null


    public GsParamsPairData initFilteredGrailsParams(){
        try{
            def request = rawParams.getRequest()
            if (request && params){
                filteredGrailsParameterMap = new GrailsParameterMap(params, request)
            }
        }catch(Exception e){}
        return this
    }

    public addToParams(String key, Object value){
        rawParams[key] = filteredGrailsParameterMap[key] = params[key] = value
    }

}
