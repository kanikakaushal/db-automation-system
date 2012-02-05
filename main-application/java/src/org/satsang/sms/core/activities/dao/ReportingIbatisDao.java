/**
 * 
 */
package org.satsang.sms.core.activities.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.satsang.sms.core.activities.interfaces.IReportingDao;
import org.satsang.sms.core.activities.reporting.Report;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Default
 * 
 */
public class ReportingIbatisDao implements IReportingDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	public byte[] generateReport(String eventId, String accountId, Report report,
			String reportTemplate, Map<String, Object> parameters) {

		byte[] reportPdf = null;
		Connection jdbcConnection = null;
		try {
			jdbcConnection = sqlMapClientTemplate.getDataSource().getConnection();			
			JasperPrint print = JasperFillManager.fillReport(reportTemplate, parameters, jdbcConnection);

			reportPdf = JasperExportManager.exportReportToPdf(print);
			report.setReportFile(reportPdf);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("eventId", eventId);
			params.put("accountId", accountId);
			params.put("report", report);
			sqlMapClientTemplate.insert("Reporting.storeReport", params);

		} catch (JRException e) {			
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (jdbcConnection != null) {
				try {
					// Close to prevent database connection exhaustion
					jdbcConnection.close();
				} catch (SQLException ex) {
					System.out.println("Exception while closing connection...");
				}
			}
		}

		return reportPdf;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Report> getReports(String eventId, String activityName,
			String reportType) {
		String whereClause = "WHERE EVENT_ID = '" + eventId + "'";

		if (activityName != null && !activityName.equals("")) {
			whereClause += " AND ACTIVITY_NAME = '" + activityName + "'";
		}
		if (reportType != null && !reportType.equals("")) {
			whereClause += " AND REPORT_TYPE = '" + reportType + "'";
		}

		List<Report> reports = sqlMapClientTemplate.queryForList("Reporting.getReports", whereClause);
		return reports;
	}

	@Override
	public byte[] viewReport(String reportName) {		
		Report reportFile = (Report) sqlMapClientTemplate.queryForObject("Reporting.viewReport", reportName);
		return reportFile.getReportFile();
	}
}
