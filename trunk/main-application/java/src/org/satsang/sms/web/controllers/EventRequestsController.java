/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
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
public class EventRequestsController extends AbstractController {

	private MasterController masterController;

	/**
	 * @param masterController
	 *            the masterController to set
	 */
	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal
	 * (javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {
		httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		String uri = httpRequest.getRequestURI();
		PrintWriter out = httpResponse.getWriter();
		IAccount account = (IAccount) httpRequest.getSession().getAttribute(
				Constants.LOGGED_IN_ACCOUNT);

		if (uri.endsWith("select.do")) {
			String eventId = httpRequest.getParameter(Constants.EVENT_ID);
			IEvent event = account.getMyActiveEvents().get(eventId);
			httpRequest.getSession().setAttribute(Constants.SELECTED_EVENT,
					event);
			String responseXML = "<root><eventSelection>";
			responseXML += "<status message=\"Default event set to "+eventId+"\"/>";
			responseXML += "</eventSelection></root>";			
			out.print(responseXML);		
			
		} else if (uri.endsWith("create.do")) {
			String communityId = httpRequest
					.getParameter(Constants.COMMUNITY_ID);
			String contextName = httpRequest
					.getParameter(Constants.CONTEXT_NAME);
			String bhandaraId = httpRequest.getParameter(Constants.BHANDARA_ID);
			String eventName = httpRequest.getParameter(Constants.EVENT_NAME);
			String eventDesc = httpRequest.getParameter(Constants.EVENT_DESC);

			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.CREATE_EVENT);
			List<Object> payload = new ArrayList<Object>();
			payload.add(account);
			payload.add(account.getCommunityMap().get(communityId));
			payload.add(contextName);
			payload.add(eventName);
			payload.add(eventDesc);
			payload.add(bhandaraId);

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
				IEvent event = (IEvent) response.get(1);
				String responseXML = "<root>";
				responseXML += "<event id=\""+event.getEventId()+"\" name=\""+event.getEventName()+"\" " +
						"community=\""+event.getCommunityName()+"\" context=\""+event.getContextName()+"\" " +
								"status=\"Success\" details=\"Created Successfully\"/>";
				responseXML += "</root>";
				out.print(responseXML);
			}
		}else if (uri.endsWith("list.do")) {
			Map<String, IEvent> activeEvents = account.getMyActiveEvents();
			Iterator<String> eventIter = activeEvents.keySet().iterator();
			
			String responseXML = "<root><events>";			
			while(eventIter.hasNext()){
				String eventId = eventIter.next();
				IEvent event = activeEvents.get(eventId);
				responseXML += "<event id=\""+eventId+"\" name=\""+event.getEventName()+"\" createdBy=\""+event.getOwner()+"\""+ 
						" community=\""+event.getCommunityName()+"\" context=\""+event.getContextName()+"\" " +
								"fromDate=\""+(event.getEventStartDate() == null ? "" : event.getEventStartDate())+"\" " +
										"toDate=\""+(event.getEventEndDate() == null ? "" : event.getEventEndDate())+"\" status=\"Active\"/>";
			}
			responseXML += "</events></root>";
			out.print(responseXML);
		}else {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

		return null;
	}

}
