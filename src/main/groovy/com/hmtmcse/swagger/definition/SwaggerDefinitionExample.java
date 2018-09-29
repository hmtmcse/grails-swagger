package com.hmtmcse.swagger.definition;

public class SwaggerDefinitionExample {


    public static SwaggerDefinition first(){
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition();
        swaggerDefinition.setInfo("Bismillah Swagger");
        swaggerDefinition.setTag("mia", "vai").setExternalDocs("url", "xyz");
        swaggerDefinition.setTag("kotha", "hobe");

        SwaggerProperty swaggerProperty = swaggerDefinition
                .property("id", "integer").format("int64")
                .property("petId", "integer").format("int64")
                .property("quantity", "integer").format("int64")
                .property("shipDate", "integer").format("date-time");
        swaggerDefinition.addDefinition("Order", "object").addProperties(swaggerProperty);


        return swaggerDefinition;
    }

}
