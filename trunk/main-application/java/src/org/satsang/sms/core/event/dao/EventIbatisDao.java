/**
 * 
 */
package org.satsang.sms.core.event.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.event.interfaces.IEventDao;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Kanikkau
 * 
 */
public class EventIbatisDao implements IEventDao {

	private SqlMapClientTemplate sqlMapClientTemplate;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormatterForEventId = new SimpleDateFormat("yyyyMMdd");

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Integer endActivity(String accountId, String eventID, Integer activityId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventID);
		parameters.put("activityId", activityId);
		parameters.put("activityEndDate", new Date());
		parameters.put("accountId", accountId);

		return this.sqlMapClientTemplate.update("Event.endActivity", parameters);
	}

	@Override
	public Integer endEvent(String eventID) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("eventId", eventID);
		parameters.put("logTime", new Date());
		parameters.put("action", "Deactivated");
		this.sqlMapClientTemplate.insert("Event.logEvent", parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventID);
		parameters.put("eventEndDate", new Date());
		return this.sqlMapClientTemplate.update("Event.endEvent", parameters);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getActiveEvents() {
		List<Object> activeEvents = this.sqlMapClientTemplate
				.queryForList("Event.getActiveEvents");
		return activeEvents;
	}

	public List<Object> getNewEvents() {
		List<Object> activeEvents = this.sqlMapClientTemplate
				.queryForList("Event.getNewEvents");
		return activeEvents;
	}

	public Map<String, Object> getEvent(String eventId) {
		Map<String, Object> eventMap = (Map)this.sqlMapClientTemplate
				.queryForObject("Event.getEvent", eventId);
		return eventMap;
	}

	public List<Object> getTerminatedEvents(String accountId,
			Date startFromDate, Date startToDate) {
		if (startFromDate == null) {
			return new ArrayList<Object>();
		}
		String whereClause = "";
		if (startToDate == null) {
			whereClause += "EVENT_START_DATE LIKE '"
					+ formatter.format(startFromDate) + "%'";
		} else {
			whereClause += "EVENT_START_DATE BETWEEN '"
					+ formatter.format(startFromDate) + "' AND '"
					+ formatter.format(startToDate) + "'";
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("accountId", accountId);
		params.put("whereClause", whereClause);
		List<Object> pastEvents = this.sqlMapClientTemplate.queryForList("Event.getPastEvents", params);
		return pastEvents;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNonPerishableEvents() {
		return this.sqlMapClientTemplate.queryForList("Event.getNonPerishableEvents");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getOpenActivities(String accountId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountId", accountId);
		return this.sqlMapClientTemplate.queryForList("Event.getOpenActivities", parameters);
	}

	@Override
	public void persistEvent(IEvent theEvent, String accountId) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("creationDate", formatter.format(new Date()));
		
		Integer seqNo = (Integer) this.sqlMapClientTemplate.queryForObject(
				"Event.getNextEventId", queryParams);
		if (seqNo == null)
			seqNo = 1;

		String eventId = dateFormatterForEventId.format(new Date())	+ padZeros(seqNo.toString());

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventName", theEvent.getEventName());
		params.put("instanceId", theEvent.getCommunityID());
		params.put("contextId", theEvent.getContextId());
		params.put("description", theEvent.getEventDescription());
		params.put("accountId", accountId);
		params.put("eventId", eventId);
		params.put("bhandaraId", theEvent.getBhandaraId());

		this.sqlMapClientTemplate.insert("Event.createEvent", params);
		theEvent.setEventId(eventId);
		queryParams.put("nextId", seqNo + 1);
		if (seqNo == 1) {
			this.sqlMapClientTemplate.update("Event.insertNextEventId",
					queryParams);
		} else {
			this.sqlMapClientTemplate.update("Event.updateNextEventId",
					queryParams);
		}
	}

	@Override
	public Integer startActivity(String accountId, String eventID, Integer activityId, String desc) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventID);
		parameters.put("activityId", activityId);
		parameters.put("activityStartDate", new Date());
		parameters.put("description", desc);
		parameters.put("accountId", accountId);
		return (Integer) this.sqlMapClientTemplate.insert("Event.startActivity", parameters);
	}

	@Override
	public Integer startEvent(String eventID) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("eventId", eventID);
		parameters.put("logTime", new Date());
		parameters.put("action", "Activated");
		this.sqlMapClientTemplate.insert("Event.logEvent", parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventID);
		parameters.put("eventStartDate", new Date());
		return this.sqlMapClientTemplate.update("Event.startEvent", parameters);
	}
	
	public Integer reactivateEvent(String eventID) {
		Map<String, Object> parameters = new HashMap<String, Object>();

		parameters.put("eventId", eventID);
		parameters.put("logTime", new Date());
		parameters.put("action", "Reactivated");
		this.sqlMapClientTemplate.insert("Event.logEvent", parameters);

		parameters = new HashMap<String, Object>();
		parameters.put("eventId", eventID);
		//parameters.put("eventStartDate", new Date());
		return this.sqlMapClientTemplate.update("Event.restartEvent", parameters);
	}

	private String padZeros(String number){
		String paddedNumber = number;
		while(paddedNumber.length() < 2){
			paddedNumber = "0" + paddedNumber; 
		}
		return paddedNumber;
	}
}
