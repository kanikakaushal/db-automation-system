package org.satsang.sms.core.activities.interfaces;

import java.util.HashMap;
import java.util.List;

import org.satsang.sms.aspects.statistics.SadanStats;
import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.activities.tvc.TvcSearchMember;

public interface ITvcActionHandler extends IActionHandler {

	public String register(TvcMember tvcMember, String bhandaraId);
	
	public HashMap<String, SadanStats> sadanStats();
	
	public HashMap<Integer, List<String>> sabhaQuarterStats(String colonyName, int noOfPeople);
	
	public List<TvcMember> search(TvcSearchMember searchTvcMember);
	
	public void addAssociations(String tvcId, List<TvcAssociateMember> associations);

	public List<TvcAssociateMember> viewAssociations(String tvcId);
	
	public void duplicate(String tvcId);
	
	public void edit(TvcMember tvcMember, String bhandaraId, List<TvcAssociateMember> associations);	
	
	HashMap<String, String> licenseeDetails(String quarterNo, String colonyName);
	
	String primaryDetails(String associateTvcId);
	
	String validateAssociate(TvcAssociateMember associate);
	
	List<HashMap<String, String>> getPQList(String associatedColonyName);
}
