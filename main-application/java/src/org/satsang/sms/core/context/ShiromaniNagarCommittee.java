/**
 * 
 */
package org.satsang.sms.core.context;

import java.util.HashMap;

import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.context.interfaces.IContext;

/**
 * @author Default
 *
 */
public class ShiromaniNagarCommittee implements IContext{
	
	private int contextId;
	private String contextName;
	private HashMap<String, IActivity> activityMap;

	@Override
	public void setContextName(String aContextName) {
		this.contextName = aContextName;
	}

	@Override
	public String getContextName() {
		return this.contextName;
	}

	@Override
	public void setActivityMap(HashMap<String, IActivity> activityMap) {
		this.activityMap = activityMap;
	}

	@Override
	public HashMap<String, IActivity> getActivityMap() {
		return this.activityMap;
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
