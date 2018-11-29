package com.hmtmcse.gs.data


class GsRequestResponseProperty {

    public String name
    public String alias = null
    public String dataType = null
    public String example = null
    public String format = null
    public String description = null
    public String defaultValue = ""
    public String referenceDefinition = null

    String getName() {
        return name
    }

    String getAlias() {
        return alias
    }

    String getDataType() {
        return dataType
    }

    String getExample() {
        return example
    }

    String getFormat() {
        return format
    }

    String getDescription() {
        return description
    }

    String getDefaultValue() {
        return defaultValue
    }
}
