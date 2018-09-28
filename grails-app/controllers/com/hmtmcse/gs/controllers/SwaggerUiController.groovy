package com.hmtmcse.gs.controllers

import com.hmtmcse.swagger.definition.SwaggerDefinition
import com.hmtmcse.swagger.definition.SwaggerProperty
import grails.converters.JSON

class SwaggerUiController {

    def index() { }

    def definition() {
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition()
        swaggerDefinition.setInfo("Bismillah Swagger")
        swaggerDefinition.setTag("mia", "vai").setExternalDocs("url", "xyz")
        swaggerDefinition.setTag("kotha", "hobe")

        SwaggerProperty swaggerProperty = swaggerDefinition
                .property("id", "integer").format("int64")
                .property("petId", "integer").format("int64")
                .property("quantity", "integer").format("int64")
                .property("shipDate", "integer").format("date-time")
        swaggerDefinition.addDefinition("Order", "object").addProperties(swaggerProperty)

        render(swaggerDefinition.getDefinition() as JSON)
    }
}
