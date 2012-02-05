/**
 * 
 */
package org.satsang.sms.core.communities.interfaces;

import java.util.HashMap;
import java.util.Set;

import org.satsang.sms.core.context.interfaces.IContext;

/**
 * @author gcapriha
 * 
 */
public interface ICommunity {

	public HashMap<String, IContext> getContextMap();

	public IContext getHandleToContext(String aContextName);

	public void setInstanceName(String aName);

	public String getInstanceName();

	public void setInstanceID(String anID);

	public String getInstanceID();

	public String getInstanceType();

	public void setInstanceType(String instanceType);

	/* Spring Setters */
	public void setContextMap(HashMap<String, IContext> aMap);

	public Set<String> getContextList();
}
