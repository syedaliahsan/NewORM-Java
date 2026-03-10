package com.sa.orm.demo;

import com.sa.orm.reflect.annotation.Entity;
import com.sa.orm.reflect.annotation.Field;
import com.sa.orm.reflect.annotation.PrimaryKey;

@SuppressWarnings("serial")
@Entity(name = "FirmUserType")
public class FirmUserType extends AbstractVO {

  @Field(minValue = 1, required = true)
  @PrimaryKey
  private Integer id;

  @Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 15)
  private String label;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

}