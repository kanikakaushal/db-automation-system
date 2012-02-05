/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class LoginController extends AbstractController {

	private MasterController masterController;
	
	/**
	 * @param masterController the masterController to set
	 */
	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {

		httpResponse.setHeader("Cache-Control","no-cache");
		httpResponse.setDateHeader ("Expires", 0);

		String userId = httpRequest.getParameter(Constants.USER_ID);
		String password = httpRequest.getParameter(Constants.PASSWORD);

		List<Object> masterPayload = new ArrayList<Object>();
		masterPayload.add(Constants.ACCOUNT_REQUEST);

		List<Object> accountPayload = new ArrayList<Object>();
		accountPayload.add(Constants.LOGIN_ACCOUNT);
		List<Object> payload = new ArrayList<Object>();
		payload.add(userId);
		payload.add(password);
		
		accountPayload.add(payload);
		masterPayload.add(accountPayload);

		List<Object> response = masterController.processRequest(masterPayload);
		
		PrintWriter out = httpResponse.getWriter();
		
		Map<String, String> messageMap = (Map<String, String>)response.get(0);
		if(messageMap.get(Constants.ERROR)!=null){
			out.print(messageMap.get(Constants.ERROR));			
		}else if(messageMap.get(Constants.INFO)!= null){
			out.print(messageMap.get(Constants.INFO));	
		}else{
			IAccount account = (IAccount)response.get(1);
			
			httpRequest.getSession().setAttribute(Constants.LOGGED_IN_ACCOUNT, account);
			String responseXML = "<root><account id=\""+account.getAccountId()+"\" name=\""+account.getAccountAlias()+"\"/></root>";
			out.print(responseXML);	
		}		

		return null;
	}

}
