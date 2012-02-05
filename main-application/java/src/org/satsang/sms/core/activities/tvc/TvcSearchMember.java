package org.satsang.sms.core.activities.tvc;

public class TvcSearchMember {

    private String tvcId;
    private String firstName;
    private String lastName;
    private String branchId;

    public TvcSearchMember() {
    }

    public void setTvcId(String tvcId) {
        this.tvcId = tvcId;
    }

    public String getTvcId() {
        return tvcId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
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


}
