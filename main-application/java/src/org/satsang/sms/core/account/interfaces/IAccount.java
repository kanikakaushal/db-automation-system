package org.satsang.sms.core.account.interfaces;

import java.util.List;
import java.util.Map;

import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.account.Profile;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.communities.interfaces.ICommunity;

public interface IAccount {

	public Map<String, IEvent> getEventsToActivate();

	public Map<String, IEvent> getEventsToDeactivate();

	public void setAccountId(String accountId);

	public String getAccountId();

	public String getAccountAlias();

	public void setAccountAlias(String accountAlias);

	public void setAccountPassword(String accountPassword);

	public String getAccountPassword();

//	public void setDefaultCommunityCode(String defaultCommunityCode);
//
//	public String getDefaultCommunityCode();

	public void setAccountEnabled(String yesNo);

	public String getAccountEnabled();

	// public List<ICommunity> getAccessibleCommunities();

	public Profile getProfile(int roleId);

	public Map<String, IActivity> getAccessibleActivities();

	public List<Profile> getProfileList();

	public void setProfileList(List<Profile> profileList);

	public Map<String, List<ICommunity>> getProfileCommunityMap();

	public void setProfileCommunityMap(
			Map<String, List<ICommunity>> profileCommunityMap);

	public void setCommunityMap(Map<String, ICommunity> communityMap);

	public Map<String, ICommunity> getCommunityMap();

	public Map<String, List<IEvent>> getActivityEvents();

	public void setActivityEvents(Map<String, List<IEvent>> activityEvents);

	public Map<String, List<String>> getMyOpenActivities();

	public void setMyOpenActivities(Map<String, List<String>> myOpenActivities);

	public Map<String, IEvent> getMyOwnedEvents();

	public void setMyOwnedEvents(Map<String, IEvent> listOfOwnedEvents);

	public Map<String, IEvent> getMyActiveEvents();

	public void setMyActiveEvents(Map<String, IEvent> listOfActiveEvents);

	public void setEventActivities(Map<String, List<String>> eventActivities);

	public Map<String, List<String>> getEventActivities();
}
