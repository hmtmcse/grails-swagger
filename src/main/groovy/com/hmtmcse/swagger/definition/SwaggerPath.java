package com.hmtmcse.swagger.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwaggerPath {

    private HashMap<String, Object> definition = new HashMap<>();

    private List<String> produces = Arrays.asList(SwaggerConstant.APPLICATION_JSON);
    private List<String> consumes = new ArrayList<>();


    public SwaggerPath addResponseProduceType(String type) {
        this.produces.add(type);
        return this;
    }

    public SwaggerPath summary(String summary) {
        definition.put("summary", summary);
        return this;
    }

    public SwaggerPath description(String description) {
        definition.put("description", description);
        return this;
    }

    public SwaggerPath operationId(String operationId) {
        definition.put("operationId", operationId);
        return this;
    }


    public SwaggerPath addRequestConsumeType(String type) {
        this.consumes.add(type);
        return this;
    }


    public HashMap getDefinition(){
        if (consumes.size() != 0){
            definition.put("consumes", consumes);
        }
        definition.put("produces", produces);

        return this.definition;
    }
}
