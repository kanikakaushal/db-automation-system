/**
 * 
 */
package org.satsang.sms.core.util.print;

import java.util.HashMap;
import java.util.List;

/**
 * @author Default
 *
 */
public class PrinterPool {

	private List<IPrinter> pool;
	private int lastResourceIndex;
	private HashMap<String, IPrinter> terminalMap;
	
	public IPrinter getPrinter(){
		return pool.get(++lastResourceIndex % pool.size());
	}

	public void setPool(List<IPrinter> pool) {
		this.pool = pool;
	}	
	
	public IPrinter getPrinter(String terminalId){
		return terminalMap.get(terminalId);
	}
	
	public void setTerminalMap(HashMap<String, IPrinter> terminalMap) {
		this.terminalMap = terminalMap;
	}
}
