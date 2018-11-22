package com.hmtmcse.gs.data

class GsDomain {

    public List<GsDomainProperty> allProperties = new ArrayList<>()
    public List<GsDomainHasMany> hasMany = new ArrayList<>()
    public List<GsDomainBelongTo> belongsTo = new ArrayList<>()
    public List<GsDomainOneToOne> oneToOne = new ArrayList<>()
}
