package com.hmtmcse.gs

class GsExceptionParser {


    static void throwException(Exception exception) throws GrailsSwaggerException{
        String name = exception.getClass().name
        switch (name){
            case "org.hibernate.NonUniqueResultException":
                throw new GrailsSwaggerException("The Query did not return a unique result")
            case  "java.lang.NumberFormatException":
                throw new GrailsSwaggerException(GsConfigHolder.invalidFieldData())
            default:
                throw new GrailsSwaggerException(GsConfigHolder.failedMessage())
        }
    }

    static String exceptionMessage(Exception exception){
        try{
            throwException(exception)
            return GsConfigHolder.failedMessage()
        }catch(GrailsSwaggerException gse){
            return gse.getMessage()
        }
    }
}
