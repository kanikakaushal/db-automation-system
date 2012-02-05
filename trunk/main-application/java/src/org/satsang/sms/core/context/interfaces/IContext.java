/**
 * 
 */
package org.satsang.sms.core.context.interfaces;

import java.util.HashMap;

import org.satsang.sms.core.activities.interfaces.IActivity;

/**
 * @author gcapriha
 * 
 */
public interface IContext {

	public void setContextName(String aContextName);

	public void setActivityMap(HashMap<String, IActivity> activityMap);

	public String getContextName();

	public HashMap<String, IActivity> getActivityMap();
	
	public int getContextId();
	
	public void setContextId(int contextId);

}
