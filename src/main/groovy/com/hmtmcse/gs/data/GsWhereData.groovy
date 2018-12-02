package com.hmtmcse.gs.data

class GsWhereData {

    public LinkedHashMap<String, Object> equal = null
    public LinkedHashMap<String, Object> notEqual = null
    public LinkedHashMap<String, Object> lessThan = null
    public LinkedHashMap<String, Object> lessThanEqual = null
    public LinkedHashMap<String, Object> greaterThan = null
    public LinkedHashMap<String, Object> greaterThanEqual = null
    public LinkedHashMap<String, Object> like = null
    public LinkedHashMap<String, Object> order = null
    public LinkedHashMap<String, LinkedHashMap<Object, Object>> between = null
    public Boolean count = false
    public List<Object> inList = null
    public List<Object> select = null
    public GsWhereData and = null
    public GsWhereData or = null

}
