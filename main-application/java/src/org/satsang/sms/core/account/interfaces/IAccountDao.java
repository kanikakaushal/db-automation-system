package org.satsang.sms.core.account.interfaces;

import java.util.Map;

public interface IAccountDao {

	public Map<String, String> getAccount(String accountID, String password);

	public Map<String, String> createAccount(String accountID, String accountPassword,
			String accountEnabled);

	public void enableAccount(IAccount accountRef);

	public void disableAccount(IAccount accountRef);

	public void updateAccountPassword(IAccount accountRef);

}
