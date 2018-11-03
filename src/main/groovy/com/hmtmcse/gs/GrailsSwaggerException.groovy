package com.hmtmcse.gs

class GrailsSwaggerException extends Exception {

    public GrailsSwaggerException(){
        super("Unknown Exception")
    }

    public GrailsSwaggerException(String message){
        super(message)
    }

}
