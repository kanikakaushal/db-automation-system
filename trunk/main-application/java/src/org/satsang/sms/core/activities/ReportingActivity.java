/**
 * 
 */
package org.satsang.sms.core.activities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.satsang.sms.core.activities.interfaces.IActionHandler;
import org.satsang.sms.core.activities.interfaces.IActivity;
import org.springframework.util.MethodInvoker;

/**
 * @author Default
 *
 */
public class ReportingActivity implements IActivity {

	private IActionHandler actionHandler;	
	private String activityName;
	private int activityId;
	private Map<String, String> listOfActions;
	
	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#configureActivity(java.util.ArrayList)
	 */
	@Override
	public Boolean configureActivity(ArrayList<Object> anArrayListOfObjects) {
		HashMap<String, Object> configParams = actionHandler.getConfigParams();
		if(configParams == null){
			configParams = new HashMap<String, Object>();
			actionHandler.setConfigParams(configParams);
		}		
		return Boolean.TRUE;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#doActivity(java.lang.String, java.lang.String, java.lang.String, java.util.ArrayList)
	 */
	@Override
	public ArrayList<Object> doActivity(String accountId, String eventId,
			String actionName, ArrayList<Object> anArrayListOfObjects) {
		HashMap<String, Object> configParams = actionHandler.getConfigParams();
		if(configParams == null){
			configParams = new HashMap<String, Object>();
			actionHandler.setConfigParams(configParams);
		}
		configParams.put("eventId", eventId);
		configParams.put("accountId", accountId);
		
		// PARSE THE ARRAY LIST TO CREATE PARAMS OR DIRECTLY ASSIGN
		Object[] params = anArrayListOfObjects.toArray();
		
		//INVOKE THE ACTION METHOD OF ACTION HANDLER
		MethodInvoker methodInvoker = new MethodInvoker();
		methodInvoker.setTargetObject(this.actionHandler);
		methodInvoker.setTargetMethod(actionName);
		methodInvoker.setArguments(params);
		
		ArrayList<Object> aTempArrayList = new ArrayList<Object>();
		Object result = null;
		try{
			methodInvoker.prepare();
			result = methodInvoker.invoke(); 
		}catch(IllegalAccessException iae){
			iae.printStackTrace();
		}catch(InvocationTargetException ite){
			ite.printStackTrace();
		}catch(NoSuchMethodException nsme){
			nsme.printStackTrace();
		}catch(ClassNotFoundException cnfe){
			cnfe.printStackTrace();
		}
		
		Boolean success = aTempArrayList.add(result);
		if(success) return aTempArrayList;
		else return null;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#getActivityId()
	 */
	@Override
	public int getActivityId() {
		return this.activityId;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#getActivityName()
	 */
	@Override
	public String getActivityName() {
		return this.activityName;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#getListOfActions()
	 */
	@Override
	public Map<String, String> getListOfActions() {
		return this.listOfActions;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#rollbackActivity()
	 */
	@Override
	public boolean rollbackActivity() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#setActionHandler(org.satsang.sms.core.activities.interfaces.IActionHandler)
	 */
	@Override
	public void setActionHandler(IActionHandler actionHandler) {
		this.actionHandler = actionHandler;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#setActivityId(int)
	 */
	@Override
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#setActivityName(java.lang.String)
	 */
	@Override
	public void setActivityName(String name) {
		this.activityName = name;
	}

	/* (non-Javadoc)
	 * @see org.satsang.sms.core.activities.interfaces.IActivity#setListOfActions(java.util.List)
	 */
	@Override
	public void setListOfActions(Map<String, String> actionList) {
		this.listOfActions = actionList;
	}

}
