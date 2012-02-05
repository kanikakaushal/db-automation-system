/**
 * 
 */
package org.satsang.sms.core.activities.interfaces;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author gcapriha
 * 
 */
public interface IActivity {

	public String getActivityName();

	public void setActivityName(String aName);

	public int getActivityId();

	public void setActivityId(int activityId);

	public boolean rollbackActivity();

	public void setActionHandler(IActionHandler actionHandler);

	public Boolean configureActivity(ArrayList<Object> anArrayListOfObjects);

	public ArrayList<Object> doActivity(String accountId, String eventId,
			String actionName, ArrayList<Object> anArrayListOfObjects);

	public Map<String, String> getListOfActions();
	
	public void setListOfActions(Map<String, String> actionList);
}
