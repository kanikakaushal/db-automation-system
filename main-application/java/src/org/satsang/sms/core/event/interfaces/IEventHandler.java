/**
 * 
 */
package org.satsang.sms.core.event.interfaces;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.communities.interfaces.ICommunity;

/**
 * @author Default
 * 
 */
public interface IEventHandler {

	public IEvent create(IAccount account,
			ICommunity handleToACommunityInstance, String contextName,
			String eventName, String eventDescription, String bhandaraId);

	public List<IEvent> search(String accountId, Date startFromDate,
			Date startToDate);

	public Boolean startActivity(IAccount account, String eventId,
			String activityName, String description);

	public ArrayList<Object> doActivity(IAccount account, String eventId,
			String activityName, String anAction,
			ArrayList<Object> anArrayListOfObjects);

	public Boolean activate(String eventId);

	public Boolean deactivate(String eventId);

	/* The DAO's come here */
	public void setEventDAO(IEventDao eventDAO);

	public Boolean endActivity(IAccount account, String eventId,
			String activityName);

	public List<Object> getActiveEvents();

	public HashMap<String, List<String>> getAccountOpenActivities(
			String accountId);

	public List<String> getNonPerishableEvents();

	public Boolean reactivate(String eventId, String accountId);

	public IEvent getEventFromId(String eventId);
}
