package org.satsang.sms.poc;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.satsang.sms.core.activities.tvc.TvcAssociateMember;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRSaver;


public class TVCReportPOC {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sms_activities", "root", "admin");
            /*
            // MFC
    		Integer noOfMales = 0;
    		Integer noOfFemales = 0;
    		
    		if (tvcAssociations != null) {
    			for(TvcAssociateMember associate : tvcAssociations){
    				if(associate.getGender().equalsIgnoreCase("Male")){
    					noOfMales++;
    				}else if(associate.getGender().equalsIgnoreCase("Female")){
    					noOfFemales++;
    				}
    			}
    			
    		} 
    		
    		if (tvc.getGender().equalsIgnoreCase("Male")) {
    			noOfMales++;
    		} else if (tvc.getGender().equalsIgnoreCase("Female")) {
    			noOfFemales++;
    		}
    		Integer noOfChildren = tvc.getNoOfMaleDependents()
    				+ tvc.getNoOfFemaleDependents();
    		  */ 
//            String title = "";
//            if (tvc.getGender().equalsIgnoreCase("Male")
//    				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
//            	title ="Pb.";
//    		} else if (tvc.getGender().equalsIgnoreCase("Female")
//    				&& tvc.getInitiationStatus().equalsIgnoreCase("Initiated")) {
//    			title ="Pbn.";
//    		}
            
            long startTime = System.currentTimeMillis();
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("Event_Name", "Bhandara of Param Guru Huzur Maharaj");
            parameters.put("Event_Date", "2010-04-29");
            parameters.put("TVC_ID", "2010071");
            parameters.put("Count_M", "4");
            parameters.put("Count_F", "3");
//            parameters.put("C", noOfChildren.toString() + "("+tvc.getFemaleDependentsAge()+ "; "+ tvc.getMaleDependentsAge()+")");
            parameters.put("Count_C", "2");
           parameters.put("Title", "Pbn");
           parameters.put("Name", "Kanika Kaushal");
   		parameters.put("Home_Address", "Noida, UP"); 
   		parameters.put("DB_Address", "Kaveri Lodge");
		parameters.put("Stay_From_Date", "2010-04-19");
		parameters.put("Stay_To_Date", "2010-04-30");
		parameters.put("Is_Duplicate", "true");		
		for(int i =0; i<20; i++){
			 JasperPrint print = JasperFillManager.fillReport("c:/tvc-card.jasper",
						parameters, jdbcConnection);

				byte[] reportPdf = JasperExportManager.exportReportToPdf(print);
				JRSaver.saveObject(reportPdf, "c:/tvc-cards/201007"+i+".pdf");
				
		}
		  
//			FileOutputStream out = new FileOutputStream("c:/tvc-cards/2010071.pdf");
//			out.write(reportPdf);
//			out.flush();
//			out.close();
			long endTime = System.currentTimeMillis();
            System.out.println("Time taken -- "+ (endTime - startTime));
  }
  catch( Exception e ) {
    e.printStackTrace();        
  }
	}

}
