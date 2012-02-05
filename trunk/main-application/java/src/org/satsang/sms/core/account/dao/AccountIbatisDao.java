/**
 * 
 */
package org.satsang.sms.core.account.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.account.interfaces.IAccountDao;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Kanikkau
 * 
 */
public class AccountIbatisDao implements IAccountDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@Override
	public Map<String, String> createAccount(String accountID,
			String accountPassword, String accountEnabled) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountID);
		parameters.put("accountPassword", accountPassword);
		parameters.put("accountEnabled", accountEnabled);
		this.sqlMapClientTemplate.insert("Account.createAccount", parameters);
		return this.getAccount(accountID, accountPassword);
	}

	@Override
	public void disableAccount(IAccount accountRef) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountRef.getAccountId());
		parameters.put("accountPassword", accountRef.getAccountPassword());
		this.sqlMapClientTemplate.update("Account.disableAccount", parameters);
	}

	@Override
	public void enableAccount(IAccount accountRef) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountRef.getAccountId());
		parameters.put("accountPassword", accountRef.getAccountPassword());
		this.sqlMapClientTemplate.update("Account.enableAccount", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getAccount(String accountID, String password) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountID);
		parameters.put("accountPassword", password);
		HashMap<String, String> account = (HashMap<String, String>)sqlMapClientTemplate
				.queryForObject("Account.getAccount", parameters);
		return account;
	}

	@Override
	public void updateAccountPassword(IAccount accountRef) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("accountID", accountRef.getAccountId());
		parameters.put("accountPassword", accountRef.getAccountPassword());
		this.sqlMapClientTemplate.update("Account.updatePassword", parameters);
	}

}
