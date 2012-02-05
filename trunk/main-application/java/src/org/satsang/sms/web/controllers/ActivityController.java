/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class ActivityController extends AbstractController {

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
	protected ModelAndView handleRequestInternal(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) throws Exception {

		httpResponse.setHeader("Cache-Control","no-cache");
		httpResponse.setDateHeader ("Expires", 0);

		String uri = httpRequest.getRequestURI();
		PrintWriter out= httpResponse.getWriter();
		
		IAccount account = (IAccount) httpRequest.getSession().getAttribute(Constants.LOGGED_IN_ACCOUNT);
		IEvent event = (IEvent)httpRequest.getSession().getAttribute(Constants.SELECTED_EVENT);
		
		if(uri.endsWith("list.do")){
			String responseXML = "<root><activities>";
			
			if(account.getEventActivities() != null){
				List<String> activities = account.getEventActivities().get(event.getEventId());
			
				for(String activityName: activities){
					if(account.getAccessibleActivities()!=null){
						IActivity activity = account.getAccessibleActivities().get(activityName);
						if(activity != null && activity.getListOfActions()!=null){
							String activityXML = "<activity label=\""+activityName+"\">";
							Map<String, String> actions = activity.getListOfActions();
							for(String action:actions.keySet()){
								activityXML += "<Method label=\""+actions.get(action)+"\"/>";
							}
							activityXML += "</activity>";
							responseXML += activityXML;
						}
					}
				}	
			}
			responseXML += "</activities></root>";			
			out.print(responseXML);
		}else if(uri.endsWith("start.do")){
			String activityName = httpRequest.getParameter(Constants.ACTIVITY_NAME);
			String description = httpRequest.getParameter(Constants.ACTIVITY_DESCRIPTION);
			
			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.EVENT_REQUEST);

			List<Object> eventPayload = new ArrayList<Object>();
			eventPayload.add(Constants.START_ACTIVITY);
			List<Object> payload = new ArrayList<Object>();
			payload.add(account);
			payload.add(event.getEventId());
			payload.add(activityName);
			payload.add(description);
			
			eventPayload.add(payload);
			masterPayload.add(eventPayload);

			List<Object> response = masterController.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} 
		}else{			
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
				
		return null;
	}

}
