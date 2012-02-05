/**
 * 
 */
package org.satsang.sms.core.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.account.interfaces.IAccountPrivDao;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Kanikkau
 * 
 */
public class AccountPrivIbatisDao implements IAccountPrivDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAccountAccessibleCommunities(String accountID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountID);
		return (List<Map<String, Object>>) this.sqlMapClientTemplate.queryForList(
				"AccountPriv.getAccountCommunities", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getAccountRolesList(String accountID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountID);
		return (List<Map<String, Object>>) this.sqlMapClientTemplate.queryForList(
				"AccountPriv.getAccountProfiles", parameters);
	}

	@Override
	public Integer getActivityIdFromName(String activityName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("activityName", activityName);
		return (Integer) this.sqlMapClientTemplate.queryForObject(
				"AccountPriv.getActivityId", parameters);
	}

	@Override
	public Integer getContextIdFromName(String contextName) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("contextName", contextName);
		return (Integer) this.sqlMapClientTemplate.queryForObject(
				"AccountPriv.getContextId", parameters);
	}

}
