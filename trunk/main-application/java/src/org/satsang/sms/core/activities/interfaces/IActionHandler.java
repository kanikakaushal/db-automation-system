/**
 * 
 */
package org.satsang.sms.core.activities.interfaces;

import java.util.HashMap;

/**
 * @author Default
 *
 */
public interface IActionHandler {
	// actions common across all activities
	public void setConfigParams(HashMap<String, Object> configParams);
	
	public HashMap<String, Object> getConfigParams();
	
}
