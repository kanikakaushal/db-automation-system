/**
 * 
 */
package org.satsang.sms.core.event.interfaces;

import java.util.Date;

/**
 * @author gcapriha
 * 
 */
public interface IEvent {

	public String getEventName();
	
	public String getContextName();
	
	public String getCommunityID();
	
	public int getContextId();
	
	public String getEventId();
	
	public String getEventDescription();
	
	public Date getEventStartDate();   
	
	public Date getEventEndDate();   
	
	public String getCommunityName();

	public void setEventId(String eventId);
	
	public void setContextId(int contextId);
	
	public void setContextName(String contextName);
	
	public void setCommunityID(String communityID);
	
	public void setCommunityName(String communityName);
	
	public void setEventEndDate(Date eventEndDate);
	
	public void setEventStartDate(Date eventStartDate);
	
	public void setEventDescription(String eventDescription);
	
	public void setEventName(String eventName);
	
	public void setPerishable(boolean perishable);
	
	public boolean isPerishable();
	
	public String getOwner();
	
	public void setOwner(String owner);
	
	public String getBhandaraId() ;
	
	public void setBhandaraId(String bhandaraId);
	
}