/**
 * 
 */
package org.satsang.sms.core.communities.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Default
 * 
 */
public interface ICommunityDao {

	public List<Map> getRegions(String searchString);

	public List<Map> getDistricts(String searchString, String regionId);

	public List<Map> getBranchesInDistrict(String searchString, String districtId);

	public List<Map> getAllBranches();

	public List<Map> getBranches(String searchString);

	HashMap<String, String> getBranchById(String branchId);
}
