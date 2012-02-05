/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class ManageEventsController extends AbstractController {

	private MasterController masterController;
	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	
	/**
	 * @param masterController
	 *            the masterController to set
	 */
	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {
		httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		String uri = httpRequest.getRequestURI();
		PrintWriter out = httpResponse.getWriter();
		IAccount account = (IAccount) httpRequest.getSession().getAttribute(Constants.LOGGED_IN_ACCOUNT);
				
		if (uri.endsWith("list.do")) {
			String responseXML = "<root><events>";

			Map<String, IEvent> eventsToActivate = account.getEventsToActivate();
			Iterator<String> eventIter = eventsToActivate.keySet().iterator();

			while (eventIter.hasNext()) {
				String eventId = eventIter.next();
				IEvent event = eventsToActivate.get(eventId);
				responseXML += "<event id=\""
						+ eventId
						+ "\" name=\""
						+ event.getEventName()
						+ "\" createdBy=\""
						+ event.getOwner()
						+ "\""
						+ " community=\""
						+ event.getCommunityName()
						+ "\" context=\""
						+ event.getContextName()
						+ "\" "
						+ "fromDate=\""
						+ (event.getEventStartDate() == null ? "" : event
								.getEventStartDate())
						+ "\" toDate=\""
						+ (event.getEventEndDate() == null ? "" : event
								.getEventEndDate())
						+ "\" status=\"Inactive\"/>";
			}

			Map<String, IEvent> eventsToDeactivate = account.getEventsToDeactivate();
			eventIter = eventsToDeactivate.keySet().iterator();

			while (eventIter.hasNext()) {
				String eventId = eventIter.next();
				IEvent event = eventsToDeactivate.get(eventId);
				responseXML += "<event id=\""
						+ eventId
						+ "\" name=\""
						+ event.getEventName()
						+ "\" createdBy=\""
						+ event.getOwner()
						+ "\""
						+ " community=\""
						+ event.getCommunityName()
						+ "\" context=\""
						+ event.getContextName()
						+ "\" "
						+ "fromDate=\""
						+ (event.getEventStartDate() == null ? "" : event
								.getEventStartDate())
						+ "\" toDate=\""
						+ (event.getEventEndDate() == null ? "" : event
								.getEventEndDate()) + "\" status=\"Active\"/>";
			}

			responseXML += "</events></root>";
			out.print(responseXML);
		} else if (uri.endsWith("reactivate.do")) {
			String eventId = httpRequest.getParameter(Constants.EVENT_ID);

			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.REACTIVATE_EVENT);
			List<Object> payload = new ArrayList<Object>();
			payload.add(eventId);
			payload.add(account.getAccountId());

			eventPayload.add(payload);
			masterPayload.add(eventPayload);

			List<Object> response = masterController.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print("<root><error>" + messageMap.get(Constants.ERROR)
						+ "</error></root>");
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print("<root><info>" + messageMap.get(Constants.INFO)
						+ "</info></root>");
			} else {
				out.print("<root><message>Event reactivated successfully!!</message></root>");
			}
		}else if (uri.endsWith("deactivate.do")) {
			String eventId = httpRequest.getParameter(Constants.EVENT_ID);

			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.DISABLE_EVENT);
			List<Object> payload = new ArrayList<Object>();
			payload.add(eventId);

			eventPayload.add(payload);
			masterPayload.add(eventPayload);

			List<Object> response = masterController.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print("<root><error>" + messageMap.get(Constants.ERROR)
						+ "</error></root>");
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print("<root><info>" + messageMap.get(Constants.INFO)
						+ "</info></root>");
			} else {
				out.print("<root><message>Event deactivated successfully!!</message></root>");
			}
		} else if (uri.endsWith("activate.do")) {
			
			String eventId = httpRequest.getParameter(Constants.EVENT_ID);

			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.ENABLE_EVENT);
			List<Object> payload = new ArrayList<Object>();
			payload.add(eventId);

			eventPayload.add(payload);
			masterPayload.add(eventPayload);

			List<Object> response = masterController.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print("<root><error>" + messageMap.get(Constants.ERROR)
						+ "</error></root>");
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print("<root><info>" + messageMap.get(Constants.INFO)
						+ "</info></root>");
			} else {
				out.print("<root><message>Event activated successfully!!</message></root>");
			}
		} else if (uri.endsWith("search.do")) {
			String fromDateString = httpRequest.getParameter(Constants.SEARCH_FROM_DATE);
			String toDateString = httpRequest.getParameter(Constants.SEARCH_TO_DATE);
			
			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.SEARCH_EVENTS);
			List<Object> payload = new ArrayList<Object>();
			payload.add(account.getAccountId());
			payload.add(fromDateString == null ? null : formatter.parse(fromDateString));
			payload.add(toDateString == null || toDateString.equals("")? null : formatter.parse(toDateString));
			
			eventPayload.add(payload);
			masterPayload.add(eventPayload);

			List<Object> response = masterController
					.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response
					.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<IEvent> searchedEvents = (List)response.get(1);
				String responseXML = "<root><events>";
				for(IEvent event: searchedEvents){
					responseXML += "<event id=\""
						+ event.getEventId()
						+ "\" name=\""
						+ event.getEventName()
						+ "\" createdBy=\""
						+ event.getOwner()
						+ "\""
						+ " community=\""
						+ event.getCommunityName()
						+ "\" context=\""
						+ event.getContextName()
						+ "\" "
						+ "fromDate=\""
						+ (event.getEventStartDate() == null ? "" : event
								.getEventStartDate())
						+ "\" toDate=\""
						+ (event.getEventEndDate() == null ? "" : event
								.getEventEndDate()) + "\" status=\"Terminated\"/>";
				}
				responseXML += "</events></root>";
				out.print(responseXML);
			}
		} else {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return null;
	}

}
