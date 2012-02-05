/**
 * 
 */
package org.satsang.sms.core.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.event.interfaces.IEventDao;
import org.satsang.sms.core.event.interfaces.IEventHandler;
import org.satsang.sms.core.util.Constants;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Default
 * 
 */
public class EventHandler implements IEventHandler, ApplicationContextAware {

	private IEventDao eventDAO;
	private ApplicationContext context;

	public IEvent getEventFromId(String eventId){
		Map<String, Object> eventMap = eventDAO.getEvent(eventId);
		
		IEvent event = (IEvent) context.getBean(Constants.EVENT);
		event.setCommunityID((String) eventMap.get("communityId"));
		event.setContextId((Integer) eventMap.get("contextId"));
		event.setEventDescription((String) eventMap.get("eventDescription"));
		event.setEventId(eventId);
		event.setEventName((String) eventMap.get("eventName"));
		event.setEventStartDate((Date) eventMap.get("eventStartDate"));
		event.setOwner((String) eventMap.get("accountName"));
		event.setCommunityName((String) eventMap.get("communityName"));
		event.setContextName((String) eventMap.get("contextName"));
		event.setEventEndDate((Date) eventMap.get("eventEndDate"));
		event.setBhandaraId((String) eventMap.get("bhandaraId"));
		return event;

	}
	
	public HashMap<String, List<String>> getAccountOpenActivities(
			String accountId) {
		List<Map> accountEventActivities = eventDAO
				.getOpenActivities(accountId);
		HashMap<String, List<String>> accountOpenActivities = new HashMap<String, List<String>>();
		for (Map m : accountEventActivities) {
			String eventId = (String) m.get("eventId");
			String activityName = (String) m.get("activityName");
			List<String> activityList = accountOpenActivities.get(eventId);
			if (activityList == null) {
				accountOpenActivities.put((String) m.get("eventId"),
						new ArrayList<String>());
				activityList = accountOpenActivities.get(m.get("eventId"));
			}
			if (!activityList.contains(activityName))
				activityList.add(activityName);
		}
		return accountOpenActivities;
	}

	public List<Object> getActiveEvents() {
		List<Object> returnValues = new ArrayList<Object>();
		// List<Object> activeEvents = eventDAO.getActiveEvents();
		List<Object> aliveEvents = eventDAO.getNewEvents();
		List<String> nonPerishableEvents = eventDAO.getNonPerishableEvents();

		Map<String, IEvent> listOfActiveEvents = new HashMap<String, IEvent>();
		Map<String, String> listOfActiveEventOwners = new HashMap<String, String>();
		Map<String, IEvent> listOfAliveEvents = new HashMap<String, IEvent>();

		for (Object result : aliveEvents) {
			Map<String, Object> record = (Map<String, Object>) result;
			String eventId = (String) record.get("eventId");

			IEvent event = (IEvent) context.getBean(Constants.EVENT);
			event.setCommunityID((String) record.get("communityId"));
			event.setContextId((Integer) record.get("contextId"));
			event.setEventDescription((String) record.get("eventDescription"));
			event.setEventId(eventId);
			event.setEventName((String) record.get("eventName"));
			event.setEventStartDate((Date) record.get("eventStartDate"));
			event.setOwner((String) record.get("accountName"));
			event.setCommunityName((String) record.get("communityName"));
			event.setContextName((String) record.get("contextName"));
			event.setBhandaraId((String) record.get("bhandaraId"));

			if (!nonPerishableEvents.contains(eventId)) {
				event.setPerishable(true);
			}

			if (record.get("eventStartDate") != null) {
				listOfActiveEvents.put(eventId, event);
			}
			listOfActiveEventOwners.put(eventId, (String) record
					.get("accountId"));

			listOfAliveEvents.put(eventId, event);
		}

		returnValues.add(listOfActiveEvents);
		returnValues.add(listOfActiveEventOwners);
		returnValues.add(listOfAliveEvents);
		return returnValues;
	}

	public List<String> getNonPerishableEvents() {
		List<String> nonPerishableEvents = eventDAO.getNonPerishableEvents();
		return nonPerishableEvents;
	}

	@Override
	public IEvent create(IAccount account,
			ICommunity handleToACommunityInstance, String contextName,
			String eventName, String eventDescription, String bhandaraId) {

		IEvent anEvent = (IEvent) context.getBean(Constants.EVENT);

		anEvent.setCommunityName(handleToACommunityInstance.getInstanceName());
		anEvent.setCommunityID(handleToACommunityInstance.getInstanceID());
		anEvent.setContextName(contextName);
		anEvent.setContextId(handleToACommunityInstance.getHandleToContext(
				contextName).getContextId());
		anEvent.setEventDescription(eventDescription);
		anEvent.setEventName(eventName);
		anEvent.setOwner(account.getAccountAlias());
		anEvent.setBhandaraId(bhandaraId);
		
		// call to DAO to persist event in database and get eventId
		eventDAO.persistEvent(anEvent, account.getAccountId());
		// set eventId in event
		return anEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.satsang.sms.core.Event.interfaces.IEventHandler#deactivateEvent(java
	 * .lang.String)
	 */
	@Override
	public Boolean deactivate(String eventId) {
		Integer status = eventDAO.endEvent(eventId);
		if (status > 0)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.satsang.sms.core.Event.interfaces.IEventHandler#searchEvents(java
	 * .lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<IEvent> search(String accountId, Date startFromDate,
			Date startToDate) {

		List<IEvent> searchResult = new ArrayList<IEvent>();
		
		List<Object> pastEvents = eventDAO.getTerminatedEvents(accountId,
				startFromDate, startToDate);
		for (Object result : pastEvents) {
			Map<String, Object> record = (Map<String, Object>) result;
			String eventId = (String) record.get("eventId");

			IEvent event = (IEvent) context.getBean(Constants.EVENT);
			event.setCommunityID((String) record.get("communityId"));
			event.setContextId((Integer) record.get("contextId"));
			event.setEventDescription((String) record.get("eventDescription"));
			event.setEventId(eventId);
			event.setEventName((String) record.get("eventName"));
			event.setEventStartDate((Date) record.get("eventStartDate"));
			event.setOwner((String) record.get("accountName"));
			event.setCommunityName((String) record.get("communityName"));
			event.setContextName((String) record.get("contextName"));
			event.setEventEndDate((Date) record.get("eventEndDate"));
			event.setBhandaraId((String) record.get("bhandaraId"));

			searchResult.add(event);
		}
		return searchResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.satsang.sms.core.Event.interfaces.IEventHandler#setEventDAO(org.satsang
	 * .sms.core.Event.interfaces.IEventDAO)
	 */
	@Override
	public void setEventDAO(IEventDao eventDAO) {
		this.eventDAO = eventDAO;
	}

	@Override
	public Boolean activate(String eventId) {
		Integer status = eventDAO.startEvent(eventId);
		if (status > 0)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}
	
	public Boolean reactivate(String eventId, String accountId) {
		Integer status = eventDAO.reactivateEvent(eventId);
		if (status > 0)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	@Override
	public Boolean startActivity(IAccount account, String eventId, String activityName, String description) {
		boolean status = false;
		Map<String, IActivity> activites = account.getAccessibleActivities();
		if(activites != null){
			IActivity aHandleToAnActivity = activites.get(activityName); 
			if(aHandleToAnActivity != null){
				eventDAO.startActivity(account.getAccountId(), eventId, aHandleToAnActivity.getActivityId(), description);
				status = true; 
			}
		}
		return status;

	}

	@Override
	public ArrayList<Object> doActivity(IAccount account, String eventId,
			String activityName, String anAction,
			ArrayList<Object> anArrayListOfObjects) {
		IActivity aHandleToAnActivity = account.getAccessibleActivities().get(
				activityName);

		return aHandleToAnActivity.doActivity(account.getAccountId(), eventId,
				anAction, anArrayListOfObjects);
	}

	@Override
	public Boolean endActivity(IAccount account, String eventId, String activityName) {
		boolean status = false;
		Map<String, IActivity> activites = account.getAccessibleActivities();
		if(activites != null){
			IActivity aHandleToAnActivity = activites.get(activityName);
			if(aHandleToAnActivity != null){
				eventDAO.endActivity(account.getAccountId(), eventId, aHandleToAnActivity.getActivityId());
				status = true; 
			}
		}
		return status;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;

	}

}
