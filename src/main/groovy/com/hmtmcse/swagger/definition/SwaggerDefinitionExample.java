package com.hmtmcse.swagger.definition;

import java.util.Map;

public class SwaggerDefinitionExample {


    public static Map<Object, Object> first(){
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition();

        swaggerDefinition.info("Bismillah Swagger Definition")
                .description("This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key `special-key` to test the authorization filters.")
                .termsOfService("http://swagger.io/terms/")
                .contactEmail("apiteam@swagger.io")
                .license("Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("1.0.0");

        swaggerDefinition.host("petstore.swagger.io");
        swaggerDefinition.basePath("/v2");
        swaggerDefinition.host("petstore.swagger.io");
        swaggerDefinition.scheme("https");

        swaggerDefinition.addTag("mia", "vai").setExternalDocs("url", "xyz");
        swaggerDefinition.addTag("kotha", "hobe");

        SwaggerProperty swaggerProperty = swaggerDefinition
                .property("id", "integer").format("int64")
                .property("petId", "integer").format("int64")
                .property("quantity", "integer").format("int64")
                .property("shipDate", "integer").format("date-time");
        swaggerDefinition.addDefinition("Order", "object").addProperties(swaggerProperty);


        return swaggerDefinition.getDefinition();
    }

}
