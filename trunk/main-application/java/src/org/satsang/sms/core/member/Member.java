package org.satsang.sms.core.member;

import org.satsang.sms.core.member.interfaces.IMember;

public class Member implements IMember {

	/* Private Fields */
	private String memberId;
	private String memberFirstName;
	private String memberMiddleName;
	private String memberLastName;
	private String memberOccupation;
	private String memberGender;
	private String memberAddressLine1;
	private String memberAddressLine2;
	private String memberAddressCity;
	private String memberAddressState;
	private String memberAddressCountry;
	private String memberAddressPinCode;
	private String memberContactNo;
	private String branchCode;
	private String initiationStatus;
	private Integer memberAge;
	private String branchName;
	private String branchSecretary;
	private String districtId;
	private String districtName;
	private String districtSuffix;
	private String regionName;
	private String regionSuffix;
	private String regionId;
	private String letterNoIfJigyasu;
	private String initiatedParent;
	
	
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberFirstName() {
		return memberFirstName;
	}
	public void setMemberFirstName(String memberFirstName) {
		this.memberFirstName = memberFirstName;
	}
	public String getMemberMiddleName() {
		return memberMiddleName;
	}
	public void setMemberMiddleName(String memberMiddleName) {
		this.memberMiddleName = memberMiddleName;
	}
	public String getMemberLastName() {
		return memberLastName;
	}
	public void setMemberLastName(String memberLastName) {
		this.memberLastName = memberLastName;
	}
	public String getMemberOccupation() {
		return memberOccupation;
	}
	public void setMemberOccupation(String memberOccupation) {
		this.memberOccupation = memberOccupation;
	}
	public String getMemberGender() {
		return memberGender;
	}
	public void setMemberGender(String memberGender) {
		this.memberGender = memberGender;
	}
	public String getMemberAddressLine1() {
		return memberAddressLine1;
	}
	public void setMemberAddressLine1(String memberAddressLine1) {
		this.memberAddressLine1 = memberAddressLine1;
	}
	public String getMemberAddressLine2() {
		return memberAddressLine2;
	}
	public void setMemberAddressLine2(String memberAddressLine2) {
		this.memberAddressLine2 = memberAddressLine2;
	}
	public String getMemberAddressCity() {
		return memberAddressCity;
	}
	public void setMemberAddressCity(String memberAddressCity) {
		this.memberAddressCity = memberAddressCity;
	}
	public String getMemberAddressState() {
		return memberAddressState;
	}
	public void setMemberAddressState(String memberAddressState) {
		this.memberAddressState = memberAddressState;
	}
	public String getMemberAddressCountry() {
		return memberAddressCountry;
	}
	public void setMemberAddressCountry(String memberAddressCountry) {
		this.memberAddressCountry = memberAddressCountry;
	}
	public String getMemberAddressPinCode() {
		return memberAddressPinCode;
	}
	public void setMemberAddressPinCode(String memberAddressPinCode) {
		this.memberAddressPinCode = memberAddressPinCode;
	}
	public String getMemberContactNo() {
		return memberContactNo;
	}
	public void setMemberContactNo(String memberContactNo) {
		this.memberContactNo = memberContactNo;
	}
	public String getBranchCode() {
		return branchCode;
	}
	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}
	public String getInitiationStatus() {
		return initiationStatus;
	}
	public void setInitiationStatus(String initiationStatus) {
		this.initiationStatus = initiationStatus;
	}
	public Integer getMemberAge() {
		return memberAge;
	}
	public void setMemberAge(Integer memberAge) {
		this.memberAge = memberAge;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchSecretary() {
		return branchSecretary;
	}
	public void setBranchSecretary(String branchSecretary) {
		this.branchSecretary = branchSecretary;
	}
	public String getDistrictId() {
		return districtId;
	}
	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getDistrictSuffix() {
		return districtSuffix;
	}
	public void setDistrictSuffix(String districtSuffix) {
		this.districtSuffix = districtSuffix;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionSuffix() {
		return regionSuffix;
	}
	public void setRegionSuffix(String regionSuffix) {
		this.regionSuffix = regionSuffix;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
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
