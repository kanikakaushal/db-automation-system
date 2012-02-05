package org.satsang.sms.web.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.satsang.sms.core.account.AccountHandler;
import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Application Lifecycle Listener implementation class SessionExpireListener
 * 
 */
public class SessionExpireListener implements HttpSessionListener {

	private static Logger log = Logger.getLogger(AccountHandler.class);
	
	/**
	 * Default constructor.
	 */
	public SessionExpireListener() {
		
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent arg0) {
		
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		IAccount account = (IAccount) session
				.getAttribute(Constants.LOGGED_IN_ACCOUNT);
		log.info(account.getAccountId() + " logging out");
		
		if (account != null) {
			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.ACCOUNT_REQUEST);

			List<Object> accountPayload = new ArrayList<Object>();
			accountPayload.add(Constants.LOGOUT_ACCOUNT);
			List<Object> payload = new ArrayList<Object>();
			payload.add(account.getAccountId());

			accountPayload.add(payload);
			masterPayload.add(accountPayload);

			((MasterController) WebApplicationContextUtils
					.getWebApplicationContext(session.getServletContext())
					.getBean(Constants.MASTER_CONTROLLER))
					.processRequest(masterPayload);
		}
		
	}

}
