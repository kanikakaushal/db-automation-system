package org.satsang.sms.core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.controllers.interfaces.IController;
import org.satsang.sms.core.member.CheckDigit;
import org.satsang.sms.core.member.interfaces.IMemberHandler;
import org.satsang.sms.core.util.Constants;
import org.satsang.sms.core.util.RequestInvoker;

/**
 * @author gcapriha
 * 
 */
public class MemberController implements IController {
	
	private IMemberHandler memberHandler;

	private List<String> memberRequests = Arrays.asList(new String[] {
			Constants.MEMBER_DETAILS_REQUEST});

	public void setMemberHandler(IMemberHandler memberHandler) {
		this.memberHandler = memberHandler;
	}

	@SuppressWarnings("unchecked")
	public List<Object> processRequest(List<Object> payload) {

		// Create a default response object
		List<Object> response = new ArrayList<Object>();
		Map<String, String> messageMap = defaultReturnMap();
		response.add(messageMap);

		// Check payload
		if (payload == null) {
			messageMap.put(Constants.ERROR, "Member payload undefined");
			return response;
		}

		Object request = payload.get(0);

		// Parse Request
		if (request == null) {
			messageMap.put(Constants.ERROR, "Member request undefined");
			return response;
		}

		if (!memberRequests.contains(request)) {
			messageMap.put(Constants.ERROR, "Unknown Member request");
			return response;
		}

		List<Object> communityPayload = payload.size() == 1 ? null
				: (List) payload.get(1);
		Object receivedResponse = null;
		
		/**********Check digit validation  - Removed temporarily 
		if(request.equals(Constants.MEMBER_DETAILS_REQUEST)){
			long memberId = Long.valueOf(communityPayload.get(0).toString());
			if(!CheckDigit.validateCode(memberId, 13)){
				messageMap.put(Constants.ERROR, "The member id is not valid according to the standard barcode check digit scheme EAN-13.");
				return response;
			}
		}*/
		
		try {
			receivedResponse = RequestInvoker.invoke(memberHandler, request
					.toString(), communityPayload);
		} catch (Exception e) {
			messageMap.put(Constants.ERROR, "Inappropriate Member payload");
			messageMap.put(Constants.DEBUG, e.getMessage());
			return response;
		}
		if (receivedResponse != null)
			response.add(receivedResponse);
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
