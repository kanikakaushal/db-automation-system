/**
 * 
 */
package org.satsang.sms.core.util.print;

import java.util.HashMap;

/**
 * @author Default
 *
 */
public class PrinterFactory {

	private HashMap<String, PrinterPool> managedPools;
	
	public PrinterPool getPrinterPool(String poolName){
		return managedPools.get(poolName);
	}

	public void setManagedPools(HashMap<String, PrinterPool> managedPools) {
		this.managedPools = managedPools;
	}
}
