package com.hmtmcse.gs

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
}
