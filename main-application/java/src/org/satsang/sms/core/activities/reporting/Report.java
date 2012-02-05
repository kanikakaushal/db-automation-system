/**
 * 
 */
package org.satsang.sms.core.activities.reporting;

import java.util.Date;

/**
 * @author Default
 *
 */
public class Report {
	private String activityName;
	private String reportName;
	private String reportType;
	private Date reportGenerationDate;
	private byte[] reportFile;
	
	/**
	 * @return the activityName
	 */
	public String getActivityName() {
		return activityName;
	}
	/**
	 * @param activityName the activityName to set
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	/**
	 * @return the reportName
	 */
	public String getReportName() {
		return reportName;
	}
	/**
	 * @param reportName the reportName to set
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	/**
	 * @return the reportGenerationDate
	 */
	public Date getReportGenerationDate() {
		return reportGenerationDate;
	}
	/**
	 * @param reportGenerationDate the reportGenerationDate to set
	 */
	public void setReportGenerationDate(Date reportGenerationDate) {
		this.reportGenerationDate = reportGenerationDate;
	}
	/**
	 * @return the reportFile
	 */
	public byte[] getReportFile() {
		return reportFile;
	}
	/**
	 * @param reportFile the reportFile to set
	 */
	public void setReportFile(byte[] reportFile) {
		this.reportFile = reportFile;
	}
	
}
