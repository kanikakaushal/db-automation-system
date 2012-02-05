/**
 * 
 */
package org.satsang.sms.core.context;

import java.util.HashMap;

import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.context.interfaces.IContext;

/**
 * @author gcapriha
 * 
 */
public class Satsang implements IContext {

	private HashMap<String, IActivity> activityMap;
	private String contextName;
	private int contextId;

	public void setContextName(String aContextName) {
		this.contextName = aContextName;
	}

	public String getContextName() {
		return this.contextName;
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
		return this.contextId;
	}

	@Override
	public void setContextId(int contextId) {
		this.contextId = contextId;
	}
}
