// ActionScript file

import mx.managers.PopUpManager;
import mx.core.IFlexDisplayObject;
import component.home.activityviews.SmartAssist;

private function createSmartAssistPopupPanel():void {
    // Create a non-modal TitleWindow container.
    var helpWindow:IFlexDisplayObject =
        PopUpManager.createPopUp(this, SmartAssist, true);
        PopUpManager.centerPopUp(this);

}

private function processLogin():void {
    // Check credentials (not shown) then remove pop up.
    PopUpManager.removePopUp(this);
} 
