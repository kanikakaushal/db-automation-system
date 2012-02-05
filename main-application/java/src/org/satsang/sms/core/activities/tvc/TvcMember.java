package org.satsang.sms.core.activities.tvc;

import java.util.Date;

public class TvcMember {

    /* Private Fields */
    private String tvcId;    
    private String firstName;
    private String middleName;
    private String lastName;
    private Integer age;
    private String occupation;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String addressCity;
    private String addressState;
    private String addressCountry;
    private String addressPinCode;
    private String initiationStatus;
    private String regionId;
	private String districtId;
	private String branchId;
	private String regionName;
	private String districtName;
	private String branchName;
    private String localAddress;
    private String localColony;    
    private String localSadan;
    private String stayCategory;    
    private Date stayFromDate;
    private Date stayToDate;    
    private int noOfChildren;    
    private String childrenAges;    
    private String permissionType;
    private String permissionRemarks;
    private boolean isDuplicate;
    private String homeStateType;
    private String pqLicenseeRelation;
    private String pqLicenseeName;
    private String recommendationType;
    private boolean isAssociate;
    private String letterNoIfJigyasu;
    private String initiatedParent;
    
    
    public TvcMember() {
    }

	/**
	 * @return the tvcId
	 */
	public String getTvcId() {
		return tvcId;
	}

	/**
	 * @param tvcId the tvcId to set
	 */
	public void setTvcId(String tvcId) {
		this.tvcId = tvcId;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the addressLine1
	 */
	public String getAddressLine1() {
		return addressLine1;
	}

	/**
	 * @param addressLine1 the addressLine1 to set
	 */
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	/**
	 * @return the addressLine2
	 */
	public String getAddressLine2() {
		return addressLine2;
	}

	/**
	 * @param addressLine2 the addressLine2 to set
	 */
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	/**
	 * @return the addressCity
	 */
	public String getAddressCity() {
		return addressCity;
	}

	/**
	 * @param addressCity the addressCity to set
	 */
	public void setAddressCity(String addressCity) {
		this.addressCity = addressCity;
	}

	/**
	 * @return the addressState
	 */
	public String getAddressState() {
		return addressState;
	}

	/**
	 * @param addressState the addressState to set
	 */
	public void setAddressState(String addressState) {
		this.addressState = addressState;
	}

	/**
	 * @return the addressCountry
	 */
	public String getAddressCountry() {
		return addressCountry;
	}

	/**
	 * @param addressCountry the addressCountry to set
	 */
	public void setAddressCountry(String addressCountry) {
		this.addressCountry = addressCountry;
	}

	/**
	 * @return the addressPinCode
	 */
	public String getAddressPinCode() {
		return addressPinCode;
	}

	/**
	 * @param addressPinCode the addressPinCode to set
	 */
	public void setAddressPinCode(String addressPinCode) {
		this.addressPinCode = addressPinCode;
	}

	/**
	 * @return the initiationStatus
	 */
	public String getInitiationStatus() {
		return initiationStatus;
	}

	/**
	 * @param initiationStatus the initiationStatus to set
	 */
	public void setInitiationStatus(String initiationStatus) {
		this.initiationStatus = initiationStatus;
	}

	/**
	 * @return the regionId
	 */
	public String getRegionId() {
		return regionId;
	}

	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return the districtId
	 */
	public String getDistrictId() {
		return districtId;
	}

	/**
	 * @param districtId the districtId to set
	 */
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	/**
	 * @return the branchId
	 */
	public String getBranchId() {
		return branchId;
	}

	/**
	 * @param branchId the branchId to set
	 */
	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	/**
	 * @return the districtName
	 */
	public String getDistrictName() {
		return districtName;
	}

	/**
	 * @param districtName the districtName to set
	 */
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	/**
	 * @return the branchName
	 */
	public String getBranchName() {
		return branchName;
	}

	/**
	 * @param branchName the branchName to set
	 */
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	/**
	 * @return the localAddress
	 */
	public String getLocalAddress() {
		return localAddress;
	}

	/**
	 * @param localAddress the localAddress to set
	 */
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	/**
	 * @return the localColony
	 */
	public String getLocalColony() {
		return localColony;
	}

	/**
	 * @param localColony the localColony to set
	 */
	public void setLocalColony(String localColony) {
		this.localColony = localColony;
	}

	/**
	 * @return the localSadan
	 */
	public String getLocalSadan() {
		return localSadan;
	}

	/**
	 * @param localSadan the localSadan to set
	 */
	public void setLocalSadan(String localSadan) {
		this.localSadan = localSadan;
	}

	/**
	 * @return the stayCategory
	 */
	public String getStayCategory() {
		return stayCategory;
	}

	/**
	 * @param stayCategory the stayCategory to set
	 */
	public void setStayCategory(String stayCategory) {
		this.stayCategory = stayCategory;
	}

	/**
	 * @return the stayFromDate
	 */
	public Date getStayFromDate() {
		return stayFromDate;
	}

	/**
	 * @param stayFromDate the stayFromDate to set
	 */
	public void setStayFromDate(Date stayFromDate) {
		this.stayFromDate = stayFromDate;
	}

	/**
	 * @return the stayToDate
	 */
	public Date getStayToDate() {
		return stayToDate;
	}

	/**
	 * @param stayToDate the stayToDate to set
	 */
	public void setStayToDate(Date stayToDate) {
		this.stayToDate = stayToDate;
	}

	/**
	 * @return the noOfChildren
	 */
	public int getNoOfChildren() {
		return noOfChildren;
	}

	/**
	 * @param noOfChildren the noOfChildren to set
	 */
	public void setNoOfChildren(int noOfChildren) {
		this.noOfChildren = noOfChildren;
	}

	/**
	 * @return the childrenAges
	 */
	public String getChildrenAges() {
		return childrenAges;
	}

	/**
	 * @param childrenAges the childrenAges to set
	 */
	public void setChildrenAges(String childrenAges) {
		this.childrenAges = childrenAges;
	}
	
	/**
	 * @return the permissionType
	 */
	public String getPermissionType() {
		return permissionType;
	}

	/**
	 * @param permissionType the permissionType to set
	 */
	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}

	/**
	 * @return the permissionRemarks
	 */
	public String getPermissionRemarks() {
		return permissionRemarks;
	}

	/**
	 * @param permissionRemarks the permissionRemarks to set
	 */
	public void setPermissionRemarks(String permissionRemarks) {
		this.permissionRemarks = permissionRemarks;
	}

	/**
	 * @return the isDuplicate
	 */
	public boolean isDuplicate() {
		return isDuplicate;
	}

	/**
	 * @param isDuplicate the isDuplicate to set
	 */
	public void setDuplicate(boolean isDuplicate) {
		this.isDuplicate = isDuplicate;
	}

	public void setHomeStateType(String homeStateType) {
		this.homeStateType = homeStateType;
	}

	public String getHomeStateType() {
		return homeStateType;
	}

	/**
	 * @param occupation the occupation to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	public void setPqLicenseeRelation(String pqLicenseeRelation) {
		this.pqLicenseeRelation = pqLicenseeRelation;
	}

	public String getPqLicenseeRelation() {
		return pqLicenseeRelation;
	}

	public void setPqLicenseeName(String pqLicenseeName) {
		this.pqLicenseeName = pqLicenseeName;
	}

	public String getPqLicenseeName() {
		return pqLicenseeName;
	}

	/**
	 * @param recommendationType the recommendationType to set
	 */
	public void setRecommendationType(String recommendationType) {
		this.recommendationType = recommendationType;
	}

	/**
	 * @return the recommendationType
	 */
	public String getRecommendationType() {
		return recommendationType;
	}

	/**
	 * @param isAssociate the isAssociate to set
	 */
	public void setAssociate(boolean isAssociate) {
		this.isAssociate = isAssociate;
	}

	/**
	 * @return the isAssociate
	 */
	public boolean isAssociate() {
		return isAssociate;
	}

	/**
	 * @param letterNoIfJigyasu the letterNoIfJigyasu to set
	 */
	public void setLetterNoIfJigyasu(String letterNoIfJigyasu) {
		this.letterNoIfJigyasu = letterNoIfJigyasu;
	}

	/**
	 * @return the letterNoIfJigyasu
	 */
	public String getLetterNoIfJigyasu() {
		return letterNoIfJigyasu;
	}

	public String getInitiatedParent() {
		return initiatedParent;
	}

	public void setInitiatedParent(String initiatedParent) {
		this.initiatedParent = initiatedParent;
	}
    
}
