package com.hmtmcse.gs

import com.hmtmcse.swagger.definition.SwaggerConstant
import grails.util.Holders
import grails.web.servlet.mvc.GrailsParameterMap

import javax.servlet.http.HttpServletRequest

class GsUtil {

    public static String makeHumReadble(String text){
        text = toSnakeCase(text)
        return toCamelCase(text, true)
    }

    public static String toCamelCase( String text, boolean capitalized = false ) {
        text = text.replaceAll( "(_)([A-Za-z0-9])", { Object[] it -> it[2].toUpperCase() } )
        return capitalized ? text?.capitalize() : text
    }


    public static String toSnakeCase( String text ) {
        text.replaceAll( /([A-Z])/, /_$1/ ).toLowerCase().replaceAll( /^_/, '' )
    }

    static def setJsonToParams(HttpServletRequest request, GrailsParameterMap params) {
        try{
            String contentType = request.getContentType()
            if (contentType && contentType.equals(SwaggerConstant.APPLICATION_JSON) && request.JSON){
                params.gsApiData = request.JSON
            }
            params.gsHttpRequestMethod = request.method
        }catch(Exception e){
            println(GsConfigHolder.unableToParseJSON + ". Error: " + e.getMessage())
            return false
        }
        return true
    }

    static String removeLastChar(String string, String lastChar) {
        if(string.endsWith(lastChar)) {
            return string.substring(0, string.lastIndexOf(lastChar));
        } else {
            return string
        }
    }

    public static def getMapValue(String key, Map map){
        try{
            return map.get(key)
        }catch(Exception e){
            return null
        }
    }

    static def getBean(String beanIdentifier) {
        try {
            return Holders.grailsApplication.mainContext.getBean(beanIdentifier)
        } catch (Exception e) {
            return null
        }
    }

    static GsCommonHelpService getCommonHelpService() {
        return getBean("gsCommonHelpService")
    }
}
