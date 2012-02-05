package org.satsang.sms.core.account;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.context.interfaces.IContext;
import org.satsang.sms.core.event.interfaces.IEvent;

public class Account implements IAccount {

	/* Private Fields */
	private String accountId;
	private String accountAlias;
	private String accountPassword;
	// private String defaultCommunityCode;
	private String accountEnabled;
	private static Logger log = Logger.getLogger(Account.class);

	private List<Profile> profileList;
	private Map<String, List<ICommunity>> profileCommunityMap;
	private Map<String, ICommunity> communityMap;
	private Map<String, IEvent> myActiveEvents;
	private Map<String, IEvent> myOwnedEvents;
	private Map<String, List<IEvent>> activityEvents;
	private Map<String, List<String>> eventActivities;
	private Map<String, List<String>> myOpenActivities;
	private Map<String, IActivity> accessibleActivities;

	public Map<String, IEvent> getEventsToActivate() {
		Map<String, IEvent> eventsToActivate = new HashMap<String, IEvent>();
		if(myOwnedEvents != null){
			Iterator<String> eventIter = myOwnedEvents.keySet().iterator();
			while (eventIter.hasNext()) {
				String eventId = eventIter.next();
				if (myActiveEvents != null && !myActiveEvents.containsKey(eventId)) {
					eventsToActivate.put(eventId, myOwnedEvents.get(eventId));
				}
			}
		}
		log.info("The events that are eligible for activation for account "
				+ this.accountId + " are " + eventsToActivate.keySet());
		return eventsToActivate;
	}

	public Map<String, IEvent> getEventsToDeactivate() {
		Map<String, IEvent> eventsToDeactivate = new HashMap<String, IEvent>();
		if(myOwnedEvents != null){			
			Iterator<String> eventIter = myOwnedEvents.keySet().iterator();
			while (eventIter.hasNext()) {
				String eventId = eventIter.next();
				if (myActiveEvents != null && myActiveEvents.containsKey(eventId)) {
					eventsToDeactivate.put(eventId, myOwnedEvents.get(eventId));
				}
			}
		}
		log.info("The events that are eligible for deactivation for account "
				+ this.accountId + " are " + eventsToDeactivate.keySet());
		return eventsToDeactivate;
	}

	/**
	 * @return the listOfOwnedEvents
	 */
	public Map<String, IEvent> getMyOwnedEvents() {
		return myOwnedEvents;
	}

	/**
	 * @param listOfOwnedEvents
	 *            the listOfOwnedEvents to set
	 */
	public void setMyOwnedEvents(Map<String, IEvent> listOfOwnedEvents) {
		this.myOwnedEvents = listOfOwnedEvents;
	}

	/**
	 * @return the listOfActiveEvents
	 */
	public Map<String, IEvent> getMyActiveEvents() {
		return myActiveEvents;
	}

	/**
	 * @param listOfActiveEvents
	 *            the listOfActiveEvents to set
	 */
	public void setMyActiveEvents(Map<String, IEvent> listOfActiveEvents) {
		this.myActiveEvents = listOfActiveEvents;
	}

	/* Property Setters */
	/**
	 * @return the communityMap
	 */
	public Map<String, ICommunity> getCommunityMap() {
		return communityMap;
	}

	/**
	 * @param communityMap
	 *            the communityMap to set
	 */
	public void setCommunityMap(Map<String, ICommunity> communityMap) {
		this.communityMap = communityMap;
	}

	/**
	 * @return the profileList
	 */
	public List<Profile> getProfileList() {
		return profileList;
	}

	/**
	 * @param profileList
	 *            the profileList to set
	 */
	public void setProfileList(List<Profile> profileList) {
		this.profileList = profileList;
	}

	/**
	 * @return the profileCommunityMap
	 */
	public Map<String, List<ICommunity>> getProfileCommunityMap() {
		return profileCommunityMap;
	}

	/**
	 * @param profileCommunityMap
	 *            the profileCommunityMap to set
	 */
	public void setProfileCommunityMap(
			Map<String, List<ICommunity>> profileCommunityMap) {
		this.profileCommunityMap = profileCommunityMap;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getAccountAlias() {
		return accountAlias;
	}

	public void setAccountAlias(String accountAlias) {
		this.accountAlias = accountAlias;
	}

	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	public String getAccountPassword() {
		return accountPassword;
	}

	// public void setDefaultCommunityCode(String defaultCommunityCode) {
	// this.defaultCommunityCode = defaultCommunityCode;
	// }
	//
	// public String getDefaultCommunityCode() {
	// return defaultCommunityCode;
	// }

	public String getAccountEnabled() {
		return this.accountEnabled;
	}

	public void setAccountEnabled(String yesNo) {
		this.accountEnabled = yesNo;
	}

	public Profile getProfile(int roleId) {
		for (Profile role : profileList) {
			if (role.getRoleId() == roleId) {
				return role;
			}
		}
		return null;
	}

	/**
	 * @return the activityEvents
	 */
	public Map<String, List<IEvent>> getActivityEvents() {
		return activityEvents;
	}

	/**
	 * @param activityEvents
	 *            the activityEvents to set
	 */
	public void setActivityEvents(Map<String, List<IEvent>> activityEvents) {
		this.activityEvents = activityEvents;
	}

	/**
	 * @return the myOpenActivities
	 */
	public Map<String, List<String>> getMyOpenActivities() {
		return myOpenActivities;
	}

	/**
	 * @param myOpenActivities
	 *            the myOpenActivities to set
	 */
	public void setMyOpenActivities(Map<String, List<String>> myOpenActivities) {
		this.myOpenActivities = myOpenActivities;
	}

	public Map<String, IActivity> getAccessibleActivities() {

		Iterator<String> commIter = communityMap.keySet().iterator();

		while (commIter.hasNext()) {
			String communityId = commIter.next();
			ICommunity community = communityMap.get(communityId);
			HashMap<String, IContext> contextList = community.getContextMap();

			if (contextList == null) {
				continue;
			}

			Iterator<String> contextIter = contextList.keySet().iterator();
			if (accessibleActivities == null)
				accessibleActivities = new HashMap<String, IActivity>();

			while (contextIter.hasNext()) {
				String contextName = contextIter.next();
				IContext context = contextList.get(contextName);
				HashMap<String, IActivity> activityMap = context
						.getActivityMap();
				Iterator<String> activityIterator = activityMap.keySet()
						.iterator();
				while (activityIterator.hasNext()) {
					String activityName = activityIterator.next();
					IActivity activity = activityMap.get(activityName);
					if (!accessibleActivities.containsKey(activityName)) {
						accessibleActivities.put(activityName, activity);
					}
				}
			}
		}
		log.info("The activities accessible to the account "
				+ this.accountId + " are " + accessibleActivities.keySet());
		return accessibleActivities.isEmpty() ? null : accessibleActivities;
	}

	/**
	 * @param accessibleActivities
	 *            the accessibleActivities to set
	 */
	public void setAccessibleActivities(
			Map<String, IActivity> accessibleActivities) {
		this.accessibleActivities = accessibleActivities;
	}

	/**
	 * @return the eventActivities
	 */
	public Map<String, List<String>> getEventActivities() {
		return eventActivities;
	}

	/**
	 * @param eventActivities
	 *            the eventActivities to set
	 */
	public void setEventActivities(Map<String, List<String>> eventActivities) {
		this.eventActivities = eventActivities;
	}

}
