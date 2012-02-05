/**
 * 
 */
package org.satsang.sms.core.communities;

import java.util.HashMap;
import java.util.Set;

import org.satsang.sms.core.communities.interfaces.ICommunity;
import org.satsang.sms.core.context.interfaces.IContext;

/**
 * @author gcapriha
 * 
 */
public class District implements ICommunity {


	private HashMap<String, IContext> contextMap;
	private String anInstanceName;
	private String anInstanceID;
	private String instanceType;
	
	/*
	 * This is a setter for the listOfRegisteredContext HashMap Each element of
	 * the Map contains a set containing an Context <entry> and <value>. We will
	 * now extract each of these sets corresponding to all the context that have
	 * been defined and add them to the listOfRegisteredContext HashMap
	 * associated with the BranchSatsang
	 */
	@Override
	public void setContextMap(HashMap<String, IContext> map) {
		this.contextMap = map;
	}

	/*
	 * Setter for the name of the community.
	 */
	public void setInstanceName(String aName) {
		this.anInstanceName = aName;
	}

	/* This is the getter for the Community Name */
	public String getInstanceName() {
		return this.anInstanceName;
	}

	public void setInstanceID(String anID) {
		this.anInstanceID = anID;
	}

	public String getInstanceID() {
		return this.anInstanceID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.satsang.sms.core.interfaces.ICommunity#getContextList() This
	 * method is called by the CommunityController to get the list of context
	 * associated with any given community name.
	 */
	public Set<String> getContextList() {
		return this.contextMap.keySet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.satsang.sms.core.interfaces.ICommunity#getHandleToContext(java.lang
	 * .String) This method returns a handle to a particular context instance
	 * within a community
	 */
	public IContext getHandleToContext(String aContextName) {
		return this.contextMap.get(aContextName);
	}
	
	@Override
	public HashMap<String, IContext> getContextMap() {
		return this.contextMap;
	}

	@Override
	public String getInstanceType() {
		return this.instanceType;
	}

	@Override
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
}
