package com.hmtmcse.gs.data

class GsDomainProperty {

    public String name
    public String swaggerDataType
    public Boolean isRelationalEntity = false
    public Boolean isOwningSide = false
    public LinkedHashMap<String, GsDomainProperty> relationalProperties = new LinkedHashMap<>()

    GsDomainProperty() {}

    GsDomainProperty(String name, String swaggerDataType) {
        this.name = name
        this.swaggerDataType = swaggerDataType
    }
}
