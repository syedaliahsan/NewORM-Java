package com.sa.orm;

public class NameValueVO {

  private String attributeName;
  private Object attributeValue;
  
  public NameValueVO(String attributeName, Object attributeValue) {
    super();
    this.attributeName = attributeName;
    this.attributeValue = attributeValue;
  }
  
  public String getAttributeName() {
    return attributeName;
  }

  public Object getAttributeValue() {
    return attributeValue;
  }
  
}
