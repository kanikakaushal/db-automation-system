/**
 * 
 */
package org.satsang.sms.core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.event.interfaces.IEventHandler;
import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.context.interfaces.IContext;
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
public class EventController implements IController, ApplicationContextAware {

	/*
	 * This HashMap is going to store a list of Active Events
	 */
	private Map<String, IEvent> listOfActiveEvents;
	private Map<String, IEvent> listOfAliveEvents;
	private Map<String, String> listOfActiveEventOwners;
	private Map<String, List<String>> listOfAccountsAccessingActiveEvents;

	private IEventHandler eventHandler;
	private List<String> eventRequests;
	private ApplicationContext context;

	@SuppressWarnings("unchecked")
	public void init() {

		List<Object> activeEvents = eventHandler.getActiveEvents();
		if(activeEvents!= null){
			listOfActiveEventOwners = (Map<String, String>) activeEvents.get(1);
			listOfActiveEvents = (Map<String, IEvent>) activeEvents.get(0);
			listOfAliveEvents = (Map<String, IEvent>) activeEvents.get(2);
			listOfAccountsAccessingActiveEvents = new HashMap<String, List<String>>();
			Iterator<String> eventIterator = listOfActiveEvents.keySet().iterator();
			while (eventIterator.hasNext()) {
				listOfAccountsAccessingActiveEvents.put(eventIterator.next(), null);
			}
		}	

		eventRequests = Arrays.asList(new String[] { Constants.CREATE_EVENT,
				Constants.START_ACTIVITY, Constants.DO_ACTIVITY,
				Constants.END_ACTIVITY, Constants.ENABLE_EVENT,
				Constants.DISABLE_EVENT, Constants.SEARCH_EVENTS, Constants.REACTIVATE_EVENT });

	}

	public void registerNewAccount(String accountId) {
		AccountController ac = (AccountController) context
				.getBean(Constants.ACCOUNT_CONTROLLER);
		IAccount account = ac.getListOfActiveAccounts().get(accountId);
		account.setMyActiveEvents(new HashMap<String, IEvent>());
		account.setMyOwnedEvents(new HashMap<String, IEvent>());
		account.setMyOpenActivities(eventHandler
				.getAccountOpenActivities(accountId));
		account.setActivityEvents(new HashMap<String, List<IEvent>>());

		account.setEventActivities(new HashMap<String, List<String>>());

		Iterator<String> eventIterator = listOfAliveEvents.keySet().iterator();
		while (eventIterator.hasNext()) {
			String eventId = eventIterator.next();
			IEvent event = listOfAliveEvents.get(eventId);
			if (listOfActiveEventOwners.get(eventId).equals(accountId)) {
				account.getMyOwnedEvents().put(eventId, event);
			}
		}

		eventIterator = listOfActiveEvents.keySet().iterator();
		while (eventIterator.hasNext()) {
			String eventId = eventIterator.next();
			IEvent event = listOfActiveEvents.get(eventId);
			
			if (isEventAccessible(account, event)) {
				account.getMyActiveEvents().put(eventId, event);
				List<String> accountList = listOfAccountsAccessingActiveEvents
						.get(eventId);
				
				if (accountList == null) {
					listOfAccountsAccessingActiveEvents.put(eventId,
							new ArrayList<String>());
					accountList = listOfAccountsAccessingActiveEvents
							.get(eventId);
				}
				if (!accountList.contains(accountId))
					accountList.add(accountId);
			}
			
			
		}
		
		
		for (Iterator<Map.Entry<String, List<String>>> eventIter = account
				.getMyOpenActivities().entrySet().iterator(); eventIter
				.hasNext();) {
			Map.Entry<String, List<String>> entry = eventIter.next();
			String eventId = entry.getKey();
			if (!account.getMyActiveEvents().containsKey(eventId)) {
				eventIter.remove();
			}
		}
	}

	public void deregisterAccount(String accountId, IAccount account) {
		if(listOfAccountsAccessingActiveEvents != null){
			Iterator<String> eventIter = listOfAccountsAccessingActiveEvents.keySet().iterator();
			while (eventIter.hasNext()) {
				String eventId = eventIter.next();
				if (listOfAccountsAccessingActiveEvents.get(eventId) != null
						&& listOfAccountsAccessingActiveEvents.get(eventId).contains(accountId)) {
					List<String> runningActivities = account.getMyOpenActivities() != null ? account.getMyOpenActivities().get(eventId) : null;
					if(runningActivities!=null){
						List<String> activityTemp = new ArrayList<String>();
						for (String activityName : runningActivities) {
							activityTemp.add(activityName);
						}
						for (String activityName : activityTemp) {
							
							List<Object> eventPayload = new ArrayList<Object>();
							eventPayload.add(Constants.END_ACTIVITY);
							List<Object> payload = new ArrayList<Object>();
							payload.add(account);
							payload.add(eventId);
							payload.add(activityName);
							eventPayload.add(payload);
							processRequest(eventPayload);
						}
					}
					listOfAccountsAccessingActiveEvents.get(eventId).remove(accountId);
				}
			}
		}
	}

	private boolean isEventAccessible(IAccount account, IEvent event) {
		String communityId = event.getCommunityID();
		Map<String, List<IEvent>> activityEvents = account.getActivityEvents();
		
		Map<String, List<String>> eventActivities = account
				.getEventActivities();

		if (account.getCommunityMap().containsKey(communityId)) {
			ICommunity community = account.getCommunityMap().get(communityId);
			if (community.getContextMap().containsKey(event.getContextName())) {
				IContext context = community.getContextMap().get(
						event.getContextName());
				HashMap<String, IActivity> activityMap = context
						.getActivityMap();
				Iterator<String> activityIter = activityMap.keySet().iterator();
				List<String> accessibleActivities = new ArrayList<String>();
				eventActivities.put(event.getEventId(), accessibleActivities);

				while (activityIter.hasNext()) {
					String activityName = activityIter.next();
					if (activityEvents.get(activityName) == null) {
						activityEvents.put(activityName,
								new ArrayList<IEvent>());						
					}
					accessibleActivities.add(activityName);
					activityEvents.get(activityName).add(event);
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * @param eventHandler
	 *            the eventHandler to set
	 */
	public void setEventHandler(IEventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}

	@SuppressWarnings("unchecked")
	public synchronized List<Object> processRequest(List<Object> payload) {

		// Create a default response object
		List<Object> response = new ArrayList<Object>();
		Map<String, String> messageMap = defaultReturnMap();
		response.add(messageMap);

		// Check payload
		if (payload == null) {
			messageMap.put(Constants.ERROR, "Event payload undefined");
			return response;
		}

		Object request = payload.get(0);

		// Parse Request
		if (request == null) {
			messageMap.put(Constants.ERROR, "Event request undefined");
			return response;
		}

		if (!eventRequests.contains(request)) {
			messageMap.put(Constants.ERROR, "Unknown Event request");
			return response;
		}

		List<Object> eventPayload = payload.get(1) != null ? (List) payload.get(1) : null;
		Object receivedResponse = null;

		try {
			if (preprocessor(request.toString(), eventPayload, messageMap) == false)
				return response;

			receivedResponse = RequestInvoker.invoke(eventHandler, request.toString(), eventPayload);

			postprocessor(request.toString(), eventPayload, receivedResponse, messageMap);

		} catch (Exception e) {
			messageMap.put(Constants.ERROR, "Inappropriate Event payload");
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

	public boolean preprocessor(String request, List<Object> eventPayload,
			Map<String, String> messageMap) {
		if (request.equals(Constants.CREATE_EVENT)) {
			if (eventPayload == null) {
				messageMap.put(Constants.ERROR, "Event payload not specified.");
				return false;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			if (account == null) {
				messageMap.put(Constants.ERROR, "Account not specified");
				return false;
			}
			ICommunity community = (ICommunity) eventPayload.get(1);
			if (community == null) {
				messageMap.put(Constants.ERROR, "Community not specified");
				return false;
			}
			String contextName = (String) eventPayload.get(2);
			if (contextName == null) {
				messageMap.put(Constants.ERROR, "Context name not specified");
				return false;
			}
			if (!account.getCommunityMap().containsKey(
					community.getInstanceID())) {
				messageMap
						.put(Constants.ERROR,
								"This account does not have access to create event on the community");
				return false;
			}
			if (account.getCommunityMap().get(community.getInstanceID())
					.getHandleToContext(contextName) == null) {
				messageMap
						.put(Constants.ERROR,
								"This account does not have access to create event on the context");
				return false;
			}
		} else if (request.equals(Constants.ENABLE_EVENT)) {
			if (eventPayload == null || eventPayload.get(0) == null) {
				messageMap.put(Constants.ERROR, "Event id not specified.");
				return false;
			}

			String eventId = (String) eventPayload.get(0);
			//FIXME
			if ((listOfActiveEvents!= null && listOfActiveEvents.containsKey(eventId))
					|| (listOfAliveEvents == null || !listOfAliveEvents.containsKey(eventId))) {
				messageMap.put(Constants.ERROR,	"Event can not be activated. It may be already active.");
				return false;
			}
		}else if (request.equals(Constants.REACTIVATE_EVENT)) {
			if (eventPayload == null || eventPayload.get(0) == null) {
				messageMap.put(Constants.ERROR, "Event id not specified.");
				return false;
			}

			String eventId = (String) eventPayload.get(0);
			
			if (listOfActiveEvents!= null && listOfActiveEvents.containsKey(eventId)) {
				messageMap.put(Constants.ERROR,	"Event can not be activated. It may be already active.");
				return false;
			}
		} else if (request.equals(Constants.DISABLE_EVENT)) {
			if (eventPayload == null || eventPayload.get(0) == null) {
				messageMap.put(Constants.ERROR, "Event id not specified.");
				return false;
			}

			String eventId = (String) eventPayload.get(0);
			if (listOfActiveEvents == null || !listOfActiveEvents.containsKey(eventId)) {
				messageMap.put(Constants.ERROR,	"Event can not be deactivated since it is not active.");
				return false;
			}
			IEvent event = listOfActiveEvents.get(eventId);
			if (!event.isPerishable()) {
				// check if the activities associated with the event have all
				// ended
				if (eventHandler.getNonPerishableEvents().contains(eventId)) {
					messageMap.put(Constants.ERROR,
									"There are open activities associated with this event. So it can't be deactivated.");
					return false;
				}
				event.setPerishable(true);
			}
		} else if (request.equals(Constants.START_ACTIVITY)) {
			if (eventPayload == null) {
				messageMap.put(Constants.ERROR, "Event payload not specified.");
				return false;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			String eventId = (String) eventPayload.get(1);

			if (listOfActiveEvents != null && !listOfActiveEvents.containsKey(eventId)) {
				messageMap.put(Constants.ERROR, "Event is not active.");
				return false;
			}

			if (listOfAccountsAccessingActiveEvents != null && 
					listOfAccountsAccessingActiveEvents.get(eventId) != null &&
					!listOfAccountsAccessingActiveEvents.get(eventId).contains(account.getAccountId())) {
				messageMap.put(Constants.ERROR,	"The account is not allowed to access this event");
				return false;
			}
			String activityName = (String) eventPayload.get(2);

			if (account.getMyOpenActivities() != null && account.getMyOpenActivities().get(eventId) != null
					&& account.getMyOpenActivities().get(eventId).contains(activityName)) {
				messageMap.put(Constants.ERROR,
								"This activity for the event is already runnning. Please end and then re-start.");
				return false;
			}

		} else if (request.equals(Constants.END_ACTIVITY)) {
			if (eventPayload == null) {
				messageMap.put(Constants.ERROR, "Event payload not specified.");
				return false;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			String eventId = (String) eventPayload.get(1);

			if (!listOfActiveEvents.containsKey(eventId)) {
				messageMap.put(Constants.ERROR, "Event is not active.");
				return false;
			}

			if (!listOfAccountsAccessingActiveEvents.get(eventId).contains(
					account.getAccountId())) {
				messageMap.put(Constants.ERROR,
						"The account is not allowed to access this event");
				return false;
			}
			String activityName = (String) eventPayload.get(2);

			if (account.getMyOpenActivities().get(eventId) != null
					&& !account.getMyOpenActivities().get(eventId).contains(
							activityName)) {
				messageMap.put(Constants.ERROR,
						"This activity for the event is not runnning.");
				return false;
			}

		} else if (request.equals(Constants.DO_ACTIVITY)) {
			if (eventPayload == null) {
				messageMap.put(Constants.ERROR, "Event payload not specified.");
				return false;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			String eventId = (String) eventPayload.get(1);

			if (!listOfActiveEvents.containsKey(eventId)) {
				messageMap.put(Constants.ERROR, "Event is not active.");
				return false;
			}

			if (!listOfAccountsAccessingActiveEvents.get(eventId).contains(
					account.getAccountId())) {
				messageMap.put(Constants.ERROR,
						"The account is not allowed to access this event");
				return false;
			}
			String activityName = (String) eventPayload.get(2);

			if (account.getMyOpenActivities().get(eventId) != null
					&& !account.getMyOpenActivities().get(eventId).contains(
							activityName)) {
				messageMap.put(Constants.ERROR,
						"This activity for the event is not runnning.");
				return false;
			}
			IActivity aHandleToAnActivity = account.getAccessibleActivities()
					.get(activityName);

			// check if the activity contains the action
			String actionName = (String) eventPayload.get(3);
			if (!aHandleToAnActivity.getListOfActions().containsKey(actionName)) {
				messageMap.put(Constants.ERROR,
						"The action is undefined for the requested activity.");
				return false;
			}
		}
		return true;
	}

	public void postprocessor(String request, List<Object> eventPayload,
			Object receivedResponse, Map<String, String> messageMap) {
		if (request.equals(Constants.CREATE_EVENT)) {
			IAccount account = (IAccount) eventPayload.get(0);
			listOfActiveEventOwners.put(((IEvent) receivedResponse)
					.getEventId(), account.getAccountId());
			listOfAliveEvents.put(((IEvent) receivedResponse).getEventId(),
					(IEvent) receivedResponse);
			account.getMyOwnedEvents().put(
					((IEvent) receivedResponse).getEventId(),
					(IEvent) receivedResponse);
		} else if (request.equals(Constants.ENABLE_EVENT)) {

			if (receivedResponse == null || ((Boolean) receivedResponse) == Boolean.FALSE) {
				messageMap.put(Constants.ERROR, "Error in activating event");
				return;
			}
			String eventId = (String) eventPayload.get(0);

			String eventOwner = listOfActiveEventOwners.get(eventId);
			HashMap<String, IAccount> loggedInAccounts = ((AccountController) context.getBean(Constants.ACCOUNT_CONTROLLER))
					.getListOfActiveAccounts();
			IAccount owner = loggedInAccounts.get(eventOwner);
			IEvent event = owner.getMyOwnedEvents().get(eventId);
			listOfActiveEvents.put(eventId, event);
			event.setEventStartDate(new Date());
			listOfAccountsAccessingActiveEvents.put(eventId, new ArrayList<String>());

			Iterator<String> accountIter = loggedInAccounts.keySet().iterator();
			while (accountIter.hasNext()) {
				String accountId = accountIter.next();
				IAccount eachAccount = loggedInAccounts.get(accountId);
				if (isEventAccessible(eachAccount, event)) {
					eachAccount.getMyActiveEvents().put(eventId, event);
					listOfAccountsAccessingActiveEvents.get(eventId).add(accountId);
					eachAccount.getMyOpenActivities().put(eventId, new ArrayList<String>());
				}
			}
		}else if (request.equals(Constants.REACTIVATE_EVENT)) {

			if (receivedResponse == null
					|| ((Boolean) receivedResponse) == Boolean.FALSE) {
				messageMap.put(Constants.ERROR, "Error in reactivating event");
				return;
			}
			String eventId = (String) eventPayload.get(0);
			String accountId = (String) eventPayload.get(1);
			
			listOfActiveEventOwners.put(eventId, accountId);
			IEvent event = eventHandler.getEventFromId(eventId);

			listOfActiveEvents.put(eventId, event);
			listOfAliveEvents.put(eventId, event);
			
			HashMap<String, IAccount> loggedInAccounts = ((AccountController) context.getBean(Constants.ACCOUNT_CONTROLLER))
					.getListOfActiveAccounts();
			
			listOfAccountsAccessingActiveEvents.put(eventId,
					new ArrayList<String>());

			IAccount account = loggedInAccounts.get(accountId);
			account.getMyOwnedEvents().put(eventId, event);
			
			Iterator<String> accountIter = loggedInAccounts.keySet().iterator();
			while (accountIter.hasNext()) {
				String eachAccountId = accountIter.next();
				IAccount eachAccount = loggedInAccounts.get(eachAccountId);
				if (isEventAccessible(eachAccount, event)) {
					eachAccount.getMyActiveEvents().put(eventId, event);
					listOfAccountsAccessingActiveEvents.get(eventId).add(
							eachAccountId);
					eachAccount.getMyOpenActivities().put(eventId,
							new ArrayList<String>());
				}
			}
		} else if (request.equals(Constants.DISABLE_EVENT)) {
			if (receivedResponse == null
					|| ((Boolean) receivedResponse) == Boolean.FALSE) {
				messageMap.put(Constants.ERROR, "Error in deactivating event");
				return;
			}
			String eventId = (String) eventPayload.get(0);
			IEvent event = listOfActiveEvents.get(eventId);
			event.setEventEndDate(new Date());

			// remove from various lists
			HashMap<String, IAccount> loggedInAccounts = ((AccountController) context
					.getBean(Constants.ACCOUNT_CONTROLLER))
					.getListOfActiveAccounts();
			List<String> accountsAccessingEvent = listOfAccountsAccessingActiveEvents
					.get(eventId);
			
			for (String accountId : accountsAccessingEvent) {
				IAccount eachAccount = loggedInAccounts.get(accountId);
				eachAccount.getMyActiveEvents().remove(eventId);
				if (eachAccount.getMyOwnedEvents().containsKey(eventId))
					eachAccount.getMyOwnedEvents().remove(eventId);
				eachAccount.getEventActivities().remove(eventId);
				eachAccount.getMyOpenActivities().remove(eventId);

				Map<String, List<IEvent>> activityEvents = eachAccount
						.getActivityEvents();
				Iterator<String> activityIter = activityEvents.keySet()
						.iterator();
				while (activityIter.hasNext()) {
					String activityName = activityIter.next();
					List<IEvent> listOfEvents = activityEvents
							.get(activityName);
					int index = -1;

					for (IEvent eachEvent : listOfEvents) {
						if (eachEvent.getEventId().equals(eventId)) {
							index = listOfEvents.indexOf(eachEvent);
							break;
						}
					}
					if (index != -1) {
						listOfEvents.remove(index);
					}
				}
			}
			listOfAccountsAccessingActiveEvents.remove(eventId);
			listOfActiveEventOwners.remove(eventId);
			listOfActiveEvents.remove(eventId);
			listOfAliveEvents.remove(eventId);
			event = null;
		} else if (request.equals(Constants.START_ACTIVITY)) {
			if (receivedResponse == null || ((Boolean) receivedResponse) == Boolean.FALSE) {
				messageMap.put(Constants.ERROR, "Error in starting activity");
				return;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			String eventId = (String) eventPayload.get(1);
			
			if(listOfActiveEvents != null){
				IEvent event = listOfActiveEvents.get(eventId);	
				if (event != null && event.isPerishable())
					event.setPerishable(false);
			}
			String activityName = (String) eventPayload.get(2);

			if(account.getMyOpenActivities() != null && account.getMyOpenActivities().get(eventId) == null){
				account.getMyOpenActivities().put(eventId, new ArrayList<String>());
			}
			account.getMyOpenActivities().get(eventId).add(activityName);

		} else if (request.equals(Constants.END_ACTIVITY)) {
			if (receivedResponse == null
					|| ((Boolean) receivedResponse) == Boolean.FALSE) {
				messageMap.put(Constants.ERROR, "Error in ending activity");
				return;
			}
			IAccount account = (IAccount) eventPayload.get(0);
			String eventId = (String) eventPayload.get(1);
			String activityName = (String) eventPayload.get(2);

			account.getMyOpenActivities().get(eventId).remove(activityName);

		}
	}

	public Set<String> searchEvents(String communityName, String instanceName,
			String contextName) {
		/* This method is called ONLY WHEN an event has been de-activated
		 * earlier and you want
		 * to search for its details in the database. You will get back a Set
		 * containing eventID's
		 * Set aResultSet = this.aHandleToDAO.searchEvents(communityName,
		 * instanceName, contextName); 
		 */
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}
}
