package com.sa.orm.demo;

import com.sa.orm.reflect.annotation.*;

@SuppressWarnings("serial")
@Entity(name = "FirmUser", inherits = "User")
public class FirmUser extends User {

  /**
   * Unique identifier of this object.
   */
	@Field(minValue = 1, required = true)
	@PrimaryKey
  @ForeignKey(referenceEntity = "User", referencedField = "id")
	protected Integer userId;
	
  /**
   * Unique identifier of this object.
   */
  @Field(minValue = 1, required = true)
  @ForeignKey(referenceEntity = "Firm", referencedField = "id")
  protected Integer firmId;
  
	/**
	 * Status of this firm user.
	 */
	@Field
  @ForeignKey(referenceEntity = "UserStatus", referencedField = "id")
	protected int userStatusId;

  /**
   * Status of this firm user.
   */
  @Field
  @ForeignKey(referenceEntity = "FirmUserType", referencedField = "id")
  protected int firmUserTypeId;

	/**
	 * Corresponding customer service representative object.
	 */
	@ContainedObject(referencedEntity = "UserStatus", referencedField = "id", localInstanceMember = "userStatusId", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT)
	protected User userStatus;

  /**
   * Corresponding customer service representative object.
   */
  @ContainedObject(referencedEntity = "FirmUserType", referencedField = "id", localInstanceMember = "firmUserTypeId", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT)
  protected User firmUserType;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getFirmId() {
    return firmId;
  }

  public void setFirmId(Integer firmId) {
    this.firmId = firmId;
  }

  public int getUserStatusId() {
    return userStatusId;
  }

  public void setUserStatusId(int userStatusId) {
    this.userStatusId = userStatusId;
  }

  public int getFirmUserTypeId() {
    return firmUserTypeId;
  }

  public void setFirmUserTypeId(int firmUserTypeId) {
    this.firmUserTypeId = firmUserTypeId;
  }

  public User getUserStatus() {
    return userStatus;
  }

  public void setUserStatus(User userStatus) {
    this.userStatus = userStatus;
  }

  public User getFirmUserType() {
    return firmUserType;
  }

  public void setFirmUserType(User firmUserType) {
    this.firmUserType = firmUserType;
  }

}
