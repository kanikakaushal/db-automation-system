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
public interface ICommunityHandler {

	public List<Map> regions(String searchString);
	
	public List<Map> districts(String searchString, String regionId);
	
	public List<Map> branches(String searchString, String districtId);
	
	public List<Map> allBranches();

	public List<Map> branches(String searchString);
	
	public void setCommunityDao(ICommunityDao communityDao);
	
	HashMap<String, String> branch(String branchId);
}
