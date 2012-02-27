/**
 * 
 */
package org.satsang.sms.core.activities.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.satsang.sms.core.activities.interfaces.ITvcDao;
import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.activities.tvc.TvcSearchMember;
import org.springframework.orm.ibatis.SqlMapClientTemplate;

/**
 * @author kanikkau
 * 
 */
public class TvcIbatisDao implements ITvcDao {

	private SqlMapClientTemplate sqlMapClientTemplate;

	public void setSqlMapClientTemplate(
			SqlMapClientTemplate sqlMapClientTemplate) {
		this.sqlMapClientTemplate = sqlMapClientTemplate;
	}

	@SuppressWarnings("unchecked")
	public List<TvcMember> getTVCMemberList(String eventId,
			TvcSearchMember searchTVCRecord) {

		String whereClause = "EVENT_ID = '" + eventId + "'";

		if (searchTVCRecord.getTvcId() != null
				&& searchTVCRecord.getTvcId() != "") {
			whereClause += " AND TVC_ID = '" + searchTVCRecord.getTvcId() + "'";
		}

		if (searchTVCRecord.getFirstName() != null
				&& searchTVCRecord.getFirstName() != "") {
			whereClause += " AND MEMBER_FIRST_NAME LIKE '"
					+ searchTVCRecord.getFirstName() + "%'";
		}
		if (searchTVCRecord.getLastName() != null
				&& searchTVCRecord.getLastName() != "") {
			whereClause += " AND MEMBER_LAST_NAME LIKE '"
					+ searchTVCRecord.getLastName() + "%'";
		}
		if (searchTVCRecord.getBranchId() != null
				&& searchTVCRecord.getBranchId() != "") {
			whereClause += " AND MEMBER_BRANCH_ID = '"
					+ searchTVCRecord.getBranchId() + "'";
		}
		List<TvcMember> memberList = sqlMapClientTemplate.queryForList(
				"TVC.searchTVCRecords", whereClause);
		return memberList;
	}

	public String createTVCRecord(String eventId, String accountId, TvcMember tvcMember) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("instanceId", tvcMember.getBranchId());
				
		Integer seqNo = (Integer) this.sqlMapClientTemplate.queryForObject("TVC.getNextEventId", params);
		if (seqNo == null){
			seqNo = 1;
		}

		String tvcId = tvcMember.getBranchId() + "7" + padZeros(seqNo.toString()) + "M";
		tvcMember.setTvcId(tvcId);
		params.put("tvc", tvcMember);
		params.put("eventId", eventId);
		params.put("accountId", accountId);

		sqlMapClientTemplate.insert("TVC.createTVCRecord", params);
		params.remove("tvc");
		params.remove("eventId");
		params.remove("accountId");

		params.put("nextId", seqNo + 1);
		if (seqNo == 1) {
			this.sqlMapClientTemplate.update("TVC.insertNextEventId", params);
		} else {
			this.sqlMapClientTemplate.update("TVC.updateNextEventId", params);
		}
		return tvcId;
	}

	public void createTVCRecordForMember(String eventId, String accountId, TvcMember tvcMember) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("accountId", accountId);
		params.put("tvc", tvcMember);

		sqlMapClientTemplate.insert("TVC.createTVCRecord", params);
	}

	public void saveTVCRecord(String eventId, TvcMember tvcMember) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("tvc", tvcMember);
		sqlMapClientTemplate.update("TVC.editTVCRecord", params, 1);
	}

	@Override
	public void setDuplicate(String eventId, String tvcId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("tvcId", tvcId);
		sqlMapClientTemplate.update("TVC.setDuplicate", params, 1);
	}

	@SuppressWarnings("unchecked")
	public List<TvcAssociateMember> getMemberAssociations(String eventId,
			String tvcId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("tvcId", tvcId);
		List<TvcAssociateMember> memberList = sqlMapClientTemplate.queryForList("TVC.getMemberAssociations", params);
		return memberList;
	}

	public void addMemberAssociations(String eventId, String tvcId,
			List<TvcAssociateMember> newAssociations) {

		List<TvcAssociateMember> oldAssociates = getMemberAssociations(eventId, tvcId);
		if(oldAssociates!= null && oldAssociates.size() > 0){
			String tvcIdList = "(";
			for (int index = 0; index < oldAssociates.size(); index ++) {
				TvcAssociateMember member = oldAssociates.get(index);
				tvcIdList += "'"+member.getAssociateTvcId()+"'";
				if(index != oldAssociates.size() - 1){
					tvcIdList += ",";
				}
			}
			tvcIdList += ")";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("eventId", eventId);
			params.put("tvcIdList", tvcIdList);
			sqlMapClientTemplate.update("TVC.resetAssociates", params);		
			
			params.put("tvcId", tvcId);
			sqlMapClientTemplate.delete("TVC.deleteAssociations", params);			
		}
				
		if(newAssociations!=null){
			String insertClause = "";
			String tvcIdList = "(";
			for (TvcAssociateMember member : newAssociations) {
				tvcIdList += "'"+member.getAssociateTvcId()+"'";
				if (newAssociations.indexOf(member) != newAssociations.size() - 1){
					insertClause += "('" + eventId + "', '" + tvcId + "', '"
							+ member.getAssociateTvcId() + "', '"
							+ member.getAssociateRelationship() + "'),";
					tvcIdList += ",";
				}
				else {
					insertClause += "('" + eventId + "', '" + tvcId + "', '"
							+ member.getAssociateTvcId() + "', '"
							+ member.getAssociateRelationship() + "')";
				}
			}

			tvcIdList += ")";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("eventId", eventId);
			params.put("tvcIdList", tvcIdList);
			sqlMapClientTemplate.update("TVC.setAssociates", params);		
			sqlMapClientTemplate.insert("TVC.insertAssociations", insertClause);
		}
				
	}

	public HashMap<String, String> getLicenseeDetails(String localAddr, String associatedColonyName){
		Map<String, Object> params = new HashMap<String, Object>();
		
		String quarterNo = localAddr;
		String colonyName = associatedColonyName;
		if(localAddr.contains(",")){
			quarterNo = localAddr.substring(0, localAddr.indexOf(","));
			colonyName = localAddr.substring(localAddr.indexOf(",")+1);
		}
			
		params.put("quarterNo", quarterNo);
		params.put("colonyName", colonyName);
		params.put("associatedColonyName", associatedColonyName);
		return (HashMap<String, String>)sqlMapClientTemplate.queryForObject("TVC.getLicenseeDetails", params);
	}
	
	public List<HashMap<String, String>> getPQList(String associatedColonyName){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("associatedColonyName", associatedColonyName);
		return (List<HashMap<String, String>>)sqlMapClientTemplate.queryForList("TVC.getPQList", params);
	}
	
	public Map<String, Object> getBhandaraRegion(String branchId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("branchId", branchId);
		HashMap<String, Object> bhandaraRegion = (HashMap<String, Object>)sqlMapClientTemplate
				.queryForObject("TVC.getBhandaraRegion", params);
		return bhandaraRegion;
	}
	
	public byte[] generateTvcCard(Map<String, Object> parameters,
			String tvcTemplate) {
		byte[] reportPdf = null;
		Connection jdbcConnection = null;
		try {
			jdbcConnection = sqlMapClientTemplate.getDataSource()
					.getConnection();
			JasperPrint print = JasperFillManager.fillReport(tvcTemplate,
					parameters, jdbcConnection);
			reportPdf = JasperExportManager.exportReportToPdf(print);
			
		} catch (JRException e) {
			e.printStackTrace();
			reportPdf = null;
		} catch (Exception e) {
			e.printStackTrace();
			reportPdf = null;
		}finally{
			if (jdbcConnection != null) {
				try {
					// Close to prevent database connection exhaustion
					jdbcConnection.close();
				} catch (SQLException ex) {
					System.out.println("Exception while closing connection...");
				}
			}
		}
		return reportPdf;
	}
	
	public String getPrimaryDetails(String eventId, String associateTvcId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("associateTvcId", associateTvcId);
		
		return (String)sqlMapClientTemplate.queryForObject("TVC.getPrimaryTvcId", params);
	}
	
	public String validateAssociate(String eventId, TvcAssociateMember associate){
		
		if(associate.getTvcId().equals(associate.getAssociateTvcId()))
			return "Cannot associate two identical ids " + associate.getAssociateTvcId();
		
		TvcSearchMember searchOn = new TvcSearchMember();
		searchOn.setTvcId(associate.getAssociateTvcId());
		
		List<TvcMember> memberList = getTVCMemberList(eventId, searchOn);
		if(memberList == null || memberList.isEmpty())		
			return "TVC Id does not exist for " + associate.getAssociateTvcId();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("eventId", eventId);
		params.put("tvcId", associate.getAssociateTvcId());
		
		boolean status = (Boolean)sqlMapClientTemplate.queryForObject("TVC.isAssociate", params);
		if(status)
			return "TVC Id " + associate.getAssociateTvcId() + " is already an associate.";
		
		String primaryTvcId = (String)sqlMapClientTemplate.queryForObject("TVC.isPrimary", params);
		if(primaryTvcId != null)
			return "TVC Id " + associate.getAssociateTvcId() + " is a primary card holder and cannot be associated to another person.";
		
		associate.setBranchName(memberList.get(0).getBranchName());
		associate.setFirstName(memberList.get(0).getFirstName());
		associate.setGender(memberList.get(0).getGender());
		associate.setLastName(memberList.get(0).getLastName());	
		
		return "Success";
	}
	
	private String padZeros(String number){
		String paddedNumber = number;
		while(paddedNumber.length() < 5){
			paddedNumber = "0" + paddedNumber; 
		}
		return paddedNumber;
	}
	
	private String removePadding(String paddedNumber){
		String number = paddedNumber;
		int index = 0;
		while(number.charAt(index) == '0'){
			number = number.substring(++index); 
		}
		return number;
	}
}
