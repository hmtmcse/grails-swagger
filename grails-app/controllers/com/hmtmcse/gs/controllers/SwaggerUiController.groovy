package com.hmtmcse.gs.controllers

import com.hmtmcse.gs.SwaggerUIGeneratorService
import grails.converters.JSON

class SwaggerUiController {

    SwaggerUIGeneratorService swaggerUIGeneratorService

    def index() { }

    def definition() {
        render(swaggerUIGeneratorService.generate() as JSON)
    }
}
