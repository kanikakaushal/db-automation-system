/**
 * 
 */
package org.satsang.sms.core.util.print;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.util.JRSaver;

import org.apache.log4j.Logger;
import org.satsang.sms.aspects.statistics.managers.PilgrimAccomodationStatisticsManager;
import org.satsang.sms.core.activities.interfaces.ITvcDao;
import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.event.interfaces.IEvent;

/**
 * @author Default
 * 
 */
public class JasperPDFPrinter implements IPrinter {

	private String printerName;
	private String printDirectory;
	private String pathToJasperTemplate;
	private ITvcDao tvcDao;
	
	private static Logger log = Logger.getLogger(JasperPDFPrinter.class);
	
	
	// TODO - MAKE SHIFT ARRANGEMENT - NEEDS TO BE REMOVED
	private static List<String> branchesPermittedInA = Arrays.asList("1020403", "1020406");
	private static List<String> branchesPermittedInB = Arrays.asList("1100301", "1100302", "1100303", "1100304", "1100305", 
			"1100401", "1100402", "1100403", "1100404", "1100405", "1100406", "1100501", "1100502", "1100503", "1100504", "1100505",
			"1100601", "1100602", "1100603");
	
	@Override
	public void print(IEvent event, TvcMember tvc,
			List<TvcAssociateMember> tvcAssociations) throws Exception {

		// Prepare parameters to the tvc report
		String tvcId = tvc.getTvcId();
		log.info("Printing the tvc card for id "+ tvcId);
		
		// MFC
		Integer noOfMales = 0;
		Integer noOfFemales = 0;
		Integer noOfChildren = 0;
		if(tvc.isAssociate()){
			
		}else if (tvcAssociations != null) {
			for (TvcAssociateMember associate : tvcAssociations) {
				if (associate.getGender().equalsIgnoreCase("Male")) {
					noOfMales++;
				} else if (associate.getGender().equalsIgnoreCase("Female")) {
					noOfFemales++;
				}
			}
			if (tvc.getGender().equalsIgnoreCase("Male")) {
				noOfMales++;
			} else if (tvc.getGender().equalsIgnoreCase("Female")) {
				noOfFemales++;
			}			
		}else{
			if (tvc.getGender().equalsIgnoreCase("Male")) {
				noOfMales++;
			} else if (tvc.getGender().equalsIgnoreCase("Female")) {
				noOfFemales++;
			}
		}
		noOfChildren = tvc.getNoOfChildren();		
		// Title
		String title = "";
		if (tvc.getGender().equalsIgnoreCase("Male")
				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
			title = "PB.";
		} else if (tvc.getGender().equalsIgnoreCase("Female")
				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
			title = "PBn.";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//		SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("Event_Name", event.getEventName());
		parameters.put("Event_Description", event.getEventDescription());
		parameters.put("TVC_ID", tvcId);
		parameters.put("M", noOfMales.toString());
		parameters.put("F", noOfFemales.toString());
		String childText = noOfChildren.toString();
		parameters.put("C", childText);
		String name = "";
		name += title + " " + tvc.getFirstName() + " ";
		if (tvc.getMiddleName() != null) {
			name += tvc.getMiddleName() + " ";
		}
		name += tvc.getLastName();
		parameters.put("Name", name);
		parameters.put("Branch_Name", tvc.getBranchName());
		parameters.put("Region_Name", tvc.getRegionName());

		String dbAddr = "";
		dbAddr += tvc.getLocalAddress();
		if (tvc.getStayCategory().equals(PilgrimAccomodationStatisticsManager.DORMITORIES)) {
			dbAddr = tvc.getLocalSadan();
		}else {
			if(!tvc.getLocalColony().equals(PilgrimAccomodationStatisticsManager.OTHER)){
				dbAddr += ",  " + tvc.getLocalColony();
			}
		}
		
		if(tvc.getInitiationStatus().equals("Jigyasu") && tvc.getStayFromDate().compareTo(dateFormatter.parse("2010-12-23 13:30:00")) < 0){
			parameters.put("Applied_For_Initiation", "true");			
		}else{
			parameters.put("Applied_For_Initiation", "false");		
		}
		
		parameters.put("DB_Address", dbAddr);
		parameters.put("From_Date", sdf.format(tvc.getStayFromDate()));
		parameters.put("To_Date", sdf.format(tvc.getStayToDate()));
//		parameters.put("Time", timeFormatter.format(tvc.getStayFromDate()));
		if (tvc.isDuplicate()) {
			parameters.put("Is_Duplicate", "true");
		} else {
			parameters.put("Is_Duplicate", "false");
		}
		parameters.put("Init_Letter_Number", (tvc.getLetterNoIfJigyasu()== null)? "" : tvc.getLetterNoIfJigyasu());
		
		// TODO - MAKE SHIFT ARRANGEMENT - NEEDS TO BE REMOVED		
//		if(branchesPermittedInA.contains(tvc.getBranchId())){
//			parameters.put("Show_Bhandara_Number", "true");
//			parameters.put("Bhandara_Number", "A");
//		}else 
//		if(branchesPermittedInB.contains(tvc.getBranchId())){
//			parameters.put("Show_Bhandara_Number", "true");
//			parameters.put("Bhandara_Number", "B");
//		}else{
//			parameters.put("Show_Bhandara_Number", "false");
//			parameters.put("Bhandara_Number", "");
//		}
		
		parameters.put("Show_Bhandara_Number", "false");
		parameters.put("Bhandara_Number", "");
		
		// Generate report
		byte[] reportPdf = tvcDao.generateTvcCard(parameters,
				pathToJasperTemplate);
		// Write to file
		JRSaver.saveObject(reportPdf, printDirectory +"/"+ tvcId + ".pdf");
		log.info("Sent the tvc card for id " + tvcId + "to "
				+ printDirectory + tvcId + ".pdf");
	}

	public void setTvcDao(ITvcDao tvcDao) {
		this.tvcDao = tvcDao;
	}

	public void setPathToJasperTemplate(String pathToJasperTemplate) {
		this.pathToJasperTemplate = pathToJasperTemplate;
	}

	@Override
	public String getPrintDirectory() {
		return printDirectory;
	}

	@Override
	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public void setPrintDirectory(String printDirectory) {
		this.printDirectory = printDirectory;
	}

}
