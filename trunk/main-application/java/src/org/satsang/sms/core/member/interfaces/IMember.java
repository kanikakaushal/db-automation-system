package org.satsang.sms.core.member.interfaces;

public interface IMember {
	
	String getMemberId() ;
	void setMemberId(String memberId);
	String getMemberFirstName();
	void setMemberFirstName(String memberFirstName);
	String getMemberMiddleName();
	void setMemberMiddleName(String memberMiddleName);
	String getMemberLastName();
	void setMemberLastName(String memberLastName);
	String getMemberOccupation();
	void setMemberOccupation(String memberOccupation);
	String getMemberGender();
	void setMemberGender(String memberGender);
	String getMemberAddressLine1();
	void setMemberAddressLine1(String memberAddressLine1);
	String getMemberAddressLine2();
	void setMemberAddressLine2(String memberAddressLine2);
	String getMemberAddressCity();
	void setMemberAddressCity(String memberAddressCity);
	String getMemberAddressState();
	void setMemberAddressState(String memberAddressState);
	String getMemberAddressCountry();
	void setMemberAddressCountry(String memberAddressCountry);
	String getMemberAddressPinCode();
	void setMemberAddressPinCode(String memberAddressPinCode);
	String getMemberContactNo();
	void setMemberContactNo(String memberContactNo);
	String getBranchCode();
	void setBranchCode(String branchCode);
	String getInitiationStatus();
	void setInitiationStatus(String initiationStatus);
	Integer getMemberAge();
	void setMemberAge(Integer memberAge) ;	
	String getBranchName();
	void setBranchName(String branchName);
	String getBranchSecretary();
	void setBranchSecretary(String branchSecretary);
	String getDistrictId();
	void setDistrictId(String districtId);
	String getDistrictName();
	void setDistrictName(String districtName);
	String getDistrictSuffix();
	void setDistrictSuffix(String districtSuffix);
	String getRegionName();
	void setRegionName(String regionName);
	String getRegionSuffix();
	void setRegionSuffix(String regionSuffix);
	String getRegionId();
	void setRegionId(String regionId);
	void setLetterNoIfJigyasu(String letterNoIfJigyasu);
	String getLetterNoIfJigyasu();
	String getInitiatedParent();
	void setInitiatedParent(String initiatedParent);
	
}