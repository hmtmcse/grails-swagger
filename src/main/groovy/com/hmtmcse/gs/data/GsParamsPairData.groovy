package com.hmtmcse.gs.data

import grails.web.servlet.mvc.GrailsParameterMap

class GsParamsPairData {

    public String httpMethod
    public Map params = [:]
    public Map rawParams
    public GrailsParameterMap filteredGrailsParameterMap = null


    public GsParamsPairData initFilteredGrailsParams(){
        if (rawParams instanceof GrailsParameterMap){
            try{
                def request = rawParams.getRequest()
                if (request && params){
                    filteredGrailsParameterMap = new GrailsParameterMap(params, request)
                }
            }catch(Exception e){}
        }
        return this
    }


}
