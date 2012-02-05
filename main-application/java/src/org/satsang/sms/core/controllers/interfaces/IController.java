/**
 * 
 */
package org.satsang.sms.core.controllers.interfaces;

import java.util.List;
import java.util.Map;

/**
 * @author gcapriha
 * 
 */
public interface IController {

	public List<Object> processRequest(List<Object> payload);
	public Map<String, String> defaultReturnMap();
}
