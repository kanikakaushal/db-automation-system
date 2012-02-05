/**
 * 
 */
package org.satsang.sms.core.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.communities.interfaces.ICommunityHandler;
import org.satsang.sms.core.controllers.interfaces.IController;
import org.satsang.sms.core.util.Constants;
import org.satsang.sms.core.util.RequestInvoker;

/**
 * @author gcapriha
 * 
 */
public class CommunityController implements IController {

	private ICommunityHandler communityHandler;

	private List<String> communityRequests = Arrays.asList(new String[] {
			Constants.REGIONS_REQUEST, Constants.DISTRICTS_REQUEST,
			Constants.BRANCHES_REQUEST, Constants.ALL_BRANCHES_REQUEST });

	/**
	 * @param communityHandler
	 *            the communityHandler to set
	 */
	public void setCommunityHandler(ICommunityHandler communityHandler) {
		this.communityHandler = communityHandler;
	}

	@SuppressWarnings("unchecked")
	public List<Object> processRequest(List<Object> payload) {

		// Create a default response object
		List<Object> response = new ArrayList<Object>();
		Map<String, String> messageMap = defaultReturnMap();
		response.add(messageMap);

		// Check payload
		if (payload == null) {
			messageMap.put(Constants.ERROR, "Community payload undefined");
			return response;
		}

		Object request = payload.get(0);

		// Parse Request
		if (request == null) {
			messageMap.put(Constants.ERROR, "Community request undefined");
			return response;
		}

		if (!communityRequests.contains(request)) {
			messageMap.put(Constants.ERROR, "Unknown Community request");
			return response;
		}

		List<Object> communityPayload = payload.size() == 1 ? null
				: (List) payload.get(1);
		Object receivedResponse = null;

		try {
			receivedResponse = RequestInvoker.invoke(communityHandler, request
					.toString(), communityPayload);
		} catch (Exception e) {
			messageMap.put(Constants.ERROR, "Inappropriate Community payload");
			messageMap.put(Constants.DEBUG, e.getMessage());
			return response;
		}
		if (receivedResponse != null)
			response.add(receivedResponse);
		return response;
	}

	public Map<String, String> defaultReturnMap() {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(Constants.ERROR, null);
		returnMap.put(Constants.INFO, null);
		returnMap.put(Constants.DEBUG, null);
		return returnMap;
	}

	// private List<String> communityRequests = Arrays.asList(new String[] {});
	//
	// /*
	// * The listOfRegisteredCommunityTemplates is a HashMap containing pointers
	// * to "templates" for each of the registered communities indexed on the
	// * community name. These templates are used to create instances of each
	// * community type. For example, creating instances of BranchSatsang or any
	// * other community
	// */
	// private HashMap listOfRegisteredCommunityTemplates = new HashMap();
	// /*
	// * The listOfRegisteredCommunities is a "HashMap of HashMaps" in which
	// each
	// * toplevel hashelement actually points to a community specific hashmap of
	// * all the registered elements of that community
	// */
	// private HashMap listOfRegisteredCommunities = new HashMap();
	//
	// public CommunityController() {
	// };
	//
	// public CommunityController(Map aMap) {
	// String aCommunityName;
	// Collection c = aMap.keySet();
	// Iterator itr = c.iterator();
	//
	// while (itr.hasNext()) {
	// aCommunityName = (String) itr.next();
	// // List the communityTemplate in the RegisteredCommunityTemplate
	// // HashMap
	// this.listOfRegisteredCommunityTemplates.put(aCommunityName,
	// (String) aMap.get(aCommunityName));
	// 
	// /*
	// * Also make a corresponding entry in the "HashMap-Of-HashMaps
	// * listOfRegisteredCommunities with this particular community name
	// * as the Key! This is going to be used hereon to store references
	// * to all instances of a particular community
	// */
	// HashMap aCommunityInstanceHashMap = new HashMap();
	// this.listOfRegisteredCommunities.put((String) aMap
	// .get(aCommunityName), aCommunityInstanceHashMap);
	// }
	// }
	//
	// public void setCommunityInstanceMap(Map aMap) {
	// ICommunity aCommunityInstance;
	// String anInstanceName = "";
	// Collection c = aMap.keySet();
	// Iterator itr = c.iterator();
	// HashMap aHandleToTheCommunityHashMap;
	//
	// while (itr.hasNext()) {
	// anInstanceName = (String) itr.next();
	// aCommunityInstance = (ICommunity) aMap.get(anInstanceName);
	// if (this.listOfRegisteredCommunities.containsKey(aCommunityInstance
	// .getClass().getSimpleName())) {
	// /*
	// * Now that it exists, do three things. One, go into the list of
	// * ListOfRegisteredCommunities and get a reference to the
	// * specific hashmap corresponding for that community name. Two,
	// * look inside that HashMap and see whether an instance already
	// * exists for the requested instance. Three, only if no instance
	// * exists, go ahead and create one!
	// */
	// aHandleToTheCommunityHashMap = (HashMap) this.listOfRegisteredCommunities
	// .get(aCommunityInstance.getClass().getSimpleName());
	// if (!aHandleToTheCommunityHashMap.containsKey(anInstanceName)) {
	// /* Setting the community name */
	// aCommunityInstance.setInstanceName(anInstanceName);
	//
	// /* Adding this instance to the specific community HashMap */
	// aHandleToTheCommunityHashMap.put(anInstanceName,
	// aCommunityInstance);
	// }
	// }
	// }
	// }
	//
	// /*
	// * Each element of the Map contains a set containing an <entry> and
	// <value>.
	// * We will now extract each of these sets corresponding to all the
	// * communities that have been defined and add them to the
	// * listOfRegisteredCommunities HashMap associated with the
	// * CommunityController
	// */
	//
	// /*
	// * public void setCommunityMap(Map aMap) { String aCommunityName;
	// Collection
	// * c = aMap.keySet(); Iterator itr = c.iterator();
	// *
	// * while(itr.hasNext()) { aCommunityName = (String) itr.next(); //List the
	// * communityTemplate in the RegisteredCommunityTemplate HashMap
	// * this.listOfRegisteredCommunityTemplates.put(aCommunityName,
	// * aMap.get(aCommunityName)); Also make a corresponding entry in the
	// * "HashMap-Of-HashMaps listOfRegisteredCommunities with this particular
	// * community name as the Key! This is going to be used hereon to store
	// * references to all instances of a particular community HashMap
	// * aCommunityInstanceHashMap = new HashMap();
	// * this.listOfRegisteredCommunities.put(aCommunityName,
	// * aCommunityInstanceHashMap); } }
	// */
	//
	// /*
	// * This method returns the list of communities (templates) that have been
	// * registered with this controller.
	// */
	// public Set getCommunitySet() {
	// return this.listOfRegisteredCommunityTemplates.keySet();
	// }
	//
	// /*
	// * This method would return a set containing all the names of the
	// instances
	// * of a particular community
	// */
	// public Set getCommunityInstanceSet(String aCommunityName) {
	// Set resultSet = null;
	// /*
	// * First off, check whether there is a community with that name
	// */
	// if (this.listOfRegisteredCommunities.containsKey(aCommunityName)) {
	// resultSet = ((HashMap) this.listOfRegisteredCommunities
	// .get(aCommunityName)).keySet();
	// }
	//
	// return resultSet;
	// }
	//
	// /*
	// * This method returns a Set containing the list of Context in a given
	// * Community
	// */
	// public Set getCommunityInstanceContextSet(String aCommunityName,
	// String instanceName) {
	// Set resultSet = null;
	// if (this.listOfRegisteredCommunities.containsKey(aCommunityName)) {
	// HashMap aHandleToACommunity = (HashMap) this.listOfRegisteredCommunities
	// .get(aCommunityName);
	// if (aHandleToACommunity.containsKey(instanceName)) {
	// ICommunity aHandleToAnInstance = (ICommunity) aHandleToACommunity
	// .get(instanceName);
	// resultSet = aHandleToAnInstance.getContextList();
	// }
	// }
	// return resultSet;
	// }
	//
	// /*
	// * This method returns a Set containing the list of methods within a
	// * specified Context of a specified Community
	// */
	// public HashMap getCommunityInstanceContextActivitySet(
	// String aCommunityName, String instanceName, String aContextName) {
	// // HashMap resultSet = null;
	// // if (this.listOfRegisteredCommunities.containsKey(aCommunityName)) {
	// // HashMap aHandleToTheCommunity = (HashMap)
	// // this.listOfRegisteredCommunities
	// // .get(aCommunityName);
	// // if (aHandleToTheCommunity.containsKey(instanceName)) {
	// // ICommunity aHandleToTheInstance = (ICommunity) aHandleToTheCommunity
	// // .get(instanceName);
	// // resultSet = aHandleToTheInstance
	// // .getContextActivities(aContextName);
	// // }
	// // }
	// // return resultSet;
	// return null;
	// }
	//
	// /*
	// * public Boolean createNewCommunityInstance(String communityName, String
	// * instanceName) { Boolean result = Boolean.FALSE; HashMap
	// * aHandleToTheCommunityHashMap; //First off, check whether there
	// requested
	// * community template exists or not!
	// * if(this.listOfRegisteredCommunities.containsKey(communityName)) { Now
	// * that it exists, do three things. One, go into the list of
	// * ListOfRegisteredCommunities and get a reference to the specific hashmap
	// * corresponding for that community name. Two, look inside that HashMap
	// and
	// * see whether an instance already exists for the requested instance.
	// Three,
	// * only if no instance exists, go ahead and create one!
	// * aHandleToTheCommunityHashMap = (HashMap)
	// * this.listOfRegisteredCommunities.get(communityName);
	// * if(!aHandleToTheCommunityHashMap.containsKey(instanceName)) {
	// ICommunity
	// * aNewInstance = (ICommunity) Utility.getNewInstance(communityName);
	// *
	// * Setting the community name Boolean aReturnValue =
	// * aNewInstance.setInstanceName(instanceName); Adding this instance to the
	// * specific community HashMap
	// aHandleToTheCommunityHashMap.put(instanceName,
	// * aNewInstance); result = Boolean.TRUE; } }
	// *
	// * return result; }
	// */
	//
	// /*
	// * This method is called by Master Controller to get a handle to
	// particular
	// * community instance e.g. for setting up an event or anyother similar
	// * purpose!
	// */
	// public ICommunity getHandleToCommunityInstance(String communityName,
	// String instanceName) {
	// ICommunity anInstance = null;
	// if (((HashMap) this.listOfRegisteredCommunities.get(communityName))
	// .containsKey(instanceName))
	// anInstance = (ICommunity) ((HashMap) this.listOfRegisteredCommunities
	// .get(communityName)).get(instanceName);
	// return anInstance;
	// }
	//
	// public IContext getHandleToCommunityContext(String aCommunityName,
	// String aContextName) {
	// ICommunity aHandleToACommunity = (ICommunity)
	// this.listOfRegisteredCommunities
	// .get(aCommunityName);
	// return aHandleToACommunity.getHandleToContext(aContextName);
	// }

}
