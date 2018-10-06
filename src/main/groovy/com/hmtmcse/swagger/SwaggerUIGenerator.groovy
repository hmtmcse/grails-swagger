package com.hmtmcse.swagger

import com.hmtmcse.swagger.definition.SwaggerConstant
import com.hmtmcse.swagger.definition.SwaggerDefinition
import com.hmtmcse.swagger.definition.SwaggerPath
import com.hmtmcse.swagger.definition.SwaggerPathParameter
import com.hmtmcse.swagger.definition.SwaggerPathResponse
import com.hmtmcse.swagger.definition.SwaggerProperty

class SwaggerUIGenerator {

    public static Map<Object, Object> generate(){
        SwaggerDefinition swaggerDefinition = new SwaggerDefinition()

        swaggerDefinition.host("petstore.swagger.io")
        swaggerDefinition.basePath("/v2")
        swaggerDefinition.scheme("https")

        swaggerDefinition.addTag("pet", "Everything about your Pets")
        swaggerDefinition.addTag("store", "Access to Petstore orders")

        SwaggerPath swaggerPath = swaggerDefinition.path()
        SwaggerPathParameter parameter = swaggerDefinition.pathParameter(SwaggerConstant.IN_BODY, "body")
        parameter.description("Pet object that needs to be added to the store");
        parameter.required().schema("Pet")

        swaggerPath.addTag("pet")
        swaggerPath.summary("Add a new pet to the store")
        swaggerPath.description("")
        swaggerPath.operationId("addPet")
        swaggerPath.addParameter(parameter);

        SwaggerPathResponse response = swaggerDefinition.pathResponse(SwaggerConstant.SUCCESS);
        response.description("Success").schema("array", "Pet");
        swaggerPath.addResponse(response);
        swaggerDefinition.startPaths("/pet").addPath(SwaggerConstant.POST_METHOD, swaggerPath);


        SwaggerProperty swaggerProperty = swaggerDefinition
                .property("id", "integer").format("int64")
                .property("petId", "integer").format("int64")
                .property("quantity", "integer").format("int64")
                .property("shipDate", "integer").format("date-time");
        swaggerDefinition.addDefinition("Order", "object").addProperties(swaggerProperty);


        swaggerProperty = swaggerDefinition
                .property("id", "integer").format("int64")
                .property("name", "string").example("doggie");
        swaggerDefinition.addDefinition("Pet", "object").addProperties(swaggerProperty);


        return swaggerDefinition.getDefinition();
    }
}
