/**
 * 
 */
package org.satsang.sms.core.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.satsang.sms.aspects.statistics.SadanStats;
import org.satsang.sms.core.activities.interfaces.ITvcActionHandler;
import org.satsang.sms.core.activities.interfaces.ITvcDao;
import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.activities.tvc.TvcSearchMember;

import com.sun.xml.internal.bind.v2.runtime.AssociationMap;

/**
 * @author kanikkau
 * 
 */
public class TvcActionHandler implements ITvcActionHandler {

	private ITvcDao tvcDao;
	private HashMap<String, Object> configParams;
	private static Logger log = Logger.getLogger(TvcActionHandler.class);

	@Override
	public HashMap<String, Object> getConfigParams() {
		return this.configParams;
	}

	public void setTvcDao(ITvcDao tvcDao) {
		this.tvcDao = tvcDao;
	}

	@Override
	public void setConfigParams(HashMap<String, Object> configParams) {
		this.configParams = configParams;
	}

	public String register(TvcMember tvcMember, String bhandaraId) {
		String tvcId = tvcMember.getTvcId();
		String eventId = (String) configParams.get("eventId");
		if(tvcId == null){
//			tvcId = tvcDao.createTVCRecord(eventId, (String) configParams.get("accountId"),
//					tvcMember, (String) configParams.get("tvcIdMask"));
//			tvcMember.setTvcId(tvcId);			
		}else{
			setState(tvcMember, bhandaraId);
			TvcSearchMember searchCriteria = new TvcSearchMember();
			searchCriteria.setTvcId(tvcId);
			List<TvcMember> searchResult = tvcDao.getTVCMemberList(eventId, searchCriteria);
			if(searchResult != null && searchResult.size() > 0){
				return "A TVC Card has already been printed for this TVC ID";	
			}else{
				tvcDao.createTVCRecordForMember(eventId, (String) configParams.get("accountId"), tvcMember);
			}
		}
		log.info("TVC card generated with id " + tvcId);
		return tvcId;
	}

	public List<TvcMember> search(TvcSearchMember searchTvcMember) {
		return tvcDao.getTVCMemberList((String) configParams.get("eventId"),
				searchTvcMember);
	}

	public void edit(TvcMember tvcMember, String bhandaraId, List<TvcAssociateMember> associations) {
		setState(tvcMember, bhandaraId);
		tvcDao.saveTVCRecord((String) configParams.get("eventId"), tvcMember);
		addAssociations(tvcMember.getTvcId(), associations);
		log.info("TVC card updated for " + tvcMember.getTvcId());
	}

	public void duplicate(String tvcId) {
		tvcDao.setDuplicate((String) configParams.get("eventId"), tvcId);
		log.info("Duplicate TVC card generated for " + tvcId);
	}

	public List<TvcAssociateMember> viewAssociations(String tvcId) {
		return tvcDao.getMemberAssociations((String) configParams
				.get("eventId"), tvcId);
	}

	public void addAssociations(String tvcId,
			List<TvcAssociateMember> associations) {
		tvcDao.addMemberAssociations((String) configParams.get("eventId"),
				tvcId, associations);
	}

	public HashMap<String, SadanStats> sadanStats() {
		return null;
	}

	public HashMap<Integer, List<String>> sabhaQuarterStats(String colonyName,
			int noOfPeople) {
		return null;
	}

	public HashMap<String, String> licenseeDetails(String quarterNo, String colonyName){
		return tvcDao.getLicenseeDetails(quarterNo, colonyName);
	}
	
	public List<HashMap<String, String>> getPQList(String associatedColonyName){
		return tvcDao.getPQList(associatedColonyName);
	}
	
	public String primaryDetails(String associateTvcId){
		return tvcDao.getPrimaryDetails((String) configParams.get("eventId"), associateTvcId);
	}
	
	public String validateAssociate(TvcAssociateMember associate){
		return tvcDao.validateAssociate((String) configParams.get("eventId"), associate);
	}
	
	private void setState(TvcMember tvcMember, String bhandaraNo) {
				
		if (tvcMember.getAddressCountry()!= null && !tvcMember.getAddressCountry().equals("") &&
				tvcMember.getAddressCountry().equals("Singapore")) {
			tvcMember.setHomeStateType("SNG");
		} else if(tvcMember.getAddressCountry()!= null && !tvcMember.getAddressCountry().equals("") &&
				tvcMember.getAddressCountry().equals("United Arab Emirates")) {
			tvcMember.setHomeStateType("DUB");
		} else if (tvcMember.getAddressCountry()!= null && !tvcMember.getAddressCountry().equals("") &&
				!tvcMember.getAddressCountry().equals("India")) {
			tvcMember.setHomeStateType("FOR");
		} else if (tvcMember.getBranchId()!= null && 
				(bhandaraNo == null || bhandaraNo.equals("0"))) {
			// get the abbreviation as such
			Map<String, Object> bhandaraRegion = tvcDao
					.getBhandaraRegion(tvcMember.getBranchId());
			if(bhandaraRegion!= null){
				tvcMember.setHomeStateType((String) bhandaraRegion.get("stateAbbrv"));
			}else{
				// ****** This should never be executed in real time *****
				tvcMember.setHomeStateType("ROI");
			}
		} else if(tvcMember.getBranchId()!=null){
			// get the state abbrv and bhandara id for the state
			// if the bhandara id equals then set the state abbrv
			// else set ROI
			Map<String, Object> bhandaraRegion = tvcDao
					.getBhandaraRegion(tvcMember.getBranchId());
			if (bhandaraRegion != null && bhandaraNo.equals((String) bhandaraRegion.get("bhandaraId"))) {
				tvcMember.setHomeStateType((String) bhandaraRegion
						.get("stateAbbrv"));
			} else {
				tvcMember.setHomeStateType("ROI");
			}
		}else{
			// ****** This should ideally not be executed in real time *****
			tvcMember.setHomeStateType("ROI");
		}

	}
}
