package org.satsang.sms.core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.account.interfaces.IAccountHandler;
import org.satsang.sms.core.controllers.interfaces.IController;
import org.satsang.sms.core.util.Constants;
import org.satsang.sms.core.util.RequestInvoker;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author gcapriha
 * 
 */
public class AccountController implements IController, ApplicationContextAware {

	private IAccountHandler accountHandler;
	private HashMap<String, IAccount> listOfActiveAccounts = new HashMap<String, IAccount>();
	private ApplicationContext context;

	private List<String> accountRequests = Arrays.asList(new String[] {
			Constants.CREATE_ACCOUNT, Constants.LOGIN_ACCOUNT,
			Constants.LOGOUT_ACCOUNT, Constants.UPDATE_ACCOUNT,
			Constants.DISABLE_ACCOUNT });

	/**
	 * @return the listOfActiveAccounts
	 */
	public HashMap<String, IAccount> getListOfActiveAccounts() {
		return listOfActiveAccounts;
	}

	/* Spring Setters */
	/**
	 * @param accountHandler
	 *            the accountHandler to set
	 */
	public void setAccountHandler(IAccountHandler accountHandler) {
		this.accountHandler = accountHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<Object> processRequest(List<Object> payload) {

		// Create a default response object
		List<Object> response = new ArrayList<Object>();
		Map<String, String> messageMap = defaultReturnMap();
		response.add(messageMap);

		// Check payload
		if (payload == null) {
			messageMap.put(Constants.ERROR, "Account payload undefined");
			return response;
		}

		Object request = payload.get(0);

		// Parse Request
		if (request == null) {
			messageMap.put(Constants.ERROR, "Account request undefined");
			return response;
		}

		if (!accountRequests.contains(request)) {
			messageMap.put(Constants.ERROR, "Unknown Account request");
			return response;
		}

		List<Object> accountPayload = payload.get(1) != null ? (List) payload.get(1) : null;
		Object receivedResponse = null;

		try {

			if (request.toString().equals(Constants.LOGOUT_ACCOUNT)) {
				if (accountPayload == null || accountPayload.get(0) == null) {
					messageMap.put(Constants.ERROR, "Account Id not specified.");
					return response;
				}
				String accountId = (String) accountPayload.get(0);
				if (!listOfActiveAccounts.containsKey(accountId)) {
					messageMap.put(Constants.ERROR, "Account already logged out.");
					return response;
				}
				
				((EventController) context.getBean(Constants.EVENT_CONTROLLER))
						.deregisterAccount(accountId, listOfActiveAccounts.get(accountId));
				
				listOfActiveAccounts.remove(accountId);
				messageMap.put(Constants.INFO, "You have been logged off.");
				return response;
			}else if(request.toString().equals(Constants.LOGIN_ACCOUNT)){
				if (accountPayload == null || accountPayload.get(0) == null) {
					messageMap
							.put(Constants.ERROR, "Account Id not specified.");
					return response;
				}
				String accountId = (String) accountPayload.get(0);
				if(listOfActiveAccounts.containsKey(accountId)){
					response.add(listOfActiveAccounts.get(accountId));
					return response;
				}
			}
			receivedResponse = RequestInvoker.invoke(accountHandler, request
					.toString(), accountPayload);

			if (request.toString().equals(Constants.LOGIN_ACCOUNT)) {
				if (receivedResponse == null) {
					messageMap.put(Constants.ERROR,
							"Account is disabled or does not exist.");
					return response;
				}

				listOfActiveAccounts.put(((IAccount) receivedResponse)
						.getAccountId(), (IAccount) receivedResponse);
				((EventController) context.getBean(Constants.EVENT_CONTROLLER))
						.registerNewAccount(((IAccount) receivedResponse)
								.getAccountId());
			}
		} catch (Exception e) {
			e.printStackTrace();
			messageMap.put(Constants.ERROR, "Inappropriate Account payload");
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
