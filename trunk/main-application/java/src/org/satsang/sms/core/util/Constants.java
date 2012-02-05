/**
 * 
 */
package org.satsang.sms.core.util;

/**
 * @author Default
 *
 */
public class Constants {

	// SPECIAL MESSAGES
	public static final String ERROR = "Error";
	public static final String INFO = "Info";
	public static final String DEBUG = "Debug";
	
	// REQUESTS - MASTER CONTROLLER
	public static final String EVENT_REQUEST = "Event";
	public static final String ACCOUNT_REQUEST = "Account";
	public static final String COMMUNITY_REQUEST = "Community";
	public static final String MEMBER_REQUEST = "Member";
	
	// REQUESTS - ACCOUNT CONTROLLER
	public static final String CREATE_ACCOUNT = "create";
	public static final String LOGIN_ACCOUNT = "login";
	public static final String UPDATE_ACCOUNT = "update";
	public static final String DISABLE_ACCOUNT = "disable";
	public static final String LOGOUT_ACCOUNT = "logout";
	
	// REQUESTS - EVENT CONTROLLER
	public static final String CREATE_EVENT = "create";
	public static final String START_ACTIVITY = "startActivity";
	public static final String END_ACTIVITY = "endActivity";
	public static final String DO_ACTIVITY = "doActivity";
	public static final String ENABLE_EVENT = "activate";
	public static final String DISABLE_EVENT = "deactivate";	
	public static final String SEARCH_EVENTS = "search";	
	public static final String REACTIVATE_EVENT = "reactivate";	
	
	// REQUESTS - COMMUNITY CONTROLLER
	public static final String REGIONS_REQUEST = "regions";
	public static final String DISTRICTS_REQUEST = "districts";
	public static final String BRANCHES_REQUEST = "branches";
	public static final String BRANCH_BY_ID_REQUEST = "branch";
	public static final String ALL_BRANCHES_REQUEST = "allBranches";
	
	// REQUESTS - MEMBER CONTROLLER
	public static final String MEMBER_DETAILS_REQUEST = "memberDetails";
	
	// BEANS
	public static final String MASTER_CONTROLLER = "MasterController";
	public static final String ACCOUNT_CONTROLLER = "AccountController";
	public static final String EVENT_CONTROLLER = "EventController";
	public static final String EVENT = "Event";
	public static final String ACCOUNT = "Account";
	
	// HTTP REQUEST PARAMETERS
	public static final String USER_ID = "UserId";
	public static final String PASSWORD = "Password";
	public static final String ACTIVITY_NAME = "ActivityName";
	public static final String ACTIVITY_DESCRIPTION = "ActivityDescription";
	public static final String REPORT_TYPE = "ReportType";
	public static final String REPORT_NAME = "ReportName";
	public static final String BRANCH_SEARCH_STRING = "BranchSearch";
	public static final String EVENT_ID = "EventId";
	public static final String EVENT_NAME = "EventName";
	public static final String EVENT_DESC = "EventDesc";
	public static final String COMMUNITY_ID = "CommunityId";
	public static final String CONTEXT_NAME = "ContextName";
	public static final String IS_DEFAULT = "IsDefault";
	public static final String SEARCH_FROM_DATE = "SearchFromDate";
	public static final String SEARCH_TO_DATE = "SearchToDate";
	public static final String ASSOCIATION_LIST = "AssocString";
	public static final String REPORT_PARAMS = "ReportParams";
	public static final String COLONY_NAME = "ColonyName";
	public static final String NO_OF_PEOPLE = "NoOfPeople";
	public static final String SADAN_NAME = "SadanName";
	public static final String MEMBER_ID = "MemberId";
	public static final String PRINT_LABEL_OR_CARD = "PrintLabelOrCard";
	public static final String ASSOCIATE_TVC_ID = "AssociateTvcId";
	public static final String ASSOCIATE_RELATION = "AssociateRelation";
	
	
	public static final String TVC_ID = "TvcId";
	public static final String FIRST_NAME = "FirstName";
	public static final String MIDDLE_NAME = "MiddleName";
	public static final String LAST_NAME = "LastName";
	public static final String AGE = "Age";
	public static final String OCCUPATION = "Occupation";
	public static final String GENDER = "Gender";
	public static final String INITIATION_STATUS = "InitiationStatus";
	public static final String BRANCH = "Branch";
	public static final String DISTRICT = "District";
	public static final String REGION = "Region";
	public static final String HOME_ADDRESS_LINE1 = "HomeAddress1";
	public static final String HOME_ADDRESS_LINE2 = "HomeAddress2";
	public static final String CITY = "City";
	public static final String STATE = "State";
	public static final String COUNTRY = "Country";
	public static final String PINCODE = "Pincode";
	public static final String PERMISSION = "Permission";
	public static final String PERMISSION_COMMENTS = "PermissionRemarks";
	public static final String ACCOMODATION_TYPE = "AccoType";
	public static final String COLONY = "Colony";
	public static final String SADAN = "Sadan";
	public static final String LOCAL_ADDRESS = "LocalAddress";
	public static final String FROM_DATE = "FromDate";
	public static final String TO_DATE = "ToDate";
	public static final String NO_OF_CHILDREN = "ChildCount";
	public static final String CHILDREN_AGES = "ChildrenAges";
	public static final String IS_DUPLICATE = "IsDuplicate";
	public static final String BRANCH_NAME = "BranchName";
	public static final String DISTRICT_NAME = "DistrictName";
	public static final String REGION_NAME = "RegionName";
	public static final String BHANDARA_ID = "BhandaraId";
	public static final String LICENSEE_RELATION = "LicenseeRelation";
	public static final String RECOMMENDATION_TYPE = "RecommendationType";
	public static final String TERMINAL_ID = "TerminalID";
	public static final String IS_ASSOCIATE = "IsAssociate";
	public static final String JIGYASU_LETTER_NO = "JigyasuLetterNo";
	public static final String INITIATED_PARENT = "InitiatedParent";
	
	
	// HTTP SESSION ATTRIBUTES
	public static final String LOGGED_IN_ACCOUNT = "LoggedInAccount";
	public static final String SELECTED_EVENT = "SelectedEvent";
	
	
}
