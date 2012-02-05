/**
 * 
 */
package org.satsang.sms.core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.controllers.interfaces.IController;
import org.satsang.sms.core.util.Constants;

/**
 * @author gcapriha
 * 
 */
public class MasterController implements IController {

	private CommunityController aHandleToCommunityController;
	private EventController aHandleToEventController;
	private MemberController aHandleToMemberController;
	private AccountController aHandleToAccountController;

	private List<String> masterRequests = Arrays.asList(new String[] {
			Constants.ACCOUNT_REQUEST, Constants.COMMUNITY_REQUEST,
			Constants.MEMBER_REQUEST, Constants.EVENT_REQUEST});

	/*
	 * These are the setters used by Spring to set references to the below!
	 */
	public void setRefToCommunityController(CommunityController aRef) {
		this.aHandleToCommunityController = aRef;
	}

	public void setRefToEventController(EventController aRef) {
		this.aHandleToEventController = aRef;
	}

	public void setRefToMemberController(MemberController aRef) {
		this.aHandleToMemberController = aRef;
	}

	public void setRefToAccountController(AccountController aRef) {
		this.aHandleToAccountController = aRef;
	}

	@SuppressWarnings("unchecked")
	public List<Object> processRequest(List<Object> payload) {

		// Create a default response object
		List<Object> response = new ArrayList<Object>();
		Map<String, String> messageMap = defaultReturnMap();
		response.add(messageMap);

		// Check payload
		if (payload == null) {
			messageMap.put(Constants.ERROR, "Payload undefined");
			return response;
		}

		Object request = payload.get(0);

		// Parse Request
		if (request == null) {
			messageMap.put(Constants.ERROR, "Request undefined");
			return response;
		}

		if (!masterRequests.contains(request)) {
			messageMap.put(Constants.ERROR, "Unknown request");
			return response;
		}
		
		List<Object> receivedResponse = null;

		// Delegate request to appropriate controller
		if (request.equals(Constants.ACCOUNT_REQUEST)) {
			receivedResponse = aHandleToAccountController
					.processRequest(payload.get(1) != null ? (List) payload
							.get(1) : null);
		} else if (request.equals(Constants.EVENT_REQUEST)) {
			receivedResponse = aHandleToEventController.processRequest(payload
					.get(1) != null ? (List) payload.get(1) : null);
		} else if (request.equals(Constants.COMMUNITY_REQUEST)) {
			 receivedResponse =  aHandleToCommunityController.processRequest(payload
						.get(1) != null ? (List) payload.get(1) : null);
		} else if (request.equals(Constants.MEMBER_REQUEST)) {
			receivedResponse =  aHandleToMemberController.processRequest(payload
					.get(1) != null ? (List) payload.get(1) : null);
		} else {
			messageMap.put(Constants.ERROR, "Unknown Request");
			return response;
		}

		messageMap.putAll((Map) receivedResponse.get(0));
		if (receivedResponse.size() > 1)
			response.add(receivedResponse.get(1));
				
		return response;
	}

	public Map<String, String> defaultReturnMap() {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(Constants.ERROR, null);
		returnMap.put(Constants.INFO, null);
		returnMap.put(Constants.DEBUG, null);
		return returnMap;
	}

}
