/**
 * 
 */
package org.satsang.sms.core.event;

import java.util.Date;
import org.satsang.sms.core.event.interfaces.IEvent;

/**
 * @author gcapriha
 * 
 */
public class Event implements IEvent {

	private String eventId;
	private String eventName;
	private String eventDescription;
	private Date eventStartDate;
	private Date eventEndDate;	
	private String communityName;
	private String communityID;
	private String contextName;
	private int contextId;
	private boolean perishable;
	private String owner;
	private String bhandaraId;
	
	/**
	 * @param eventId the eventId to set
	 */
	@Override
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/**
	 * @return the eventId
	 */
	@Override
	public String getEventId() {
		return eventId;
	}

	/**
	 * @return the eventDescription
	 */
	@Override
	public String getEventDescription() {
		return eventDescription;
	}

	/**
	 * @return the contextId
	 */
	@Override
	public int getContextId() {
		return contextId;
	}

	/**
	 * @return the eventDate
	 */
	@Override
	public Date getEventStartDate() {
		return eventStartDate;
	}

	/**
	 * @return the communityName
	 */
	@Override
	public String getCommunityName() {
		return communityName;
	}

	/**
	 * @return the communityID
	 */
	@Override
	public String getCommunityID() {
		return communityID;
	}

	/**
	 * @return the eventContext
	 */
	@Override
	public String getContextName() {
		return contextName;
	}

	@Override
	public String getEventName() {
		return this.eventName;
	}
		
	@Override
	public Date getEventEndDate() {
		return this.eventEndDate;
	}

	/**
	 * @param eventName the eventName to set
	 */
	@Override
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @param eventDescription the eventDescription to set
	 */
	@Override
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	/**
	 * @param eventStartDate the eventStartDate to set
	 */
	@Override
	public void setEventStartDate(Date eventStartDate) {
		this.eventStartDate = eventStartDate;
	}

	/**
	 * @param eventEndDate the eventEndDate to set
	 */
	@Override
	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}

	/**
	 * @param communityName the communityName to set
	 */
	@Override
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	/**
	 * @param communityID the communityID to set
	 */
	@Override
	public void setCommunityID(String communityID) {
		this.communityID = communityID;
	}

	/**
	 * @param contextName the contextName to set
	 */
	@Override
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * @param contextId the contextId to set
	 */
	@Override
	public void setContextId(int contextId) {
		this.contextId = contextId;
	}

	public void setPerishable(boolean perishable) {
		this.perishable = perishable;
	}

	public boolean isPerishable() {
		return perishable;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setBhandaraId(String bhandaraId) {
		this.bhandaraId = bhandaraId;
	}

	public String getBhandaraId() {
		return bhandaraId;
	}

}
