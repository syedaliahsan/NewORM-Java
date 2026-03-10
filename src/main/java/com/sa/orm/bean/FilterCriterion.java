package com.sa.orm.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterCriterion {
    private String fieldName;
    private String operator;
    private Object value; // Jackson maps JSON primitives/arrays to Java Object/List

    public String getFieldName() { return fieldName; }
    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getOperator() { return operator; }
    public void setOperator(String operator) { this.operator = operator; }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    
    public String toString() {
      StringBuffer sb = new StringBuffer();
      sb.append("fieldNmae: ").append(fieldName);
      sb.append(", operator: ").append(operator);
      sb.append(", value: ").append(value);
      return sb.toString();
    }
}