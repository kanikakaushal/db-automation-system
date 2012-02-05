/**
 * 
 */
package org.satsang.sms.core.activities.interfaces;

import java.util.List;
import java.util.Map;

import org.satsang.sms.core.activities.reporting.Report;

/**
 * @author Default
 * 
 */
public interface IReportingDao {

	public byte[] generateReport(String eventId, String accountId, Report report,
			String jasperFile, Map<String, Object> parameters);

	public List<Report> getReports(String eventId, String activityName,
			String reportType);

	public byte[] viewReport(String reportName);
}
