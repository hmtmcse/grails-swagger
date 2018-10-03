package com.hmtmcse.gs

class GsException extends RuntimeException {

    public GsException(){
        super("Invalid Request")
    }

    public GsException(String message){
        super(message)
    }
}
