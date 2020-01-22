package com.hmtmcse.gs.controllers

import grails.util.Holders


class SwaggerUiInterceptor {

    boolean before() {
        return Holders.config?.grailsSwagger?.isEnableUi
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
