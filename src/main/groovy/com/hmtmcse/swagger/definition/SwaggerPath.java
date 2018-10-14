package com.hmtmcse.swagger.definition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SwaggerPath {

    private HashMap<String, Object> definition = new HashMap<>();

    private List<String> produces = Arrays.asList(SwaggerConstant.APPLICATION_JSON);
    private List<String> consumes = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<Object> parameters = new ArrayList<>();


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

    public SwaggerPath parameters(List<Object> parameters) {
        this.parameters = parameters;
        return this;
    }

    public SwaggerPath addRequestConsumeType(String type) {
        this.consumes.add(type);
        return this;
    }

    public SwaggerPath addParameter(SwaggerPathParameter parameter) {
        parameters.add(parameter.getDefinition());
        return this;
    }

    public SwaggerPath addConsumeType(String consumeType) {
        consumes.add(consumeType);
        return this;
    }

    public SwaggerPath addResponse(SwaggerPathResponse response) {
        definition.put("responses", response.getDefinition());
        return this;
    }

    public SwaggerPath addTag(String tag) {
        tags.add(tag);
        return this;
    }

    public SwaggerPath deprecated() {
        definition.put("deprecated", true);
        return this;
    }

    public HashMap getDefinition(){
        if (consumes.size() != 0){
            definition.put("consumes", consumes);
        }
        if (parameters.size() != 0){
            definition.put("parameters", parameters);
        }
        if (tags.size() != 0){
            definition.put("tags", tags);
        }
        definition.put("produces", produces);
        return this.definition;
    }
}
