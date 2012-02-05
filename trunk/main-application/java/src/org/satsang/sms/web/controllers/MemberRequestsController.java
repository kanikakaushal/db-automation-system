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

import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.member.interfaces.IMember;
import org.satsang.sms.core.util.Constants;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class MemberRequestsController extends AbstractController {

	private MasterController masterController;

	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	@Override
	protected ModelAndView handleRequestInternal(
			HttpServletRequest httpRequest, HttpServletResponse httpResponse)
			throws Exception {

		httpResponse.setHeader("Cache-Control", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		String uri = httpRequest.getRequestURI();
		PrintWriter out = httpResponse.getWriter();

		if (uri.endsWith("member.do")) {
			String memberId = httpRequest.getParameter(Constants.MEMBER_ID);
			List<Object> masterPayload = new ArrayList<Object>();
			masterPayload.add(Constants.MEMBER_REQUEST);

			List<Object> memberPayload = new ArrayList<Object>();
			memberPayload.add(Constants.MEMBER_DETAILS_REQUEST);
			List<Object> payload = new ArrayList<Object>();
			payload.add(memberId);

			memberPayload.add(payload);
			masterPayload.add(memberPayload);

			List<Object> response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				String responseXML = "<root>";
				if(response.size() > 1){
					IMember member = (IMember) response.get(1);
					if(member != null){
						responseXML += "<member id=\""
								+ member.getMemberId()
								+ "\" firstName=\""
								+ member.getMemberFirstName()
								+ "\" middleName=\""
								+ nullToEmptyString(member.getMemberMiddleName())
								+ "\" lastName=\""
								+ nullToEmptyString(member.getMemberLastName())
								+ "\" gender=\""
								+ member.getMemberGender()
								+ "\" initiationStatus=\""
								+ member.getInitiationStatus()
								+ "\" age=\""
								+ (member.getMemberAge() == null? "" : Integer.toString(member.getMemberAge()))
								+ "\" occupation=\""
								+ nullToEmptyString(member.getMemberOccupation())
								+ "\" homeAddress1=\""
								+ nullToEmptyString(member.getMemberAddressLine1())
								+ "\" homeAddress2=\""
								+ nullToEmptyString(member.getMemberAddressLine2())
								+ "\" city=\""
								+ nullToEmptyString(member.getMemberAddressCity())
								+ "\" state=\""
								+ nullToEmptyString(member.getMemberAddressState())
								+ "\" country=\""
								+ nullToEmptyString(member.getMemberAddressCountry())
								+ "\" pincode=\""
								+ nullToEmptyString(member.getMemberAddressPinCode())
								+ "\" branchId=\""
								+ member.getBranchCode()
								+ "\" branchName=\"" + member.getBranchName()
								+ "\" secretary=\"" + nullToEmptyString(member.getBranchSecretary())
								+ "\" districtId=\"" + nullToEmptyString(member.getDistrictId())
								+ "\" districtName=\"" + nullToEmptyString(member.getDistrictName())
								+ "\" regionId=\"" + nullToEmptyString(member.getRegionId())
								+ "\" districtSuffix=\"" + nullToEmptyString(member.getDistrictSuffix())
								+ "\" regionSuffix=\"" + nullToEmptyString(member.getRegionSuffix())
								+ "\" regionName=\"" + nullToEmptyString(member.getRegionName())
								+ "\" letterNo=\"" + nullToEmptyString(member.getLetterNoIfJigyasu())
								+ "\" initiatedParent=\"" + nullToEmptyString(member.getInitiatedParent())
								+ "\"/>";
					}
				}
				responseXML += "</root>";
				out.print(responseXML);
			}
		} else {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}

		return null;
	}

	private String nullToEmptyString(String nullString) {
		return nullString == null ? "" : nullString;
	}
	
}
