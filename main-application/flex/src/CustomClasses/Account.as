package CustomClasses
{
	import component.DataSources.AllDataSources;
	
	public class Account
	{
		[Bindable]
		public var accountName:String;
		[Bindable]
		public var accountID:String;
		[Bindable]
		public var terminalID:String;
		[Bindable]
		public var currEventID:String;
		[Bindable]
		public var currEventName:String;
		[Bindable]
		public var currCommunity:String;
		[Bindable]
		public var currContext:String;
		[Bindable]
		public var currentApplicationActivityState:String;
		[Bindable]
		public var currentApplicationSubActivityState:String;
		
		public function Account()
		{
			this.accountID = new String();
			this.accountID = "";
			this.accountName = new String();
			this.accountName = "";
			this.terminalID = "";
			this.currEventID = new String();
			this.currEventID = "";
			this.currEventName = new String();
			this.currEventName = "";
			this.currCommunity = new String();
			this.currCommunity = "";
			this.currContext = new String();
			this.currContext = "";
			this.currentApplicationActivityState = new String();
			this.currentApplicationActivityState = "";
			this.currentApplicationSubActivityState = new String();
			this.currentApplicationSubActivityState = "";
		}

		public function set myAccountID(value:String):void
		{
			this.accountID = value;
		}
		
		public function get myAccountID():String
		{
			return this.accountID;
		}
		
		public function set myTerminalID(value:String):void
		{
			this.terminalID = value;
		}
		
		public function get myTerminalID():String
		{
			return this.terminalID;
		}		
		
		public function set myAccountName(value:String):void
		{
			this.accountName = value;
		}
		
		public function get myAccountName():String
		{
			return this.accountName;
		}
		
		public function set myCurrentApplicationActivityState(value:String):void
		{
			this.currentApplicationActivityState = value;
		}
		
		public function get myCurrentApplicationActivityState():String
		{
			return this.currentApplicationActivityState;
		}

		public function set myCurrentApplicationSubActivityState(value:String):void
		{
			this.currentApplicationSubActivityState = value;
		}
		
		public function get myCurrentApplicationSubActivityState():String
		{
			return this.currentApplicationSubActivityState;
		}				
		
		public function set myCurrEventID(value:String):void
		{
			this.currEventID = value;
		}
		
		public function get myCurrEventID():String
		{
			return this.currEventID;
		}
		
		public function set myCurrEventName(value:String):void
		{
			this.currEventName = value;
		}
		
		public function get myCurrEventName():String
		{
			return this.currEventName;
		}
		
		public function get isEventIDSet():Boolean
		{
			if (this.currEventID != "") return true;
			else return false;
		}
		
		public function set myCurrentCommunity(value:String):void
		{
			this.currCommunity = value;
		}
		
		public function get myCurrentCommunity():String
		{
			return this.currCommunity;
		}
		
		public function set myCurrentContext(value:String):void
		{
			this.currContext = value;
		}
		
		public function get myCurrentContext():String
		{
			return this.currContext;
		}
	
		public function resetAllAccountSettings():void
		{
			this.accountID = "";
			this.accountName = "";
			this.currEventID = "";
			this.currEventName = "";
			this.currCommunity = "";
			this.currContext = "";
			this.currentApplicationActivityState = "";
			this.currentApplicationSubActivityState = "";
			component.DataSources.AllDataSources.currEventDetails = "";
			this.terminalID = "";
		}
	}
}