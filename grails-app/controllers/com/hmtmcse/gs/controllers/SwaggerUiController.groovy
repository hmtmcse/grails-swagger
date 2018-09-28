package com.hmtmcse.gs.controllers

import com.hmtmcse.swagger.definition.SwaggerDefinition
import grails.converters.JSON

class SwaggerUiController {

    def index() { }

    def definition() {
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition()
        swaggerDefinition.setInfo("Bismillah Swagger")
        swaggerDefinition.setTag("mia", "vai").setExternalDocs("url", "xyz")
        swaggerDefinition.setTag("kotha", "hobe")
        render(swaggerDefinition.getDefinition() as JSON)
    }
}
