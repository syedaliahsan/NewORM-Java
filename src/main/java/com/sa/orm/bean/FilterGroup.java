package com.sa.orm.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterGroup {
    private String operator; // "AND", "OR"
    private List<FilterCriterion> criteria;
    private List<FilterGroup> groups; // Recursive structure

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public List<FilterCriterion> getCriteria() { return criteria; }
    public void setCriteria(List<FilterCriterion> criteria) { this.criteria = criteria; }

    public List<FilterGroup> getGroups() { return groups; }
    public void setGroups(List<FilterGroup> groups) { this.groups = groups; }
}