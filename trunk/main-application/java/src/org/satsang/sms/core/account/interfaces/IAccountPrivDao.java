package org.satsang.sms.core.account.interfaces;

import java.util.List;
import java.util.Map;

public interface IAccountPrivDao {

	public List<Map<String, Object>> getAccountRolesList(String accountID);

	public List<Map<String, Object>> getAccountAccessibleCommunities(String accountID);
	
	public Integer getContextIdFromName(String contextName);
	
	public Integer getActivityIdFromName(String activityName);
}
