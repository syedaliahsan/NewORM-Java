package com.sa.orm.demo;

import java.sql.Timestamp;

import com.sa.orm.reflect.annotation.*;

@SuppressWarnings("serial")
@Entity(name = "FirmUser", inherits = User.class, inheritPK = true)
public class FirmUser extends User {

  /**
   * Unique identifier of this object.
   * Note: This field is inherited from User when inheritPK = true.
   * It is kept here for backward compatibility but will be ignored during introspection
   * as the inherited PK is automatically added by the ORM.
   */
//	@Field(minValue = 1, required = true)
//	@PrimaryKey
//  @ForeignKey(referenceEntity = "User", referencedField = "id")
//	protected Integer id;

  @Field
	private String title;

	private String SSN;

  @Field
	private boolean isOwner = false;
	
	private int loginCount = 0;

  private String verificationCode;

  private int lastUpdatedBy;

  private Timestamp updatedAt;

  private double doubleValue;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSSN() {
    return SSN;
  }

  public void setSSN(String sSN) {
    SSN = sSN;
  }

  public boolean getIsOwner() {
    return isOwner;
  }

  public void setIsOwner(boolean isOwner) {
    this.isOwner = isOwner;
  }

  public int getLoginCount() {
    return loginCount;
  }

  public void setLoginCount(int loginCount) {
    this.loginCount = loginCount;
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }

  public int getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(int lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }

  public double getDoubleValue() {
    return doubleValue;
  }

  public void setDoubleValue(double doubleValue) {
    this.doubleValue = doubleValue;
  }
  
  
  /**
   * Unique identifier of this object.
   */
//  @Field(minValue = 1, required = true)
//  @ForeignKey(referenceEntity = "Firm", referencedField = "id")
//  protected Integer firmId;
  
	/**
	 * Status of this firm user.
	 */
//	@Field
//  @ForeignKey(referenceEntity = "UserStatus", referencedField = "id")
//	protected int userStatusId;

  /**
   * Status of this firm user.
   */
//  @Field
//  @ForeignKey(referenceEntity = "FirmUserType", referencedField = "id")
//  protected int firmUserTypeId;

	/**
	 * Corresponding customer service representative object.
	 */
//	@ContainedObject(referencedEntity = "UserStatus", referencedField = "id", localInstanceMember = "userStatusId", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT)
//	protected User userStatus;

  /**
   * Corresponding customer service representative object.
   */
//  @ContainedObject(referencedEntity = "FirmUserType", referencedField = "id", localInstanceMember = "firmUserTypeId", relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT)
//  protected User firmUserType;

//  public Integer getUserId() {
//    return id;
//  }
//
//  public void setUserId(Integer userId) {
//    this.id = userId;
//  }

//  public Integer getFirmId() {
//    return firmId;
//  }
//
//  public void setFirmId(Integer firmId) {
//    this.firmId = firmId;
//  }

//  public int getUserStatusId() {
//    return userStatusId;
//  }
//
//  public void setUserStatusId(int userStatusId) {
//    this.userStatusId = userStatusId;
//  }
//
//  public int getFirmUserTypeId() {
//    return firmUserTypeId;
//  }
//
//  public void setFirmUserTypeId(int firmUserTypeId) {
//    this.firmUserTypeId = firmUserTypeId;
//  }
//
//  public User getUserStatus() {
//    return userStatus;
//  }
//
//  public void setUserStatus(User userStatus) {
//    this.userStatus = userStatus;
//  }
//
//  public User getFirmUserType() {
//    return firmUserType;
//  }
//
//  public void setFirmUserType(User firmUserType) {
//    this.firmUserType = firmUserType;
//  }

}
