package com.hmtmcse.gs.controllers

import com.hmtmcse.swagger.definition.SwaggerDefinitionExample
import grails.converters.JSON

class SwaggerUiController {

    def index() { }

    def definition() {
        render(SwaggerDefinitionExample.first() as JSON)
    }
}
