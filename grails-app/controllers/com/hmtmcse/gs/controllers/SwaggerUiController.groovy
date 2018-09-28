package com.hmtmcse.gs.controllers

import com.hmtmcse.swagger.definition.SwaggerDefinition
import grails.converters.JSON

class SwaggerUiController {

    def index() { }

    def definition() {
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition()
        swaggerDefinition.setInfo("Bismillah Swagger")
        render(swaggerDefinition.getDefinition() as JSON)
    }
}
