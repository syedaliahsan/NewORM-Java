package com.sa.orm.demo;

import com.sa.orm.reflect.annotation.AnnotationConstants;
import com.sa.orm.reflect.annotation.ContainedObject;
import com.sa.orm.reflect.annotation.Entity;
import com.sa.orm.reflect.annotation.Field;
import com.sa.orm.reflect.annotation.PrimaryKey;


@Entity
public class User extends AbstractVO {

	/**
	 * Unique serial (for serialization and de-serialization) for this class.
	 */
	private static final long serialVersionUID = 20110427000000002L;
	
	@Field(minValue = 1, required = true)
	@PrimaryKey
	private int id;
	
	@Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 20)
	private String firstName;
	
	@Field(required = false, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 10)
	private String middleName;
	
	@Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 20)
	private String lastName;
	
	@Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 100)
	private String email;
	
  @Field(required = true)
  private Integer userTypeId;
  
	//@Field(required = true, type = Field.Type.FIXED_LENGTH_TEXT, maxLength = 1)
	//private String status;

  private String username;

  /**
   * Corresponding {@link UserCredentials} object.
   */
  @ContainedObject(referencedEntity = "UserCredentials", referencedField = "userId", localInstanceMember ="id", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_CHILD)
  private UserCredentials userLogin;
  
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getUserTypeId() {
		return userTypeId;
	}

	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public UserCredentials getUserLogin() {
    return userLogin;
  }
	

  public void setUserLogin(UserCredentials userLogin) {
    this.userLogin = userLogin;
  }
} // end of class User
