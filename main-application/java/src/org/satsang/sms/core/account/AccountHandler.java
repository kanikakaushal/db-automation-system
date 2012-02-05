/**
 * 
 */
package org.satsang.sms.core.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.satsang.sms.core.account.interfaces.IAccount;
import org.satsang.sms.core.account.interfaces.IAccountDao;
import org.satsang.sms.core.account.interfaces.IAccountHandler;
import org.satsang.sms.core.account.interfaces.IAccountPrivDao;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.context.interfaces.IContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author Default
 * 
 */
public class AccountHandler implements IAccountHandler, ApplicationContextAware {

	private IAccountDao handleToAccountDAO;
	private IAccountPrivDao handleToAccountPrivDAO;
	private ApplicationContext appContext;
	private static Logger log = Logger.getLogger(AccountHandler.class);
	
	/* Spring Setters */
	/**
	 * @param handleToAccountDAO
	 *            the aHandleToAccountDAO to set
	 */
	public void setHandleToAccountDAO(IAccountDao handleToAccountDAO) {
		this.handleToAccountDAO = handleToAccountDAO;
	}

	/**
	 * @param handleToAccountPrivDAO
	 *            the aHandleToAccountPrivDAO to set
	 */
	public void setHandleToAccountPrivDAO(IAccountPrivDao handleToAccountPrivDAO) {
		this.handleToAccountPrivDAO = handleToAccountPrivDAO;
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.appContext = context;
	}

	@Override
	public IAccount create(String accountID, String accountPassword,
			String accountEnabled) {

		Map<String, String> resultMap = handleToAccountDAO.createAccount(
				accountID, accountPassword, accountEnabled);

		IAccount account = (IAccount) appContext.getBean("Account");
		account.setAccountId(resultMap.get("accountId"));
		account.setAccountAlias(resultMap.get("accountAlias"));
		account.setAccountPassword(resultMap.get("password"));
		account.setAccountEnabled(resultMap.get("isEnabled"));
		log.info("Successfully created account with id "
				+ accountID);
		return account;
	}

	@Override
	public void disable(IAccount accountRef) {
		handleToAccountDAO.disableAccount(accountRef);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IAccount login(String accountID, String password) {
		
		Map<String, String> resultMap = handleToAccountDAO.getAccount(
				accountID, password);
		
		if (resultMap == null || resultMap.isEmpty()){
			log.debug("No account exists with  id "+ accountID);			
			return null;
		}

		IAccount account = (IAccount) appContext.getBean("Account");
		account.setAccountId(resultMap.get("accountId"));
		account.setAccountAlias(resultMap.get("accountAlias"));
		account.setAccountPassword(resultMap.get("password"));
		account.setAccountEnabled(resultMap.get("isEnabled"));
		log.debug("Fetched account "+ resultMap.get("accountId") +" from database ");
		log.debug("Account name -  "+ resultMap.get("accountAlias"));
		

		HashMap<String, List<Object>> accountProfiles = getProfiles(handleToAccountPrivDAO
				.getAccountRolesList(accountID));
		
		// The account has no privileges...
		if (accountProfiles == null || accountProfiles.isEmpty()) {
//			account.setCommunityMap(null);
//			account.setProfileList(null);
//			account.setProfileCommunityMap(null);
			log.info("The account " + accountID
					+ " has no privileges");
			return account;
		}

		// Compute account access
		Iterator<String> roleIterator = accountProfiles.keySet().iterator();
		List<Profile> roleList = new ArrayList<Profile>();

		HashMap<Integer, List<ICommunity>> roleCommunities = getProfileCommunityMap(handleToAccountPrivDAO
				.getAccountAccessibleCommunities(accountID));

		Map<String, ICommunity> communityMap = new HashMap<String, ICommunity>();

		while (roleIterator.hasNext()) {
			String roleName = roleIterator.next();
			List<Object> profileList = accountProfiles.get(roleName);
			Profile profile = (Profile) profileList.get(0);
			roleList.add(profile);

			Map<String, Object> objectMap = (Map<String, Object>) profileList
					.get(1);
			Iterator<String> objectIterator = objectMap.keySet().iterator();

			HashMap<String, IContext> contextMap = null;
			if (!objectMap.containsKey("0")) {
				contextMap = new HashMap<String, IContext>();
				while (objectIterator.hasNext()) {
					String contextId = objectIterator.next();
					IContext context = (IContext) objectMap.get(contextId);
					contextMap.put(context.getContextName(), context);
				}
			}

			List<ICommunity> communities = roleCommunities.get(profile
					.getRoleId());
			log.info("The account " + accountID + " with role "
					+ roleName + " can access these communities : "
					+ communities);
			for (ICommunity aCommunity : communities) {

				communityMap.put(aCommunity.getInstanceID(), aCommunity);
				HashMap<String, IContext> commContextMap = aCommunity
						.getContextMap();
				Iterator<String> contextIterator = commContextMap.keySet()
						.iterator();

				if (objectMap.containsKey("0")) {
					HashMap<String, IActivity> activityMap = (HashMap<String, IActivity>) objectMap
							.get("0");

					if (activityMap != null) {
						while (contextIterator.hasNext()) {
							IContext context = commContextMap
									.get(contextIterator.next());
							HashMap<String, IActivity> contextActivityMap = context
									.getActivityMap();

							for (Iterator<Map.Entry<String, IActivity>> activityIterator = activityMap
									.entrySet().iterator(); activityIterator
									.hasNext();) {
								Map.Entry<String, IActivity> entry = activityIterator
										.next();
								String activityName = entry.getKey();
								if (!contextActivityMap
										.containsKey(activityName)) {
									activityIterator.remove();
								}
							}

							if (activityMap.isEmpty())
								context.setActivityMap(null);
							else
								context.setActivityMap(activityMap);
						}
					}

					objectIterator = objectMap.keySet().iterator();
					objectIterator.next();
					while (objectIterator.hasNext()) {
						String contextId = objectIterator.next();
						if (!contextId.equals("0")) {
							IContext retrievedContext = (IContext) objectMap
									.get(contextId);
							IContext context = commContextMap
									.get(retrievedContext.getContextName());

							if (context != null) {
								HashMap<String, IActivity> contextActivityMap = context
										.getActivityMap();

								HashMap<String, IActivity> retrievedActivityMap = retrievedContext
										.getActivityMap();
								if (contextActivityMap == null) {
									contextActivityMap = retrievedActivityMap;
									context.setActivityMap(contextActivityMap);
								} else {
									contextActivityMap
											.putAll(retrievedActivityMap);
								}
							}
						}
					}
					continue;
				}

				for (Iterator<Map.Entry<String, IContext>> contIterator = commContextMap
						.entrySet().iterator(); contIterator.hasNext();) {
					Map.Entry<String, IContext> entry = contIterator.next();

					String contextName = entry.getKey();
					IContext context = entry.getValue();

					if (!contextMap.containsKey(contextName)) {
						contIterator.remove();
						continue;
					}

					context.setContextId(contextMap.get(contextName)
							.getContextId());
					HashMap<String, IActivity> contextActivityMap = contextMap
							.get(contextName).getActivityMap();

					if (contextActivityMap == null)
						continue;

					HashMap<String, IActivity> commContextActivityMap = context
							.getActivityMap();

					for (Iterator<Map.Entry<String, IActivity>> activityIterator = contextActivityMap
							.entrySet().iterator(); activityIterator.hasNext();) {
						Map.Entry<String, IActivity> mapEntry = activityIterator
								.next();
						String activityName = mapEntry.getKey();
						if (!commContextActivityMap.containsKey(activityName)) {
							activityIterator.remove();
						}
					}
					if (contextActivityMap.isEmpty())
						context.setActivityMap(null);
					else
						context.setActivityMap(contextActivityMap);
				}
				if (commContextMap.isEmpty())
					aCommunity.setContextMap(null);
				else
					aCommunity.setContextMap(commContextMap);

			}
		}

		Iterator<String> communityIterator = communityMap.keySet().iterator();

		while (communityIterator.hasNext()) {

			HashMap<String, IContext> contextMap = communityMap.get(
					communityIterator.next()).getContextMap();
			if (contextMap == null)
				continue;
			Iterator<String> commuIterator = contextMap.keySet().iterator();
			while (commuIterator.hasNext()) {
				String contextName = commuIterator.next();
				IContext context = contextMap.get(contextName);
				if (context.getContextId() == 0) {
					// fire query to get context id for the context name
					// and set the context id in context
					context.setContextId(handleToAccountPrivDAO
							.getContextIdFromName(contextName));
				}
				HashMap<String, IActivity> activityMap = context
						.getActivityMap();
				if (activityMap == null)
					continue;
				Iterator<String> activityIterator = activityMap.keySet()
						.iterator();
				while (activityIterator.hasNext()) {
					String activityName = activityIterator.next();
					IActivity activity = activityMap.get(activityName);
					if (activity.getActivityId() == 0) {
						// fire query to get activity id for the activity
						// name
						// and set the activity id in activity
						int activityId = handleToAccountPrivDAO
								.getActivityIdFromName(activityName);
						activity.setActivityId(activityId);
					}
				}
			}
		}
		// construct roleCommunityMap
		HashMap<String, List<ICommunity>> roleCommunityMap = new HashMap<String, List<ICommunity>>();

		for (Profile profile : roleList) {
			if (!roleCommunities.containsKey(profile.getRoleId())) {
				roleCommunities.remove(profile.getRoleId());
			}
			List<ICommunity> communityList = roleCommunities.get(profile
					.getRoleId());
			roleCommunityMap.put(profile.getRoleName(), communityList);
		}

		account.setProfileCommunityMap(roleCommunityMap);
		account.setCommunityMap(communityMap);
		account.setProfileList(roleList);
		log.info(account.getAccountAlias() + " has logged in.");
		return account;
	}

	@Override
	public void update(IAccount accountRef) {
		handleToAccountDAO.updateAccountPassword(accountRef);
	}

	/* Internal methods */
	@SuppressWarnings("unchecked")
	private HashMap<String, List<Object>> getProfiles(
			List<Map<String, Object>> roleList) {
		String tempRoleId = null;
		// declare context map
		HashMap<String, Object> contextMap = null;
		HashMap<String, IActivity> activityMap = null;
		String tempContextName = null;
		String tempActivityName = null;

		if (roleList == null || roleList.isEmpty())
			return null;

		// key: role name, value: list {profile, hashmap<context> |
		// hashmap<activity> when context 0}
		HashMap<String, List<Object>> roleMap = new HashMap<String, List<Object>>();

		for (int i = 0; i < roleList.size(); i++) {
			Map<String, Object> role = roleList.get(i);

			String roleId = ((Long) role.get("roleId")).toString();
			String roleName = (String) role.get("roleName");
			String description = (String) role.get("roleDesc");
			String contextId = ((Long) role.get("contextId")).toString();
			String contextName = (String) role.get("contextName");
			String activityId = ((Long) role.get("activityId")).toString();
			String activityName = (String) role.get("activityName");

			if (!roleId.equals(tempRoleId)) {
				Profile profile = new Profile();
				// list containing profile object, hashmap<context> |
				// hashmap<activity> when context 0
				List<Object> profileInfo = new ArrayList<Object>();

				profile.setRoleId(Integer.parseInt(roleId));
				profile.setRoleName(roleName);
				profile.setDescription(description);
				// add profile to list
				profileInfo.add(profile);

				contextMap = new HashMap<String, Object>();
				profileInfo.add(contextMap);

				activityMap = new HashMap<String, IActivity>();
				if (activityId.equals("0"))
					activityMap = null;
				roleMap.put(roleName, profileInfo);

				tempContextName = null;
				tempActivityName = null;
			}
			if (!contextName.equals(tempContextName)) {
				activityMap = new HashMap<String, IActivity>();
				if (activityId.equals("0"))
					activityMap = null;

				if (contextId.equals("0")) {
					contextMap.put(contextId, activityMap);
				}
				if (!contextId.equals("0")) {
					try {
						IContext contextInstance = (IContext) appContext
								.getBean(contextName);
						contextInstance.setContextName(contextName);
						contextInstance.setContextId(Integer
								.parseInt(contextId));
						contextInstance.setActivityMap(activityMap);

						contextMap.put(contextId, contextInstance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				tempActivityName = null;
			}
			if (!activityName.equals(tempActivityName)) {
				if (!activityId.equals("0") && activityMap != null) {
					try {
						IActivity activityInstance = (IActivity) appContext
								.getBean(activityName);
						activityInstance.setActivityId(Integer
								.parseInt(activityId));
						activityInstance.setActivityName(activityName);
						activityMap.put(activityName, activityInstance);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			tempRoleId = roleId;
			tempContextName = contextName;
			tempActivityName = activityName;
		}

		for (Iterator<Map.Entry<String, List<Object>>> roleIter = roleMap
				.entrySet().iterator(); roleIter.hasNext();) {
			Map.Entry<String, List<Object>> entry = roleIter.next();

			List<Object> objList = entry.getValue();
			if (objList == null || objList.isEmpty()) {
				roleIter.remove();
				continue;
			}

			HashMap<String, Object> map = (HashMap<String, Object>) objList
					.get(1);
			if (map == null || map.isEmpty()) {
				roleIter.remove();
				continue;
			}
		}
		return roleMap;
	}

	private HashMap<Integer, List<ICommunity>> getProfileCommunityMap(
			List<Map<String, Object>> communityList) {
		String tempRoleId = null;
		List<ICommunity> roleCommunityList = null;
		if (communityList == null || communityList.isEmpty())
			return null;
		HashMap<Integer, List<ICommunity>> roleMap = new HashMap<Integer, List<ICommunity>>();

		for (int i = 0; i < communityList.size(); i++) {
			Map<String, Object> community = communityList.get(i);
			/* Step 1: Extract all the relevant information from the Node */
			String communityInstanceID = (String) community.get("instanceId");
			String communityInstanceName = (String) community
					.get("instanceName");
			String communityInstanceType = (String) community
					.get("instanceType");
			String roleId = ((Long) community.get("roleId")).toString();

			if (!roleId.equals(tempRoleId)) {
				roleCommunityList = new ArrayList<ICommunity>();
				roleMap.put(Integer.parseInt(roleId), roleCommunityList);
			}

			/* Step 2: Create an instance of the appropriate Bean */
			try {
				ICommunity aBeanInstance = (ICommunity) appContext
						.getBean(communityInstanceType);

				/* Step 3: Configure the Bean with Specifics */
				aBeanInstance.setInstanceName(communityInstanceName);
				aBeanInstance.setInstanceID(communityInstanceID);
				aBeanInstance.setInstanceType(communityInstanceType);
				roleCommunityList.add(aBeanInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}

			tempRoleId = roleId;

		}

		for (Iterator<Map.Entry<Integer, List<ICommunity>>> roleIter = roleMap
				.entrySet().iterator(); roleIter.hasNext();) {
			Map.Entry<Integer, List<ICommunity>> entry = roleIter.next();

			List<ICommunity> commList = entry.getValue();
			if (commList == null || commList.isEmpty()) {
				roleIter.remove();
				continue;
			}
		}

		return roleMap;
	}

}
