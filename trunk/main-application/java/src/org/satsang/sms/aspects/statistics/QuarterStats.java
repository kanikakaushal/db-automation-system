/**
 * Radhasoami
 * QuarterStats.java, 2010
 */
package org.satsang.sms.aspects.statistics;

/**
 * This holds the statistic data pertaining to a Nagar quarter.
 * 
 * @author Kanika Kaushal
 * @version 1.0
 */
public class QuarterStats {

	private String colonyName;
	private String quarterNo;
	private Integer quarterVacancy;
	private Integer quarterCapacity;
	private Integer quarterOccupancy;

	/**
	 * @return the colonyName
	 */
	public String getColonyName() {
		return colonyName;
	}

	/**
	 * @param colonyName
	 *            the colonyName to set
	 */
	public void setColonyName(String colonyName) {
		this.colonyName = colonyName;
	}

	/**
	 * @return the quarterNo
	 */
	public String getQuarterNo() {
		return quarterNo;
	}

	/**
	 * @param quarterNo
	 *            the quarterNo to set
	 */
	public void setQuarterNo(String quarterNo) {
		this.quarterNo = quarterNo;
	}

	/**
	 * @return the quarterVacancy
	 */
	public Integer getQuarterVacancy() {
		return quarterVacancy;
	}

	/**
	 * @param quarterVacancy
	 *            the quarterVacancy to set
	 */
	public void setQuarterVacancy(Integer quarterVacancy) {
		this.quarterVacancy = quarterVacancy;
	}

	/**
	 * @return the quarterCapacity
	 */
	public Integer getQuarterCapacity() {
		return quarterCapacity;
	}

	/**
	 * @param quarterCapacity
	 *            the quarterCapacity to set
	 */
	public void setQuarterCapacity(Integer quarterCapacity) {
		this.quarterCapacity = quarterCapacity;
	}

	/**
	 * @return the quarterOccupancy
	 */
	public Integer getQuarterOccupancy() {
		return quarterOccupancy;
	}

	/**
	 * @param quarterOccupancy
	 *            the quarterOccupancy to set
	 */
	public void setQuarterOccupancy(Integer quarterOccupancy) {
		this.quarterOccupancy = quarterOccupancy;
	}
}
