/**
 * 
 */
package org.satsang.sms.core.context;

import java.util.HashMap;

import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.context.interfaces.IContext;

/**
 * Possible Activities: RegisterBhent SubmitBhent
 * @author gcapriha
 * 
 */
public class Bhent implements IContext {

	private int contextId;
	private String contextName;
	private HashMap<String, IActivity> activityMap;	
	
	public void setContextName(String aContextName) {
		this.contextName = aContextName;
	}

	@Override
	public String getContextName() {
		return contextName;
	}

	@Override
	public void setActivityMap(HashMap<String, IActivity> activityMap) {
		this.activityMap = activityMap;
	}

	@Override
	public HashMap<String, IActivity> getActivityMap() {
		return activityMap;
	}

	@Override
	public int getContextId() {
		return contextId;
	}

	@Override
	public void setContextId(int contextId) {
		this.contextId = contextId;
	}
}
