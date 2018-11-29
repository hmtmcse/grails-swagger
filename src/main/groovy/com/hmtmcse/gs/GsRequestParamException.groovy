package com.hmtmcse.gs

class GsRequestParamException extends Exception {

    public GsRequestParamException(){
        super("Unknown Request Parameter")
    }

    public GsRequestParamException(String message){
        super(message)
    }

}
