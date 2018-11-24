package com.hmtmcse.gs.data

class GsDomain {

    public LinkedHashMap<String, GsDomainProperty> domainProperties = new LinkedHashMap<>();

    public List<GsDomainProperty> allProperties = new ArrayList<>()
    public List<GsDomainHasMany> hasMany = new ArrayList<>()
    public List<GsDomainBelongTo> belongsTo = new ArrayList<>()
    public List<GsDomainOneToOne> oneToOne = new ArrayList<>()
}
