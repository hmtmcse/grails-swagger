package com.hmtmcse.gs


class GsConfigHolder {

    public static Integer itemsPerPage() {
        return 20
    }

    public static String sortColumn() {
        return "id"
    }

    public static String sortOrder() {
        return "desc"
    }

    public static String failedMessage() {
        return "Unable to Process Request"
    }

    static String controllerStartWith() {
        return "Api"
    }

    public static String getUID(){
        return UUID.randomUUID().toString().toUpperCase();
    }

    static String invalidRequest() {
        return "Invalid API Request"
    }

    static String invalidRequestType() {
        return "Invalid API Request Type"
    }

}
