package com.hmtmcse.gs.data

class GsFilteredData {

    public Object offset
    public Object max
    public Object orderProperty
    public Object order
    public Object propertyName
    public Object propertyValue
    public GsParamsPairData gsParamsPairData
    public GsWhereData where = new GsWhereData()
    public Closure whereClosure
    public Map offsetMaxSort = [:]

}
