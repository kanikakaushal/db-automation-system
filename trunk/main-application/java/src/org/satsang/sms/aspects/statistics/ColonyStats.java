/**
 * Radhasoami
 * ColonyStats.java, 2010
 */
package org.satsang.sms.aspects.statistics;

/**
 * This holds the statistic data pertaining to a Dayalbagh Nagar.
 * 
 * @author Kanika Kaushal
 * @version 1.0
 */
public class ColonyStats {
	
	private String colonyName;
	private int totalOccupancy;
	private int maleCount;
	private int femaleCount;
	private int childCount;

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
	 * @return the totalOccupancy
	 */
	public int getTotalOccupancy() {
		return totalOccupancy;
	}

	/**
	 * @param totalOccupancy
	 *            the totalOccupancy to set
	 */
	public void setTotalOccupancy(int totalOccupancy) {
		this.totalOccupancy = totalOccupancy;
	}

	/**
	 * @return the maleCount
	 */
	public int getMaleCount() {
		return maleCount;
	}

	/**
	 * @param maleCount
	 *            the maleCount to set
	 */
	public void setMaleCount(int maleCount) {
		this.maleCount = maleCount;
	}

	/**
	 * @return the femaleCount
	 */
	public int getFemaleCount() {
		return femaleCount;
	}

	/**
	 * @param femaleCount
	 *            the femaleCount to set
	 */
	public void setFemaleCount(int femaleCount) {
		this.femaleCount = femaleCount;
	}

	/**
	 * @return the childCount
	 */
	public int getChildCount() {
		return childCount;
	}

	/**
	 * @param childCount
	 *            the childCount to set
	 */
	public void setChildCount(int childCount) {
		this.childCount = childCount;
	}
}
