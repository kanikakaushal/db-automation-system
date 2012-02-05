/**
 * 
 */
package org.satsang.sms.core.account.interfaces;

/**
 * @author Default
 * 
 */
public interface IAccountHandler {
	
	public void setHandleToAccountDAO(IAccountDao handleToAccountDAO);

	public void setHandleToAccountPrivDAO(
			IAccountPrivDao handleToAccountPrivDAO);

	public IAccount login(String accountID, String password);

	public void update(IAccount accountRef);

	public IAccount create(String accountID, String accountPassword,
			String accountEnabled);

	public void disable(IAccount accountRef);
}
