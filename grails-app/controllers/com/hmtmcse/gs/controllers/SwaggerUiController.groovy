package com.hmtmcse.gs.controllers

import com.hmtmcse.gs.GsConfigHolder
import com.hmtmcse.gs.SwaggerUIGeneratorService
import grails.converters.JSON

class SwaggerUiController {

    SwaggerUIGeneratorService swaggerUIGeneratorService

    def index() {
        [swaggerDefinitionUrl: GsConfigHolder.swaggerDefinitionUrl]
    }

    def definition() {
        render(swaggerUIGeneratorService.generate() as JSON)
    }
}
