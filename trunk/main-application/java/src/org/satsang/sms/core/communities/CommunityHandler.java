/**
 * 
 */
package org.satsang.sms.core.communities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.communities.interfaces.ICommunityDao;
import org.satsang.sms.core.communities.interfaces.ICommunityHandler;

/**
 * @author Default
 * 
 */
public class CommunityHandler implements ICommunityHandler {

	private ICommunityDao communityDao;

	/**
	 * @param communityDao
	 *            the communityDao to set
	 */
	public void setCommunityDao(ICommunityDao communityDao) {
		this.communityDao = communityDao;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> branches(String searchString, String districtId) {
		List<Map> matchingBranches = communityDao.getBranchesInDistrict(searchString, districtId);
		return matchingBranches;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> districts(String searchString, String regionId) {
		List<Map> matchingDistricts = communityDao.getDistricts(searchString, regionId);
		return matchingDistricts;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> regions(String searchString) {
		List<Map> matchingRegions = communityDao.getRegions(searchString);
		return matchingRegions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> allBranches() {
		return communityDao.getAllBranches();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> branches(String searchString) {
		return communityDao.getBranches(searchString);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> branch(String branchId) {
		return communityDao.getBranchById(branchId);
	}

}
