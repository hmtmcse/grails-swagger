package com.hmtmcse.gs.controllers

import com.hmtmcse.swagger.SwaggerUIGenerator
import com.hmtmcse.swagger.definition.SwaggerDefinitionExample
import grails.converters.JSON

class SwaggerUiController {

    def index() { }

    def definition() {
        render(SwaggerUIGenerator.generate() as JSON)
    }
}
