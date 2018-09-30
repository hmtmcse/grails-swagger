package com.hmtmcse.gs


class GsConfigService {

    def itemsPerPage() {
        return 20
    }

    String sortColumn() {
        return "id"
    }

    String sortOrder() {
        return "desc"
    }

    String failedMessage() {
        return "Unable to Process Request"
    }

    static String controllerStartWith() {
        return "Api"
    }

    public static String getUID(){
        return UUID.randomUUID().toString().toUpperCase();
    }


}
