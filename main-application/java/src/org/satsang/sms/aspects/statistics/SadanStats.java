/**
 * Radhasoami
 * SadanStats.java, 2010
 */
package org.satsang.sms.aspects.statistics;

/**
 * This holds the statistic data pertaining to a Dayalbagh Sadan. 
 * There are 6 sadans in Dayalbagh. These are:
 * 	- Dormitory
 * 	- New day boarding school
 * 	- Old day boarding school
 * 	- Old exhibition complex
 *  - Pilgrim shed
 *  - Yatri sadan
 *  - Youth hostel
 * 
 * @author Kanika Kaushal
 * @version 1.0
 */
public class SadanStats {

	private Integer capacity;
	private Integer occupancy;
	private Integer maleCount;;
	private Integer femaleCount;
	private Integer childCount;
	private Integer vacancy;
	private String sadanName;
	
	/**
	 * @return the sadanName
	 */
	public String getSadanName() {
		return sadanName;
	}
	/**
	 * @param sadanName the sadanName to set
	 */
	public void setSadanName(String sadanName) {
		this.sadanName = sadanName;
	}
	/**
	 * @return the capacity
	 */
	public Integer getCapacity() {
		return capacity;
	}
	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	/**
	 * @return the occupancy
	 */
	public Integer getOccupancy() {
		return occupancy;
	}
	/**
	 * @param occupancy the occupancy to set
	 */
	public void setOccupancy(Integer occupancy) {
		this.occupancy = occupancy;
	}
	/**
	 * @return the maleCount
	 */
	public Integer getMaleCount() {
		return maleCount;
	}
	/**
	 * @param maleCount the maleCount to set
	 */
	public void setMaleCount(Integer maleCount) {
		this.maleCount = maleCount;
	}
	/**
	 * @return the femaleCount
	 */
	public Integer getFemaleCount() {
		return femaleCount;
	}
	/**
	 * @param femaleCount the femaleCount to set
	 */
	public void setFemaleCount(Integer femaleCount) {
		this.femaleCount = femaleCount;
	}
	/**
	 * @return the childCount
	 */
	public Integer getChildCount() {
		return childCount;
	}
	/**
	 * @param childCount the childCount to set
	 */
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
	/**
	 * @return the vacancy
	 */
	public Integer getVacancy() {
		return vacancy;
	}
	/**
	 * @param vacancy the vacancy to set
	 */
	public void setVacancy(Integer vacancy) {
		this.vacancy = vacancy;
	}
}
