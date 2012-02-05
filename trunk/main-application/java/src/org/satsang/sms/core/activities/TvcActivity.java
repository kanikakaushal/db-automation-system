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
 * @author kanikkau
 *
 */
public class TvcActivity implements IActivity {

	private IActionHandler actionHandler;	
	private String activityName;
	private int activityId;
	private Map<String, String> listOfActions;
	
	@Override
	public Boolean configureActivity(ArrayList<Object> anArrayListOfObjects) {
		HashMap<String, Object> configParams = actionHandler.getConfigParams();
		if(configParams == null){
			configParams = new HashMap<String, Object>();
			actionHandler.setConfigParams(configParams);
		}		
		return Boolean.TRUE;
	}

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

	@Override
	public int getActivityId() {
		return this.activityId;
	}

	@Override
	public String getActivityName() {
		return this.activityName;
	}

	@Override
	public Map<String, String> getListOfActions() {
		return this.listOfActions;
	}

	@Override
	public boolean rollbackActivity() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActionHandler(IActionHandler actionHandler) {
		this.actionHandler = actionHandler;
	}

	@Override
	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	@Override
	public void setActivityName(String name) {
		this.activityName = name;
	}

	@Override
	public void setListOfActions(Map<String, String> actionList) {
		this.listOfActions = actionList;
	}

}
