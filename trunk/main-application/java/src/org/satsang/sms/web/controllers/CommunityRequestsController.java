/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class CommunityRequestsController extends AbstractController {

	private MasterController masterController;

	/**
	 * @param masterController
	 *            the masterController to set
	 */
	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {

		httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		String uri = httpRequest.getRequestURI();
		PrintWriter out = httpResponse.getWriter();

		IAccount account = (IAccount) httpRequest.getSession().getAttribute(Constants.LOGGED_IN_ACCOUNT);

		if (uri.endsWith("communities.do")) {
			Map<String, ICommunity> communityMap = account.getCommunityMap();
			Iterator<String> commIter = communityMap.keySet().iterator();

			String responseXML = "<root><communities>";
			while (commIter.hasNext()) {
				String communityId = commIter.next();
				ICommunity community = communityMap.get(communityId);
				responseXML += "<community id=\"" + communityId + "\" name=\""
						+ community.getInstanceName() + "\" >";
				Set<String> contexts = community.getContextList();
				for (String context : contexts) {
					responseXML += "<context name=\"" + context + "\" />";
				}
				responseXML += "</community>";
			}
			responseXML += "</communities></root>";
			out.print(responseXML);
		} else if (uri.endsWith("branches.do")) {

			String branchSearchString = httpRequest.getParameter(Constants.BRANCH_SEARCH_STRING);

			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.COMMUNITY_REQUEST);

			List<Object> commPayload = new ArrayList<Object>();
			commPayload.add(Constants.BRANCHES_REQUEST);
			List<Object> payload = new ArrayList<Object>();
			payload.add(branchSearchString);

			commPayload.add(payload);
			masterPayload.add(commPayload);

			List<Object> response = masterController.processRequest(masterPayload);

			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<Map> communityGroup = (List<Map>) response.get(1);
				String responseXML = "<root><branches>";
				if(communityGroup != null){
					for (Map branch : communityGroup) {
						responseXML += "<branch id=\"" + branch.get("branchId")
								+ "\" name=\"" + branch.get("branchName")
								+ "\" secretary=\"" + branch.get("branchSecretary")
								+ "\" districtId=\"" + branch.get("districtId")
								+ "\" districtName=\"" + branch.get("districtName")
								+ "\" regionId=\"" + branch.get("regionId")
								+ "\" districtSuffix=\"" + branch.get("districtSuffix")
								+ "\" regionSuffix=\"" + branch.get("regionSuffix")
								+ "\" regionName=\"" + branch.get("regionName") + "\"/>";
					}
				}
				responseXML += "</branches></root>";
				out.print(responseXML);
			}
		} else {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return null;
	}

}
