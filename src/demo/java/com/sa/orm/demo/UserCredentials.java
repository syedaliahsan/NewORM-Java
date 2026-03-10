package com.sa.orm.demo;

import com.sa.orm.reflect.annotation.AnnotationConstants;
import com.sa.orm.reflect.annotation.ContainedObject;
import com.sa.orm.reflect.annotation.Entity;
import com.sa.orm.reflect.annotation.Field;
import com.sa.orm.reflect.annotation.PrimaryKey;
import com.sa.orm.reflect.annotation.ForeignKey;
import com.sa.orm.reflect.annotation.Unique;


@SuppressWarnings("serial")
@Entity(name = "UserCredentials")
public class UserCredentials extends AbstractVO {
	
  @Field(minValue = 1, required = true)
  @PrimaryKey
  @ForeignKey(referenceEntity = "User", referencedField = "id")
  private Integer userId;
	
	@Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 20)
	@Unique
	private String username;
	
	@Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 20)
	private String password;
	
	@ContainedObject(localInstanceMember = "userId", referencedEntity = "User", referencedField = "id", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT)
	private User user;
	
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
	
} // end of class UserLogin
