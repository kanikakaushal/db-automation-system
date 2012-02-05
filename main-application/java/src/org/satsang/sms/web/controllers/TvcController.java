/**
 * 
 */
package org.satsang.sms.web.controllers;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.satsang.sms.aspects.statistics.SadanStats;
import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.activities.tvc.TvcSearchMember;
import org.satsang.sms.core.controllers.MasterController;
import org.satsang.sms.core.event.interfaces.IEvent;
import org.satsang.sms.core.util.Constants;
import org.satsang.sms.core.util.print.PrinterFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Default
 * 
 */
public class TvcController extends AbstractController {

	private PrinterFactory printFactory;
	private MasterController masterController;
	
	private final String ACTIVITY = "TVC";
	private final String NEW_REGISTRATION = "register";
	private final String EDIT_REGISTRATION = "edit";
	private final String SEARCH_REGISTRATION = "search";
	private final String DUPLICATE_CARD = "duplicate";
	private final String VIEW_ASSOCIATIONS = "viewAssociations";
	private final String ADD_ASSOCIATIONS = "addAssociations";
	private final String SADAN_STATS = "sadanStats";
	private final String SQ_STATS = "sabhaQuarterStats";
	private final String LICENSEE_DETAILS = "licenseeDetails";
	private final String PRIMARY_DETAILS = "primaryDetails";
	private final String VALIDATE_ASSOCIATE = "validateAssociate";
	private final String PQ_LIST = "getPQList";
	
	private static Logger log = Logger.getLogger(TvcController.class);

	public void setMasterController(MasterController masterController) {
		this.masterController = masterController;
	}

	public void setPrintFactory(PrinterFactory printFactory) {
		this.printFactory = printFactory;
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
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

		IAccount account = (IAccount) httpRequest.getSession().getAttribute(Constants.LOGGED_IN_ACCOUNT);
		IEvent event = (IEvent) httpRequest.getSession().getAttribute(Constants.SELECTED_EVENT);

		List<Object> masterPayload = new ArrayList<Object>();
		masterPayload.add(Constants.EVENT_REQUEST);
		List<Object> eventPayload = new ArrayList<Object>();
		eventPayload.add(Constants.DO_ACTIVITY);
		List<Object> payload = new ArrayList<Object>();
		payload.add(account);
		payload.add(event.getEventId());
		payload.add(ACTIVITY);
		eventPayload.add(payload);
		masterPayload.add(eventPayload);

		List<Object> params = new ArrayList<Object>();
		List<Object> response = null;

		if (uri.endsWith("new.do")) {
			payload.add(NEW_REGISTRATION);
			
			log.debug("REQUEST FOR NEW REGISTRATION-------------");
			
			String memberId = httpRequest.getParameter(Constants.MEMBER_ID);			
			String firstName = httpRequest.getParameter(Constants.FIRST_NAME);
			String middleName = httpRequest.getParameter(Constants.MIDDLE_NAME);
			String lastName = httpRequest.getParameter(Constants.LAST_NAME);
			String age = httpRequest.getParameter(Constants.AGE);
			String occupation = httpRequest.getParameter(Constants.OCCUPATION);
			String gender = httpRequest.getParameter(Constants.GENDER);
			String homeAddressLine1 = httpRequest.getParameter(Constants.HOME_ADDRESS_LINE1);
			String homeAddressLine2 = httpRequest.getParameter(Constants.HOME_ADDRESS_LINE2);
			String city = httpRequest.getParameter(Constants.CITY);
			String state = httpRequest.getParameter(Constants.STATE);
			String country = httpRequest.getParameter(Constants.COUNTRY);
			String pincode = httpRequest.getParameter(Constants.PINCODE);
			String accoType = httpRequest.getParameter(Constants.ACCOMODATION_TYPE);
			String colony = httpRequest.getParameter(Constants.COLONY);
			String sadan = httpRequest.getParameter(Constants.SADAN);
			String localAddress = httpRequest.getParameter(Constants.LOCAL_ADDRESS);
			String permission = httpRequest.getParameter(Constants.PERMISSION);
			String permissionComments = httpRequest.getParameter(Constants.PERMISSION_COMMENTS);
			String isDuplicate = httpRequest.getParameter(Constants.IS_DUPLICATE);
			String initiationStatus = httpRequest.getParameter(Constants.INITIATION_STATUS);
			String branch = httpRequest.getParameter(Constants.BRANCH);
			String region = httpRequest.getParameter(Constants.REGION);
			String district = httpRequest.getParameter(Constants.DISTRICT);
			String toDate = httpRequest.getParameter(Constants.TO_DATE);
			String childCount = httpRequest.getParameter(Constants.NO_OF_CHILDREN);
			String agesChildren = httpRequest.getParameter(Constants.CHILDREN_AGES);
			String branchName = httpRequest.getParameter(Constants.BRANCH_NAME);
			String districtName = httpRequest.getParameter(Constants.DISTRICT_NAME);
			String regionName = httpRequest.getParameter(Constants.REGION_NAME);
			String licenseeRelation = httpRequest.getParameter(Constants.LICENSEE_RELATION);
			String recommendationType = httpRequest.getParameter(Constants.RECOMMENDATION_TYPE);
			String terminalId = httpRequest.getParameter(Constants.TERMINAL_ID);
			String isAssociate = httpRequest.getParameter(Constants.IS_ASSOCIATE);
			String letterNoIfJigyasu = httpRequest.getParameter(Constants.JIGYASU_LETTER_NO);
			String initiatedParent = httpRequest.getParameter(Constants.INITIATED_PARENT);
			
			TvcMember tvcMember = new TvcMember();
			tvcMember.setFirstName(firstName);
			tvcMember.setMiddleName(emptyStringToNull(middleName));
			tvcMember.setLastName(emptyStringToNull(lastName));
			tvcMember.setGender(gender);
			tvcMember.setAge(emptyStringToNull(age)== null ? null : Integer.parseInt(age));
			tvcMember.setOccupation(emptyStringToNull(occupation));
			tvcMember.setInitiationStatus(initiationStatus);
			tvcMember.setLocalAddress(localAddress);
			tvcMember.setStayCategory(accoType);
			tvcMember.setStayFromDate(new Date());
			tvcMember.setStayToDate(sdf.parse(toDate));
			tvcMember.setPermissionType(permission);
			tvcMember.setBranchId(branch);
			tvcMember.setRegionId(region);
			tvcMember.setDistrictId(district);
			if (emptyStringToNull(isDuplicate) != null) {
				if (isDuplicate.equalsIgnoreCase("false")) {
					tvcMember.setDuplicate(false);
				} else if (isDuplicate.equalsIgnoreCase("true")) {
					tvcMember.setDuplicate(true);
				}
			}
			tvcMember.setAddressCity(emptyStringToNull(city));
			tvcMember.setAddressCountry(emptyStringToNull(country));
			tvcMember.setAddressLine1(emptyStringToNull(homeAddressLine1));
			tvcMember.setAddressLine2(emptyStringToNull(homeAddressLine2));
			tvcMember.setAddressPinCode(emptyStringToNull(pincode));
			tvcMember.setAddressState(emptyStringToNull(state));
			tvcMember.setLocalColony(emptyStringToNull(colony));
			tvcMember.setLocalSadan(emptyStringToNull(sadan));
			tvcMember.setNoOfChildren(emptyStringToNull(childCount)== null ? 0 : Integer.parseInt(childCount));
			tvcMember.setPermissionRemarks(emptyStringToNull(permissionComments));
			tvcMember.setChildrenAges(emptyStringToNull(agesChildren));
			tvcMember.setBranchName(emptyStringToNull(branchName));
			tvcMember.setDistrictName(emptyStringToNull(districtName));
			tvcMember.setRegionName(emptyStringToNull(regionName));
			tvcMember.setTvcId(emptyStringToNull(memberId));
			tvcMember.setPqLicenseeRelation(emptyStringToNull(licenseeRelation));
			tvcMember.setRecommendationType(emptyStringToNull(recommendationType));
			tvcMember.setLetterNoIfJigyasu(emptyStringToNull(letterNoIfJigyasu));
			tvcMember.setInitiatedParent(emptyStringToNull(initiatedParent));
			
			if (emptyStringToNull(isAssociate) != null) {
				if (isAssociate.equalsIgnoreCase("false")) {
					tvcMember.setAssociate(false);
				} else if (isAssociate.equalsIgnoreCase("true")) {
					tvcMember.setAssociate(true);
				}
			}
			params.add(tvcMember);
			params.add(event.getBhandaraId());
			payload.add(params);

			log.debug("TVC ID GETTING GENERATED FOR MEMBER ID ------------- "+ memberId);
			
			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response
					.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				String tvcId = "";
				if(((List) response.get(1)).size() > 0){
					tvcId = (String) ((List) response.get(1)).get(0);
					log.debug("TVC ID GENERATED ------------- "+ tvcId);
//					tvcMember.setTvcId(tvcId);
	
					if(tvcId == null){
						tvcId = "";
					}else if(tvcId.equals("A TVC Card has already been printed for this TVC ID")){
						tvcId = null;
					}else{
						String listOfAssociations = httpRequest.getParameter(Constants.ASSOCIATION_LIST);
						List<TvcAssociateMember> associations = null;
		
						if (listOfAssociations != null && !listOfAssociations.equals("")) {
							// ADD ASSOCIATIONS ---------------------------------
							payload.remove(3);
							payload.remove(3);
							payload.add(ADD_ASSOCIATIONS);
		
							StringTokenizer tokenizer = new StringTokenizer(listOfAssociations, ";");
							associations = new ArrayList<TvcAssociateMember>();
		
							while (tokenizer.hasMoreTokens()) {
								String association = tokenizer.nextToken();
								TvcAssociateMember associate = new TvcAssociateMember();
								associate.setTvcId(tvcId);
		
								StringTokenizer associateTokenizer = new StringTokenizer(association, ",");
								int count = 0;
								while (associateTokenizer.hasMoreTokens()) {
									count++;
									switch (count) {
									case 1:
										associate.setAssociateTvcId(associateTokenizer.nextToken());
										break;
									case 2:
										associate.setAssociateRelationship(associateTokenizer.nextToken());
										break;
									case 3:
										associate.setGender(associateTokenizer.nextToken());
										break;
									default:
									}
								}
								associations.add(associate);
							}
		
							params = new ArrayList<Object>();
							params.add(tvcId);
							params.add(associations);
							payload.add(params);
		
							response = masterController.processRequest(masterPayload);
							messageMap = (Map<String, String>) response.get(0);
							if (messageMap.get(Constants.ERROR) != null) {
								out.print(messageMap.get(Constants.ERROR));
								log.error("FAILED TO CREATE ASSOCIATES FOR TVC ID --- "+ tvcId);
							} else if (messageMap.get(Constants.INFO) != null) {
								out.print(messageMap.get(Constants.INFO));
								log.error("FAILED TO CREATE ASSOCIATES FOR TVC ID --- "+ tvcId);
							}					
							log.debug("ASSOCIATES CREATED FOR TVC ID  ------------- "+ tvcId);
						}
					
//						if(memberId != null){
							log.debug("ENABLING STAMP BEING PRINTED FOR TVC ID ---------- "+ tvcMember.getTvcId());						
							printFactory.getPrinterPool("Label").getPrinter(terminalId).print(event, tvcMember, associations);						
//						}else{
//							log.debug("COMPLETE TVC CARD BEING PRINTED FOR TVC ID ---------- "+ tvcMember.getTvcId());
//							printFactory.getPrinterPool("Card").getPrinter().print(event, tvcMember, associations);
//						}					
						
					}
					
				}
				
				String responseXML = "<root><registrations>";
				if(tvcId != null){
					responseXML += "<registration ";
					responseXML += "id=\"" + tvcId + "\" ";
					responseXML += "fName=\"" + firstName + "\" mName=\""
							+ nullToEmptyString(middleName) + "\" lName=\"" + nullToEmptyString(lastName) + "\" age=\""
							+ nullToEmptyString(age) + "\" sex=\"" + gender
							+ "\" occupation=\"" + nullToEmptyString(occupation) + "\"";
					responseXML += " initStatus=\"" + initiationStatus
							+ "\" region=\"" + regionName + "\" district=\""
							+ districtName + "\" branch=\"" + branchName + "\"";
					responseXML += " homeAddress1=\"" + nullToEmptyString(homeAddressLine1)
							+ "\" homeAddress2=\"" + nullToEmptyString(homeAddressLine2)
							+ "\" homeCity=\"" + nullToEmptyString(city) 
							+ "\" homeState=\"" + nullToEmptyString(state)
							+ "\" homeCountry=\"" + nullToEmptyString(country) 
							+ "\" pincode=\"" + nullToEmptyString(pincode)
							+ "\" permission=\"" + permission + "\"";
					responseXML += " permissionComments=\"" + nullToEmptyString(permissionComments)
							+ "\" accoType=\"" + accoType 
							+ "\" colony=\"" + nullToEmptyString(colony)
							+ "\" sadan=\"" + nullToEmptyString(sadan) 
							+ "\" localAddress=\"" + localAddress + "\"";
					responseXML += " fromDate=\"" + sdf.format(new Date())
							+ "\" toDate=\"" + toDate 
							+ "\" childCount=\"" + nullToEmptyString(childCount) 
							+ "\" childrenAges=\"" + nullToEmptyString(agesChildren)
							+ "\" isDuplicate=\"" + isDuplicate + "\"";
					responseXML += " />";
				}
				responseXML += "</registrations></root>";
				out.print(responseXML);
			}
		} else if (uri.endsWith("edit.do")) {
			payload.add(EDIT_REGISTRATION);

			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			String firstName = httpRequest.getParameter(Constants.FIRST_NAME);
			String middleName = httpRequest.getParameter(Constants.MIDDLE_NAME);
			String lastName = httpRequest.getParameter(Constants.LAST_NAME);
			String age = httpRequest.getParameter(Constants.AGE);
			String occupation = httpRequest.getParameter(Constants.OCCUPATION);
			String gender = httpRequest.getParameter(Constants.GENDER);
			String homeAddressLine1 = httpRequest.getParameter(Constants.HOME_ADDRESS_LINE1);
			String homeAddressLine2 = httpRequest.getParameter(Constants.HOME_ADDRESS_LINE2);
			String city = httpRequest.getParameter(Constants.CITY);
			String state = httpRequest.getParameter(Constants.STATE);
			String country = httpRequest.getParameter(Constants.COUNTRY);
			String pincode = httpRequest.getParameter(Constants.PINCODE);
			String accoType = httpRequest.getParameter(Constants.ACCOMODATION_TYPE);
			String colony = httpRequest.getParameter(Constants.COLONY);
			String sadan = httpRequest.getParameter(Constants.SADAN);
			String localAddress = httpRequest.getParameter(Constants.LOCAL_ADDRESS);
			String permission = httpRequest.getParameter(Constants.PERMISSION);
			String permissionComments = httpRequest.getParameter(Constants.PERMISSION_COMMENTS);
			String isDuplicate = httpRequest.getParameter(Constants.IS_DUPLICATE);
			String initiationStatus = httpRequest.getParameter(Constants.INITIATION_STATUS);
			String branch = httpRequest.getParameter(Constants.BRANCH);
			String region = httpRequest.getParameter(Constants.REGION);
			String district = httpRequest.getParameter(Constants.DISTRICT);
			String toDate = httpRequest.getParameter(Constants.TO_DATE);
			String childCount = httpRequest.getParameter(Constants.NO_OF_CHILDREN);
			String agesChildren = httpRequest.getParameter(Constants.CHILDREN_AGES);
//			String branchName = httpRequest.getParameter(Constants.BRANCH_NAME);
//			String districtName = httpRequest.getParameter(Constants.DISTRICT_NAME);
//			String regionName = httpRequest.getParameter(Constants.REGION_NAME);
			String licenseeRelation = httpRequest.getParameter(Constants.LICENSEE_RELATION);
			String recommendationType = httpRequest.getParameter(Constants.RECOMMENDATION_TYPE);
			String listOfAssociations = httpRequest.getParameter(Constants.ASSOCIATION_LIST);
			String letterNoIfJigyasu = httpRequest.getParameter(Constants.JIGYASU_LETTER_NO);
			String initiatedParent = httpRequest.getParameter(Constants.INITIATED_PARENT);
			
			TvcMember tvcMember = new TvcMember();
			tvcMember.setFirstName(firstName);
			tvcMember.setMiddleName(emptyStringToNull(middleName));
			tvcMember.setLastName(emptyStringToNull(lastName));
			tvcMember.setGender(gender);
			tvcMember.setAge(emptyStringToNull(age)== null ? null : Integer.parseInt(age));
			tvcMember.setOccupation(emptyStringToNull(occupation));
			tvcMember.setInitiationStatus(initiationStatus);
			tvcMember.setLocalAddress(localAddress);
			tvcMember.setStayCategory(accoType);
			tvcMember.setStayFromDate(new Date());
			tvcMember.setStayToDate(sdf.parse(toDate));
			tvcMember.setPermissionType(permission);
			tvcMember.setBranchId(branch);
			tvcMember.setRegionId(region);
			tvcMember.setDistrictId(district);
			if (emptyStringToNull(isDuplicate) != null) {
				if (isDuplicate.equalsIgnoreCase("false")) {
					tvcMember.setDuplicate(false);
				} else if (isDuplicate.equalsIgnoreCase("true")) {
					tvcMember.setDuplicate(true);
				}
			}
			tvcMember.setAddressCity(emptyStringToNull(city));
			tvcMember.setAddressCountry(emptyStringToNull(country));
			tvcMember.setAddressLine1(emptyStringToNull(homeAddressLine1));
			tvcMember.setAddressLine2(emptyStringToNull(homeAddressLine2));
			tvcMember.setAddressPinCode(emptyStringToNull(pincode));
			tvcMember.setAddressState(emptyStringToNull(state));
			tvcMember.setLocalColony(emptyStringToNull(colony));
			tvcMember.setLocalSadan(emptyStringToNull(sadan));
			tvcMember.setNoOfChildren(emptyStringToNull(childCount)== null ? 0 : Integer.parseInt(childCount));
			tvcMember.setPermissionRemarks(emptyStringToNull(permissionComments));
			tvcMember.setChildrenAges(emptyStringToNull(agesChildren));
//			tvcMember.setBranchName(emptyStringToNull(branchName));
//			tvcMember.setDistrictName(emptyStringToNull(districtName));
//			tvcMember.setRegionName(emptyStringToNull(regionName));
			tvcMember.setTvcId(emptyStringToNull(tvcId));
			tvcMember.setPqLicenseeRelation(emptyStringToNull(licenseeRelation));
			tvcMember.setRecommendationType(emptyStringToNull(recommendationType));
			tvcMember.setLetterNoIfJigyasu(emptyStringToNull(letterNoIfJigyasu));
			tvcMember.setInitiatedParent(emptyStringToNull(initiatedParent));
			
			params.add(tvcMember);
			params.add(event.getBhandaraId());
			
			// add associations --- new feature
			List<TvcAssociateMember> associations = null;
			if(listOfAssociations != null && !listOfAssociations.equals("")){
				associations = new ArrayList<TvcAssociateMember>();
				StringTokenizer tokenizer = new StringTokenizer(listOfAssociations, ";");
				
				while (tokenizer.hasMoreTokens()) {
					String association = tokenizer.nextToken();
					TvcAssociateMember associate = new TvcAssociateMember();
					associate.setTvcId(tvcId);

					StringTokenizer associateTokenizer = new StringTokenizer(association, ",");
					int count = 0;
					while (associateTokenizer.hasMoreTokens()) {
						count++;
						switch (count) {
						case 1:
							associate.setAssociateTvcId(associateTokenizer.nextToken());
							break;
						case 2:
							associate.setAssociateRelationship(associateTokenizer.nextToken());
							break;
						case 3:
							associate.setGender(associateTokenizer.nextToken());
							break;
						default:
						}
					}
					associations.add(associate);
				}
			}

			params.add(associations);
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			}
		} else if (uri.endsWith("search.do")) {
			payload.add(SEARCH_REGISTRATION);

			String firstName = httpRequest.getParameter(Constants.FIRST_NAME);
			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			String lastName = httpRequest.getParameter(Constants.LAST_NAME);
			String branchId = httpRequest.getParameter(Constants.BRANCH);
			
			TvcSearchMember tvcSearch = new TvcSearchMember();
			tvcSearch.setTvcId(emptyStringToNull(tvcId));
			tvcSearch.setFirstName(emptyStringToNull(firstName));
			tvcSearch.setLastName(emptyStringToNull(lastName));
			tvcSearch.setBranchId(emptyStringToNull(branchId));

			params.add(tvcSearch);
			payload.add(params);
			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<Object> searchList = (List<Object>) response.get(1);
				List<TvcMember> tvcList = (List<TvcMember>) searchList.get(0);
				String responseXML = "<root><registrations>";

				for (TvcMember tvc : tvcList) {
					String age = (tvc.getAge() == null || tvc.getAge() == 0) ? "" : String.valueOf(tvc.getAge());
					
					responseXML += "<registration ";
					responseXML += "id=\"" + tvc.getTvcId() + "\" ";

					responseXML += "fName=\"" + tvc.getFirstName()
							+ "\" mName=\"" + nullToEmptyString(tvc.getMiddleName()) 
							+ "\" lName=\"" + nullToEmptyString(tvc.getLastName()) 
							+ "\" age=\"" + nullToEmptyString(age)
							+ "\" sex=\"" + tvc.getGender() 
							+ "\" occupation=\"" + nullToEmptyString(tvc.getOccupation())
							+ "\"";
					responseXML += " initStatus=\"" + tvc.getInitiationStatus()
							+ "\" region=\"" + tvc.getRegionName()
							+ "\" district=\"" + tvc.getDistrictName()
							+ "\" branch=\"" + tvc.getBranchName() + "\"";
					responseXML += " homeAddress1=\"" + nullToEmptyString(tvc.getAddressLine1())
							+ "\" homeAddress2=\"" + nullToEmptyString(tvc.getAddressLine2())
							+ "\" homeCity=\"" + nullToEmptyString(tvc.getAddressCity())
							+ "\" homeState=\"" + nullToEmptyString(tvc.getAddressState()) 
							+ "\" homeCountry=\"" + nullToEmptyString(tvc.getAddressCountry())
							+ "\" pincode=\"" + nullToEmptyString(tvc.getAddressPinCode()) 
							+ "\" permission=\""
							+ tvc.getPermissionType() + "\"";
					responseXML += " permissionComments=\""
							+ nullToEmptyString(tvc.getPermissionRemarks()) + "\" accoType=\""
							+ tvc.getStayCategory() 
							+ "\" colony=\"" + nullToEmptyString(tvc.getLocalColony())
							+ "\" sadan=\"" + nullToEmptyString(tvc.getLocalSadan()) + "\" localAddress=\""
							+ tvc.getLocalAddress() + "\"";
					responseXML += " fromDate=\""
							+ sdf.format(tvc.getStayFromDate())
							+ "\" toDate=\"" + sdf.format(tvc.getStayToDate())
							+ "\" childCount=\"" + nullToEmptyString(Integer.toString(tvc.getNoOfChildren())) 
							+ "\" childrenAges=\"" + nullToEmptyString(tvc.getChildrenAges())
							+ "\" licenseeName=\"" + nullToEmptyString(tvc.getPqLicenseeName())
							+ "\" licenseeRelation=\"" + nullToEmptyString(tvc.getPqLicenseeRelation())
							+ "\" recommendationType=\"" + nullToEmptyString(tvc.getRecommendationType())
							+ "\" letterNo=\"" + nullToEmptyString(tvc.getLetterNoIfJigyasu())
							+ "\" initiatedParent=\"" + nullToEmptyString(tvc.getInitiatedParent())
							+ "\" isAssociate=\"" + tvc.isAssociate()
							+ "\" isDuplicate=\"" + tvc.isDuplicate() + "\"";
					responseXML += " />";
				}
				responseXML += "</registrations></root>";
				out.print(responseXML);
			}
		} else if (uri.endsWith("duplicate.do")) {
			payload.add(DUPLICATE_CARD);

			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			params.add(emptyStringToNull(tvcId));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			}
		} else if (uri.endsWith("associations/view.do")) {
			payload.add(VIEW_ASSOCIATIONS);

			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			params.add(emptyStringToNull(tvcId));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<Object> searchList = (List<Object>) response.get(1);
				List<TvcAssociateMember> associateList = (List<TvcAssociateMember>) searchList.get(0);
				String responseXML = "<root><associations>";

				for (TvcAssociateMember associate : associateList) {
					responseXML += "<association ";
					responseXML += "id=\"" + associate.getAssociateTvcId() + "\" ";
					responseXML += "fName=\"" + associate.getFirstName()
							+ "\" lName=\"" + associate.getLastName() + "\" sex=\""+ associate.getGender() +"\"";
					responseXML += " branch=\"" + associate.getBranchName() + "\"";
					responseXML += " relation=\"" + associate.getAssociateRelationship() + "\" />";
				}
				responseXML += "</associations></root>";
				out.print(responseXML);
			}
		} else if (uri.endsWith("associations/add.do")) {
			payload.add(ADD_ASSOCIATIONS);

			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			String listOfAssociations = httpRequest.getParameter(Constants.ASSOCIATION_LIST);
			List<TvcAssociateMember> associations = new ArrayList<TvcAssociateMember>();

			while (listOfAssociations.contains(";")) {
				String association = listOfAssociations.substring(0, listOfAssociations.indexOf(';'));
				String associateId = association.substring(0, association.indexOf(','));
				String relation = association.substring(association.indexOf(',') + 1, association.length());
				TvcAssociateMember associate = new TvcAssociateMember();
				associate.setAssociateTvcId(emptyStringToNull(associateId));
				associate.setTvcId(emptyStringToNull(tvcId));
				associate.setAssociateRelationship(emptyStringToNull(relation));
				associations.add(associate);
				listOfAssociations = listOfAssociations.substring(listOfAssociations.indexOf(';') + 1, listOfAssociations.length());
			}

			String associateId = listOfAssociations.substring(0, listOfAssociations.indexOf(','));
			String relation = listOfAssociations.substring(listOfAssociations.indexOf(',') + 1, listOfAssociations.length());
			TvcAssociateMember associate = new TvcAssociateMember();
			associate.setAssociateTvcId(emptyStringToNull(associateId));
			associate.setTvcId(emptyStringToNull(tvcId));
			associate.setAssociateRelationship(emptyStringToNull(relation));
			associations.add(associate);

			params.add(tvcId);
			params.add(associations);
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			}
		} else if (uri.endsWith("print.do")) {
			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
//			String printLabelOrCard = httpRequest.getParameter(Constants.PRINT_LABEL_OR_CARD);
			String terminalId = httpRequest.getParameter(Constants.TERMINAL_ID);
			payload.add(SEARCH_REGISTRATION);

			TvcSearchMember tvcSearch = new TvcSearchMember();
			tvcSearch.setTvcId(emptyStringToNull(tvcId));

			params.add(tvcSearch);
			payload.add(params);
			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<Object> searchList = (List<Object>) response.get(1);
				List<TvcMember> tvcList = (List<TvcMember>) searchList.get(0);

				TvcMember tvc = tvcList.get(0);
				if (tvc != null) {
					payload.remove(3);
					payload.add(3, VIEW_ASSOCIATIONS);
					payload.remove(4);
					params = new ArrayList<Object>();
					params.add(tvcId);
					payload.add(4, params);
					List<TvcAssociateMember> associateList = null;
					response = masterController.processRequest(masterPayload);
					messageMap = (Map<String, String>) response.get(0);
					if (messageMap.get(Constants.ERROR) != null) {
						out.print(messageMap.get(Constants.ERROR));
					} else if (messageMap.get(Constants.INFO) != null) {
						out.print(messageMap.get(Constants.INFO));
					} else {
						searchList = (List<Object>) response.get(1);
						associateList = (List<TvcAssociateMember>) searchList.get(0);
					}
//					if(printLabelOrCard.equals("Label")){
						printFactory.getPrinterPool("Label").getPrinter(terminalId).print(event, tvc, associateList);
//					}else if(printLabelOrCard.equals("Card")){
//						printFactory.getPrinterPool("Card").getPrinter().print(event, tvc, associateList);
//					}
				}
			}
		} else if (uri.endsWith("quarters.do")) {
			payload.add(SQ_STATS);

			String colonyName = httpRequest.getParameter(Constants.COLONY_NAME);
			String noOfPeople = httpRequest.getParameter(Constants.NO_OF_PEOPLE);
			params.add(emptyStringToNull(colonyName));
			params.add(emptyStringToNull(noOfPeople) == null ? null :Integer.parseInt(noOfPeople));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				HashMap<Integer, List<String>> vacancyMap = (HashMap<Integer, List<String>>) ((List) response.get(1)).get(0);
				String responseXml = "<root><vacancies>";
				if(vacancyMap != null){
					Iterator<Integer> vacancyIter = vacancyMap.keySet().iterator();
					while (vacancyIter.hasNext()) {
						Integer vacancy = vacancyIter.next();
						List<String> houseList = vacancyMap.get(vacancy);
						if(houseList != null){
							for (String houseNo : houseList) {
								responseXml += "<vacancy count=\"" + nullToEmptyString(vacancy.toString())
										+ "\" quarter=\"" + nullToEmptyString(houseNo) + "\" />";
							}
						}
					}					
				}	
				responseXml += "</vacancies></root>";
				out.print(responseXml);
			}
		} else if (uri.endsWith("sadans.do")) {
			payload.add(SADAN_STATS);
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				HashMap<String, SadanStats> sadanStats = (HashMap<String, SadanStats>) ((List) response.get(1)).get(0);
				String responseXml = "<root><sadans>";
				if(sadanStats != null){
					Iterator<String> sadanIter = sadanStats.keySet().iterator();
					while (sadanIter.hasNext()) {
						String sadan = sadanIter.next();
						SadanStats sadanInfo = sadanStats.get(sadan);
						if(sadanInfo != null){
						responseXml += "<sadan name=\"" + nullToEmptyString(sadan) + "\" capacity=\""
								+ sadanInfo.getCapacity() + "\" numMale=\""
								+ sadanInfo.getMaleCount() + "\" numFemale=\""
								+ sadanInfo.getFemaleCount() + "\" numChild=\""
								+ sadanInfo.getChildCount() + "\" used=\""
								+ sadanInfo.getOccupancy() + "\" free=\""
								+ sadanInfo.getVacancy() + "\"/>";
						}
					}					
				}
				responseXml += "</sadans></root>";
				out.print(responseXml);
			}
		} else if (uri.endsWith("licensee.do")){
			payload.add(LICENSEE_DETAILS);
			String quarterNo = httpRequest.getParameter(Constants.LOCAL_ADDRESS);
			String colonyName = httpRequest.getParameter(Constants.COLONY_NAME);
			params.add(emptyStringToNull(quarterNo));
			params.add(emptyStringToNull(colonyName));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				HashMap<String, String> licenseeDetails = (HashMap<String, String>)((List)response.get(1)).get(0);
				String responseXml = "<root>";
				if(licenseeDetails != null){
					responseXml += "<licensee quarterNo=\""+ licenseeDetails.get("quarterNo")
											+"\" name=\""+licenseeDetails.get("licenseeName")
											+"\" colony=\""+licenseeDetails.get("associatedColonyName")+"\" />";
					
				}	
				responseXml+= "</root>";
				out.print(responseXml);
			}
		}else if (uri.endsWith("privateQuarters.do")){
			payload.add(PQ_LIST);
			String colonyName = httpRequest.getParameter(Constants.COLONY_NAME);
			params.add(emptyStringToNull(colonyName));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				List<HashMap<String, String>> pqList = (List<HashMap<String, String>>)((List)response.get(1)).get(0);
				String responseXml = "<root><PrivateQuarterDetails>";				
				for(HashMap<String, String> quarter: pqList){
					responseXml += "<PQEntry number=\""+ quarter.get("quarterNo")
					+"\" LicenseeName=\""+ quarter.get("licenseeName")
					+"\" AssocColony=\""+quarter.get("associatedColonyName")+"\" />";
				}	
				responseXml+= "</PrivateQuarterDetails></root>";
				out.print(responseXml);
			}
		}else if (uri.endsWith("associations/primary.do")){
			payload.add(PRIMARY_DETAILS);
			String associateTvcId = httpRequest.getParameter(Constants.ASSOCIATE_TVC_ID);
			params.add(emptyStringToNull(associateTvcId));
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				String tvcId = (String)((List)response.get(1)).get(0);
				String responseXML = "<root>";
				
				if(tvcId != null){
					responseXML += "<primary ";
					responseXML += "id=\"" + tvcId + "\" />";
				}
				
				responseXML+= "</root>";
				out.print(responseXML);
			}
		}
		else if (uri.endsWith("associations/validate.do")){
			payload.add(VALIDATE_ASSOCIATE);
			String tvcId = httpRequest.getParameter(Constants.TVC_ID);
			String associateTvcId = httpRequest.getParameter(Constants.ASSOCIATE_TVC_ID);
			String associateRelation = httpRequest.getParameter(Constants.ASSOCIATE_RELATION);
			TvcAssociateMember associate = new TvcAssociateMember();
			associate.setAssociateRelationship(emptyStringToNull(associateRelation));
			associate.setAssociateTvcId(emptyStringToNull(associateTvcId));
			associate.setTvcId(tvcId);
			params.add(associate);
			payload.add(params);

			response = masterController.processRequest(masterPayload);
			Map<String, String> messageMap = (Map<String, String>) response.get(0);
			if (messageMap.get(Constants.ERROR) != null) {
				out.print(messageMap.get(Constants.ERROR));
			} else if (messageMap.get(Constants.INFO) != null) {
				out.print(messageMap.get(Constants.INFO));
			} else {
				String status = (String)((List)response.get(1)).get(0);
				String responseXML = "<root>";
				
				if(status != null){
					responseXML += "<status ";
					responseXML += "msg=\"" + status + "\"" ;
					if("Success".equals(status)){
						responseXML += " fName=\"" + associate.getFirstName() + "\"" ;
						responseXML += " lName=\"" + associate.getLastName() + "\"" ;
						responseXML += " branchName=\"" + associate.getBranchName() + "\"" ;
						responseXML += " gender=\"" + associate.getGender() + "\"" ;
						responseXML += " relation=\"" + associate.getAssociateRelationship() + "\"" ;
						responseXML += " associateId=\"" + associate.getAssociateTvcId() + "\"" ;
					}					
					responseXML += " />";
				}
				
				responseXML+= "</root>";
				out.print(responseXML);
			}
		}
		else {
			httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		return null;
	}
	
	private String nullToEmptyString(String nullString) {
		return nullString == null ? "" : nullString;
	}
	
	private String emptyStringToNull(String emptyString) {
		return emptyString == "" ? null : emptyString;
	}
}
