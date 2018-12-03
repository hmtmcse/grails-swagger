package com.hmtmcse.gs

class GsValidationException extends Exception {

    public GsValidationException(){
        super("Data Validation Exception")
    }

    public GsValidationException(String message){
        super(message)
    }

}
