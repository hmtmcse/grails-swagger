package com.hmtmcse.gs.data

class GsWhereData {

    public LinkedHashMap<String, Object> equal = [:]
    public LinkedHashMap<String, Object> notEqual = [:]
    public LinkedHashMap<String, Object> lessThan = [:]
    public LinkedHashMap<String, Object> lessThanEqual = [:]
    public LinkedHashMap<String, Object> greaterThan = [:]
    public LinkedHashMap<String, Object> greaterThanEqual = [:]
    public LinkedHashMap<String, Object> like = [:]
    public LinkedHashMap<String, Object> order = [:]
    public LinkedHashMap<String, LinkedHashMap<Object, Object>> between = [:]
    public Boolean count = false
    public LinkedHashMap<String, List<Object>> inList  = []
    public List<Object> select = []
    public GsWhereData and = null
    public GsWhereData or = null

}
