/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.reporting.Report;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class ReportingController extends AbstractController {

	private MasterController masterController;
	private Properties reportTemplates;

	private final String ACTIVITY = "Reports";
	private final String GENERATE_REPORT = "generate";
	private final String LIST_REPORTS = "list";
	private final String VIEW_REPORT = "view";

	/**
	 * @param masterController
	 *            the masterController to set
	 */
	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	/**
	 * @param reportTemplates
	 *            the reportTemplates to set
	 */
	public void setReportTemplates(Properties reportTemplates) {
		this.reportTemplates = reportTemplates;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {

		httpResponse.setHeader("Cache-Control","no-cache");
		httpResponse.setDateHeader ("Expires", 0);

		String uri = httpRequest.getRequestURI();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		IAccount account = (IAccount)httpRequest.getSession().getAttribute(Constants.LOGGED_IN_ACCOUNT); 
		IEvent event = (IEvent)httpRequest.getSession().getAttribute(Constants.SELECTED_EVENT);
						
		List<Object> masterPayload = new ArrayList<Object>();
		masterPayload.add(Constants.EVENT_REQUEST);
		List<Object> eventPayload = new ArrayList<Object>();
		eventPayload.add(Constants.DO_ACTIVITY);
		List<Object> payload = new ArrayList<Object>();
		payload.add(account);
		payload.add(event.getEventId());
		payload.add(ACTIVITY);		
		eventPayload.add(payload);
		masterPayload.add(eventPayload);

		List<Object> params = new ArrayList<Object>();
		List<Object> response = null;
		
		if(uri.endsWith("generate.do")){
			payload.add(GENERATE_REPORT);		
			
			String activityName = httpRequest.getParameter(Constants.ACTIVITY_NAME);
			String reportType = httpRequest.getParameter(Constants.REPORT_TYPE);
			String reportName = httpRequest.getParameter(Constants.REPORT_NAME);
			String reportTemplate = httpRequest.getRealPath("/") + reportTemplates.getProperty(reportType);
			String reportParams = httpRequest.getParameter(Constants.REPORT_PARAMS);
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			
			if(reportParams != null && !reportParams.equals("")){
				StringTokenizer tokenizer = new StringTokenizer(reportParams,";");
				while (tokenizer.hasMoreTokens()) {
					
					String param = tokenizer.nextToken();
					StringTokenizer paramTokenizer = new StringTokenizer(param, ",");
					String name = null;
					String value = null;
					int count = 0;
					
					while(paramTokenizer.hasMoreTokens()){
						count++;
						switch(count){
						case 1: name = paramTokenizer.nextToken();
							break;
						case 2: value = paramTokenizer.nextToken();
							break;
						default:
						}
					}
					paramMap.put(name, value);
				}							
			}
			Report report = new Report();
			report.setActivityName(emptyStringToNull(activityName));
			report.setReportType(emptyStringToNull(reportType));
			report.setReportName(emptyStringToNull(reportName));
						
			params.add(report);
			params.add(reportTemplate);
			params.add(paramMap);
			payload.add(params);
			
			response = masterController.processRequest(masterPayload);		
			Map<String, String> messageMap = (Map<String, String>)response.get(0);
			if(messageMap.get(Constants.ERROR)!=null){
				PrintWriter out= httpResponse.getWriter();
				out.print(messageMap.get(Constants.ERROR));			
			}else if(messageMap.get(Constants.INFO)!= null){
				PrintWriter out= httpResponse.getWriter();
				out.print(messageMap.get(Constants.INFO));	
			}else{
				byte[] reportFile = report.getReportFile();
				httpResponse.setContentType("application/pdf");
				httpResponse.setContentLength(reportFile.length);
				httpResponse.setHeader("Content-Disposition", "inline; filename="+report.getReportName()+".pdf");
				ServletOutputStream servletOutputStream = httpResponse.getOutputStream();

				servletOutputStream.write(reportFile, 0, reportFile.length);
				servletOutputStream.flush();
				servletOutputStream.close();
			}
			
		}else if(uri.endsWith("list.do")){
			payload.add(LIST_REPORTS);			
			
			String activityName = httpRequest.getParameter(Constants.ACTIVITY_NAME);
			String reportType = httpRequest.getParameter(Constants.REPORT_TYPE);
			
			params.add(emptyStringToNull(activityName));
			params.add(emptyStringToNull(reportType));
			payload.add(params);
			
			response = masterController.processRequest(masterPayload);		
			PrintWriter out= httpResponse.getWriter();
			Map<String, String> messageMap = (Map<String, String>)response.get(0);
			if(messageMap.get(Constants.ERROR)!=null){
				out.print(messageMap.get(Constants.ERROR));			
			}else if(messageMap.get(Constants.INFO)!= null){
				out.print(messageMap.get(Constants.INFO));	
			}else{
				List<Report> reports = ((List)((List)response.get(1)).get(0));
				String responseXML = "<root><reports>";
				for(Report eachReport: reports){
					String reportXML = "<report ";
					reportXML += "activityName =\""+eachReport.getActivityName()+"\" ";
					reportXML += "reportType =\""+eachReport.getReportType()+"\" ";
					reportXML += "reportName =\""+eachReport.getReportName()+"\" ";
					reportXML += "createdOn =\""+sdf.format(eachReport.getReportGenerationDate())+"\" ";
					reportXML += "/>";
					responseXML += reportXML;
				}
				responseXML += "</reports></root>";				
				out.print(responseXML);	
			}
		}else if(uri.endsWith("view.do")){
			payload.add(VIEW_REPORT);	
			String reportName = httpRequest.getParameter(Constants.REPORT_NAME);
									
			params.add(emptyStringToNull(reportName));
			payload.add(params);
			
			response = masterController.processRequest(masterPayload);		
			Map<String, String> messageMap = (Map<String, String>)response.get(0);
			if(messageMap.get(Constants.ERROR)!=null){
				PrintWriter out= httpResponse.getWriter();
				out.print(messageMap.get(Constants.ERROR));			
			}else if(messageMap.get(Constants.INFO)!= null){
				PrintWriter out= httpResponse.getWriter();
				out.print(messageMap.get(Constants.INFO));	
			}else{
				byte[] reportFile =(byte[])((List)response.get(1)).get(0);
				httpResponse.setContentType("application/pdf");
				httpResponse.setContentLength(reportFile.length);
				httpResponse.setHeader("Content-Disposition", "inline; filename="+reportName +".pdf");
				ServletOutputStream servletOutputStream = httpResponse.getOutputStream();

				servletOutputStream.write(reportFile, 0, reportFile.length);
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}else{			
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		return null;
	}
		
	private String emptyStringToNull(String emptyString) {
		return emptyString == "" ? null : emptyString;
	}
}
