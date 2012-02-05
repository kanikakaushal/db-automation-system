/**
 * 
 */
package org.satsang.sms.core.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.satsang.sms.core.activities.interfaces.IActionHandler;
import org.satsang.sms.core.activities.interfaces.IReportingDao;
import org.satsang.sms.core.activities.reporting.Report;

/**
 * @author Default
 * 
 */
public class ReportingActionHandler implements IActionHandler {

	private IReportingDao reportingDao;
	private HashMap<String, Object> configParams;
	private static Logger log = Logger.getLogger(ReportingActionHandler.class);
	
	@Override
	public HashMap<String, Object> getConfigParams() {
		return this.configParams;
	}

	@Override
	public void setConfigParams(HashMap<String, Object> configParams) {
		this.configParams = configParams;
	}

	public void setReportingDao(IReportingDao reportingDao) {
		this.reportingDao = reportingDao;
	}

	public void generate(Report newReport, String reportTemplate, Map<String, Object> parameters) {
		newReport.setReportGenerationDate(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		newReport.setReportName(newReport.getReportName() + " " + sdf.format(new Date()));
		reportingDao.generateReport((String) configParams.get("eventId"), (String) configParams.get("accountId"),
				newReport, reportTemplate, parameters);
		log.info(newReport.getReportName() + "report generated.");		
	}

	public List<Report> list(String activityName, String reportType) {
		return reportingDao.getReports((String) configParams.get("eventId"), activityName, reportType);
	}
	
	public byte[] view(String reportName) {
		return reportingDao.viewReport(reportName);
	}
}
