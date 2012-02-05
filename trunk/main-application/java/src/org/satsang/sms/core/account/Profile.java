/**
 * 
 */
package org.satsang.sms.core.account;

/**
 * @author Default
 *
 */
public class Profile {
	private int profileId;
	private String profileName;
	private String description;
	
	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return profileId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.profileId = roleId;
	}
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return profileName;
	}
	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.profileName = roleName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
}
