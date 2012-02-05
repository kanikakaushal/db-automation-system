/**
 * 
 */
package org.satsang.sms.core.util.print;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.satsang.sms.core.activities.tvc.TvcAssociateMember;
import org.satsang.sms.core.activities.tvc.TvcMember;
import org.satsang.sms.core.event.interfaces.IEvent;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author Default
 * 
 */
public class CardCSVPrinter implements IPrinter{

	private String printerName;
	private String printDirectory;
	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public void print(IEvent event, TvcMember tvc,
			List<TvcAssociateMember> tvcAssociations) throws Exception {

		String tvcId = tvc.getTvcId();
		CSVWriter writer = new CSVWriter(new FileWriter(printDirectory +"/"+ tvcId + ".csv"));

		// feed in your array (or convert your data to an array)
		ArrayList<String> csvData = new ArrayList<String>();
		csvData.add(event.getEventName());
		csvData.add(event.getEventDescription());  // PART 1 - PART 2 BHANDARA 
		
		//DB ADDR
		String dbAddr = "";
		dbAddr += tvc.getLocalAddress();
		if (tvc.getLocalSadan() != null && !tvc.getLocalSadan().equals("")) {
			dbAddr += ",  " + tvc.getLocalSadan();
		} else if (tvc.getLocalColony() != null
				&& !tvc.getLocalColony().equals("")) {
			dbAddr += ",  " + tvc.getLocalColony();
		}
		
		csvData.add(dbAddr);
		
		//MFC
		Integer noOfMales = 0;
		Integer noOfFemales = 0;
		if (tvcAssociations != null) {
			for (TvcAssociateMember associate : tvcAssociations) {
				if (associate.getGender().equalsIgnoreCase("Male")) {
					noOfMales++;
				} else if (associate.getGender().equalsIgnoreCase("Female")) {
					noOfFemales++;
				}
			}

		}
		if (tvc.getGender().equalsIgnoreCase("Male")) {
			noOfMales++;
		} else if (tvc.getGender().equalsIgnoreCase("Female")) {
			noOfFemales++;
		}
		Integer noOfChildren = tvc.getNoOfChildren();

		csvData.add(noOfMales.toString());
		csvData.add(noOfFemales.toString());
		csvData.add(noOfChildren.toString());
		
		csvData.add(formatter.format(tvc.getStayFromDate()));
		csvData.add(formatter.format(tvc.getStayToDate()));
		csvData.add(tvcId);
		
		//NAME
		String title = "";
		if (tvc.getGender().equalsIgnoreCase("Male")
				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
			title = "PB.";
		} else if (tvc.getGender().equalsIgnoreCase("Female")
				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
			title = "PBn.";
		}
		String name = "";
		name += title + " " + tvc.getFirstName() + " ";
		if (tvc.getMiddleName() != null) {
			name += tvc.getMiddleName() + " ";
		}
		name += tvc.getLastName();
				
		csvData.add(name);
		csvData.add(tvc.getBranchName());
		csvData.add(tvc.getRegionName());
		
		if(tvc.isDuplicate()){
			csvData.add("DUPLICATE");
		}else{
			csvData.add("");
		}
		
		String str[] = (String[]) csvData.toArray(new String[csvData.size()]);
		writer.writeNext(str);
		writer.close();
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
