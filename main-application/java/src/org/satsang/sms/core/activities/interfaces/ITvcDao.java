/**
 * 
 */
package org.satsang.sms.core.activities.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.activities.tvc.TvcSearchMember;

/**
 * @author kanikkau
 * 
 */
public interface ITvcDao {

	public List<TvcMember> getTVCMemberList(String eventId,
			TvcSearchMember searchTVCRecord);

	public String createTVCRecord(String eventId, String accountId, TvcMember tvcMember, String tvcIdMask);

	public void createTVCRecordForMember(String eventId, String accountId, TvcMember tvcMember);
	
	public void saveTVCRecord(String eventId, TvcMember tvcMember);
	
	public void setDuplicate(String eventId, String tvcId);
	
	public List<TvcAssociateMember> getMemberAssociations(String eventId,
			String tvcId);
	
	public void addMemberAssociations(String eventId, String tvcId,
			List<TvcAssociateMember> associations);
	
	public Map<String, Object> getBhandaraRegion(String branchId);
	
	public byte[] generateTvcCard(Map<String, Object> parameters,
			String tvcTemplate);
	
	HashMap<String, String> getLicenseeDetails(String quarterNo, String colonyName);
	
	String getPrimaryDetails(String eventId, String associateTvcId);
	
	String validateAssociate(String eventId, TvcAssociateMember associate);
	
	List<HashMap<String, String>> getPQList(String associatedColonyName);
}
