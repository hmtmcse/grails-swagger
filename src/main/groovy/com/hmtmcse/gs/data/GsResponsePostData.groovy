package com.hmtmcse.gs.data


class GsResponsePostData {

    public String message = null
    public Boolean isSuccess = false
    public Object response = null
    public Object queryResult = null

    GsResponsePostData(Boolean isSuccess, Object response,  Object queryResult) {
        this.isSuccess = isSuccess
        this.response = response
        this.queryResult = queryResult
    }
}
