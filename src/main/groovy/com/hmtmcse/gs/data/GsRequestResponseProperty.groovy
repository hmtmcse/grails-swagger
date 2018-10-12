package com.hmtmcse.gs.data

class GsRequestResponseProperty {

    public String name
    public String alias = null
    public String dataType = null
    public String example = null
    public String format = null
    public String defaultValue = ""
    public Closure customProcessor = null

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getAlias() {
        return alias
    }


    String getDataType() {
        return dataType
    }

    void setDataType(String dataType) {
        this.dataType = dataType
    }

    String getExample() {
        return example
    }

    void setExample(String example) {
        this.example = example
    }

    String getFormat() {
        return format
    }

    void setFormat(String format) {
        this.format = format
    }

    String getDefaultValue() {
        return defaultValue
    }

    void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue
    }

    Closure getCustomProcessor() {
        return customProcessor
    }

    void setCustomProcessor(Closure customProcessor) {
        this.customProcessor = customProcessor
    }
}
