/**
 * 
 */
package org.satsang.sms.core.event.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author gcapriha
 * 
 */
public interface IEventDao {

	public void persistEvent(IEvent theEvent, String accountId);

	public Integer startEvent(String eventID);

	public Integer endEvent(String eventID);

	public Integer startActivity(String accountId, String eventID,
			Integer activityId, String desc);

	public Integer endActivity(String accountId, String eventID,
			Integer activityId);

	public List<Object> getActiveEvents();

	public List<Object> getNewEvents();

	public List<String> getNonPerishableEvents();

	public List<Map> getOpenActivities(String accountId);

	public List<Object> getTerminatedEvents(String accountId,
			Date startFromDate, Date startToEndDate);
	
	public Integer reactivateEvent(String eventID);
	
	public Map<String, Object> getEvent(String eventId);
}
