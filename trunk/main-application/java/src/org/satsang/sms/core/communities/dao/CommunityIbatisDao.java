/**
 * 
 */
package org.satsang.sms.core.communities.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.communities.interfaces.ICommunityDao;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author Kanikkau
 *
 */
public class CommunityIbatisDao implements ICommunityDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getBranchesInDistrict(String searchString, String districtId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("searchString", searchString+"%");
		parameters.put("districtId", districtId);

		return this.sqlMapClientTemplate.queryForList("Community.getBranchesInDistrict", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getDistricts(String searchString, String regionId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("searchString", searchString+"%");
		parameters.put("regionId", regionId);

		return this.sqlMapClientTemplate.queryForList("Community.getDistricts", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getRegions(String searchString) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("searchString", searchString+"%");

		return this.sqlMapClientTemplate.queryForList("Community.getRegions", parameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getAllBranches() {		
		return this.sqlMapClientTemplate.queryForList("Community.getAllBranches");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map> getBranches(String searchString) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("searchString", searchString+"%");
		List<Map> resultSet = this.sqlMapClientTemplate.queryForList("Community.getBranches", parameters);
		return resultSet;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<String, String> getBranchById(String branchId) {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("branchId", branchId);

		return (HashMap<String, String>)sqlMapClientTemplate.queryForObject("Community.getBranchById", parameters);
	}

}
