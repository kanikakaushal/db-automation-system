	import component.DataSources.AllDataSources;
	
	import flash.utils.Timer;
	
	import mx.collections.XMLListCollection;
	import mx.containers.Panel;
	import mx.controls.Alert;
	import mx.controls.DateField;
	import mx.controls.Image;
	import mx.core.Application;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.validators.Validator;
				
	private var accoTypeSelected:String = new String();
	private var ads:AllDataSources = new AllDataSources();
	            
	private var panel:Panel;
	private var eligibilityCBSelected:Boolean = false;
	private var delayTimer:Timer;
	[Bindable]
	private var cellWidth:int = 150;
	[Bindable]
	private var branchID:String = new String();
	[Bindable]
	private var branchNameString:String = new String();
	[Bindable]
	private var districtID:String = new String();
	[Bindable]
	private var districtNameString:String = new String();
	[Bindable]
	private var regionID:String = new String();
	[Bindable]
	private var regionNameString:String = new String();
	[Bindable]
	private var firstName:String = "";
	[Bindable]
	private var colonyStringsMatch:Boolean = false;
	[Bindable]
	public static var licenseeDetails:String = "";
	[Bindable]
	private var errorsGettingLicenseeDetails:Boolean = false;	
      
    public function checkStatus():void
	{
		if(initStatus.text == "Uninitiated" || initStatus.text == "Jigyasu"){
			if(initStatus.text == "Uninitiated")
				component.DataSources.AllDataSources.isMemberUninitiated = true;
			else
				component.DataSources.AllDataSources.isMemberUninitiated = false;
			ageLabel.visible = true;
			age.visible = true;
			occupation.visible = true;
			occupationLabel.visible = true;
		}else if ((initStatus.text == "Initiated") || (initStatus.text == "")){
			component.DataSources.AllDataSources.isMemberUninitiated = false;
			ageLabel.visible = false;
			age.visible = false;
			occupation.visible = false;
			occupationLabel.visible = false;
			age.text = "";
			occupation.text = "";
			letterNumber.text = "";
			initParentName.text = "";
		}
		validateUs();
	}
			                  
	public function newRegClear():void {                
	    fName.text = "";
	    mName.text = "";
	    lName.text = "";    
	    genderType.selectedItem = null;
	    initStatus.selectedItem = null;
	    branchName.selectedItem = null;
	    inchargeName.text = "";
	    inchargeName.toolTip = "";
	    districtName.text = "";
	    districtName.toolTip = "";
	    regionName.text = "";
	    regionName.toolTip = "";
	    homeAddress.text = "";
	    city.text = "";
	    stateName.selectedItem = null;
	    countryName.selectedItem = null;
	    pinCode.text = "";
	    stateName.text = "";
	    countryName.text = "";
	    eligibilityCheckBox.selected = false;
		eligibilityDetailsTextBox.text = "";
		lAccoType.selectedIndex = -1;
		accoDetailsComboBox.selectedIndex = -1;
		accoDetailsComboBox.prompt = "Select One";
		component.DataSources.AllDataSources.smartAssistAccoDetailsIndex = -1;
		component.DataSources.AllDataSources.smartAssistLAddressSelected = "";
		lAddress.text = "";
		toDate.selectedDate = null;
		childCount.selectedIndex = 0;
		cAgeString.text = "";
		radioButtonIsPrimary.selected = true;
		assocToPrimaryRel.selectedItem = null;
		assocToPrimaryRel.prompt = "Relation";
		component.DataSources.AllDataSources.licenseeDetails = "";
		licenseeRelation.prompt =  "Relation";
		lblLicenseeRelation.visible = false;
		licenseeRelation.selectedItem = null;
		isBarCodedTVCAvailable.selected = false;
		bcMemberID.text = "";
		ageLabel.visible = false;
		component.DataSources.AllDataSources.isMemberInitiated = false;
		component.DataSources.AllDataSources.isMemberJigyasu = false;
		component.DataSources.AllDataSources.isMemberUninitiated = false;		
		age.visible = false;
		occupation.visible = false;
		lblInitParentName.visible = false;
		initParentName.visible = false;		
		initParentName.text = "";
		lblLetterNumber.visible = false;
		letterNumber.visible = false;
		letterNumber.text = "";
		occupationLabel.visible = false;
		age.text = "";
		occupation.text = "";
		validateUs();
/* solution for bug # 22 raised by Ankit */
					bcMemberID.editable = true;
/* solution for bug # 22 raised by Ankit */	
		/* solution to bug of Smart Assist Staying Enabled after Clear 29-SEP-2011 */
		btnSmartAssist.enabled = false;			
		/* solution to bug of Smart Assist Staying Enabled after Clear 29-SEP-2011 */
		
	}
            
    private function populateAccoTypeComboBox():void
    {
		var accoTypeArray:Array = new Array( 
		    {label:"Private Quarter", data:""}, 
		    {label:"Sabha Quarter", data:""}, 
		    {label:"Sadan", data:""}, 
		    {label:"Outside Dayalbagh", data:""} 
			);
			lAccoType.prompt = "Select One";										 
			lAccoType.dataProvider = accoTypeArray;
    }
            
    private function populateAccoDetailsComboBox(event:ListEvent):void
    {
    	accoTypeSelected = event.currentTarget.selectedItem.label;	//Going to use this in changeHandler
		switch(event.currentTarget.selectedItem.label)
		{
			case 'Private Quarter':
			case 'Sabha Quarter':
			var mohallaNames:Array = new Array( 
			    {label:"Karyavir Nagar", data:"1/"}, 
			    {label:"Saran Ashram Nagar", data:"2/"}, 
			    {label:"Prem Nagar", data:"3/"}, 
			    {label:"Vidyut Nagar", data:"4/"}, 
			    {label:"Swet Nagar", data:"5/"}, 
			    {label:"Soami Nagar", data:"6/"}
			); 
			 
			accoDetailsComboBox.prompt = "Select One";
			accoDetailsComboBox.dataProvider = mohallaNames; 
			accoDetailsComboBox.addEventListener(Event.CHANGE, changeHandler);
			accoDetailsComboBox.visible = true;
			accoDetails.label = "Mohalla Name:";
			accoDetails.visible = true;
			localAddressLbl.label = "Local Address:";
			break;					
			case 'Sadan':
			var sadanNames:Array = new Array( 
			    {label:"Dormitory", data:""}, 
			    {label:"Yatri Sadan", data:""}, 
			    {label:"Pilgrim Shed", data:""}, 
			    {label:"Old Exhibition Complex", data:""}, 
			    {label:"Youth Hostel", data:""}, 
			    {label:"Old DB School", data:""},
			    {label:"New DB School", data:""}
			); 
			 
			accoDetailsComboBox.prompt = "Select One";
			accoDetailsComboBox.dataProvider = sadanNames; 
			accoDetailsComboBox.addEventListener(Event.CHANGE, changeHandler);
			accoDetailsComboBox.visible = true;
			accoDetails.label = "Sadan Name:";
			accoDetails.visible = true;
			localAddressLbl.label = "Room No:";
			break;
			case 'Outside Dayalbagh':
			accoDetailsComboBox.visible = false;
			accoDetails.visible = false;
			localAddressLbl.label = "Local Address:";						
			break;					
			default:
			Alert.show("Unidentified container:" + event.currentTarget.selectedItem);
		}
    }
            
    public function showEligibilityDetailsTextBox():void
    {
    	if(eligibilityCheckBox.selected == true)
    	{
    		
    		eligibilityDetails.visible = true;
    		eligibilityDetailsTextBox.visible = true;
    		eligibilityCBSelected = true;
    	}
    	else
    	{
    		eligibilityDetails.visible = false;
    		eligibilityDetailsTextBox.visible = false;
    		eligibilityCBSelected = false;
    	}
    }
 
 /* This function modifies the Label for Local Address by prefixing the colony prefix incase PQ or SQ is selected */
			public function changeHandler(event:Event):void {
				if((accoTypeSelected == 'Private Quarter') || (accoTypeSelected == 'Sabha Quarter'))
				{
/* 					localAddressLbl.label = "Local Address:";
					localAddressLbl.label = localAddressLbl.label.concat("(" + (event.target).selectedItem.data + ")");
 */					lAddress.text;
 				}	
			} 
			
			private function handleSearchChange():void 
			{
				if (delayTimer != null && delayTimer.running) 
				{
					delayTimer.stop();
				}
				
				if (branchName.searchText.length > 2) 
				{
//					aBranchDP.removeAll();
					delayTimer = new Timer( 500, 1 );
					delayTimer.addEventListener(TimerEvent.TIMER, showComboAll);
					delayTimer.start();
					
					branchName.enabled = false;
				}
			}

			private function showComboAll( event:TimerEvent ):void
			{
				callService("branches.do");
			}

			public function getLicenseeName():void
			{
				if((lAccoType.text == "Private Quarters") && (radioButtonIsPrimary.selected)){
					if((lAddress.text != "") && (accoDetailsComboBox.text != "Select One")){
							callService("tvc/licensee.do");
					}
					else
					{
						accoDetailsComboBox.setFocus();
						createAlertPopup("Please enter valid values for Colony Name and Quarter Number/Prefix!", "Search Licensee");
					}										
				}
				validateUs();
			}

/*********************HTTP Service Calls ******************************/		
	        private var service:HTTPService;
	        private var serviceCalled:String = new String("");

			public function updateDS():void
			{

			}
			
			public function callService(url:String):void
			{
		            var requestObj:Object = new Object();
		            service = new HTTPService();
		            
		            serviceCalled = url;
		            
					switch (serviceCalled)
					{
						case "branches.do":	
							requestObj.BranchSearch = branchName.searchText;
							service.request = requestObj;
						break;
						case "tvc/new.do":
							var anXML:XML = new XML(new String(branchName.selectedItem));
							requestObj.FirstName = fName.text;
							requestObj.MiddleName = mName.text;
							requestObj.LastName = lName.text;
							if(radioButtonIsPrimary.selected &&(component.DataSources.AllDataSources.assocCount > 0))
							{
								requestObj.AssocString = "";
								for(var i:int;i<component.DataSources.AllDataSources.associationArrayCollection.length;i++) {
									requestObj.AssocString = requestObj.AssocString + 
										component.DataSources.AllDataSources.associationArrayCollection[i].id;
									requestObj.AssocString = requestObj.AssocString + "," + 
										component.DataSources.AllDataSources.associationArrayCollection[i].relation;
									requestObj.AssocString = requestObj.AssocString + "," + 
										component.DataSources.AllDataSources.associationArrayCollection[i].gender;										
									if(i < (component.DataSources.AllDataSources.associationArrayCollection.length - 1))
										requestObj.AssocString = requestObj.AssocString + ";";	
									}
							}
							else
							requestObj.AssocString = "";
							if(isBarCodedTVCAvailable.selected && (bcMemberID.text != ""))
								requestObj.MemberId = bcMemberID.text;
							requestObj.IsAssociate = !radioButtonIsPrimary.selected;
							requestObj.TerminalID = Application.application.aHandleToAccount.terminalID;
							requestObj.Age = age.text;
							requestObj.Occupation = occupation.text;
							requestObj.Gender = genderType.text;
							requestObj.InitiationStatus = initStatus.text;
							if(component.DataSources.AllDataSources.isMemberJigyasu)
								requestObj.RecommendationType = "ICBS";
							else
								requestObj.RecommendationType = "";							
							requestObj.JigyasuLetterNo = letterNumber.text;
							requestObj.InitiatedParent = initParentName.text;	
							requestObj.Branch = branchID;
							requestObj.BranchName = branchNameString;
							requestObj.District = districtID;
							requestObj.DistrictName = districtNameString;
							requestObj.Region = regionID;
							requestObj.RegionName = regionNameString;
							requestObj.HomeAddress1 = homeAddress.text;
							requestObj.HomeAddress2 = "";
							requestObj.City = city.text;
							requestObj.State = stateName.text;
							requestObj.Country = countryName.text;
							requestObj.Pincode = pinCode.text;
							if(eligibilityCheckBox.selected)
								requestObj.Permission = "Special Permission";
							else
								requestObj.Permission = "";
							requestObj.PermissionRemarks = eligibilityDetailsTextBox.text;
							requestObj.AccoType = lAccoType.text;
							if((lAccoType.text == "Private Quarters") || (lAccoType.text == "Sabha Quarters") || (lAccoType.text == "Outside Dayalbagh"))
							{
								requestObj.Sadan = "";
								requestObj.Colony = accoDetailsComboBox.text;
							}
							else if((lAccoType.text == "Sadan"))
							{
								requestObj.Colony = ""; 
								requestObj.Sadan = accoDetailsComboBox.text;
							}
							if(lAccoType.text == "Private Quarters")
								requestObj.LicenseeRelation = licenseeRelation.text;
							else
								requestObj.LicenseeRelation = "";
							requestObj.LocalAddress = lAddress.text;
							requestObj.ChildCount = childCount.text;
							requestObj.ChildrenAges = cAgeString.text;
							requestObj.ToDate = toDate.text;
							requestObj.IsDuplicate = false;
							/*requestObj.BhandaraId = bhandaraIdComboBox.selectedItem.@id;*/
							service.request = requestObj;		
						break;
						case "member.do":	
							if(isBarCodedTVCAvailable.selected && (bcMemberID.text != ""))
								requestObj.MemberId = bcMemberID.text;
							service.request = requestObj;
						break;
						case "tvc/licensee.do":
							requestObj.LocalAddress = lAddress.text;
							requestObj.ColonyName = accoDetailsComboBox.text;
							service.request = requestObj;						
						break;
						default:
							Alert.show("Unrecognized service called");
					}
		            service.url = component.DataSources.AllDataSources.hostName + url;
		            service.resultFormat = "e4x";
	
		            service.method = "GET";
		            service.addEventListener("result", httpResult);
		            service.addEventListener("fault", httpFault);
			        service.send();
	  		}                             
	
	        public function httpFault(event:FaultEvent):void {
	                var faultstring:String = event.fault.faultString;
	                Alert.show(faultstring);
}
	
		    public function httpResult(event:ResultEvent):void
		    {
		    	switch (serviceCalled)
		    	{
		        	case "branches.do":
					    component.DataSources.AllDataSources.branchDetails = new XMLList(event.result);
					    branchName.dataProvider = new XMLListCollection(component.DataSources.AllDataSources.branchDetails.branches.branch);
						branchName.enabled = true;
						branchName.setFocus();
					    branchName.search();
					break;
					case "tvc/new.do":
					component.DataSources.AllDataSources.aTVCRegistration = new XMLList(event.result);
/* solution for bug # 22 raised by Ankit */
					bcMemberID.editable = true;
/* solution for bug # 22 raised by Ankit */						
					if(component.DataSources.AllDataSources.aTVCRegistration.registrations.children().length() == 0){
						createTVCCardAlreadyExitsPopup();
					}
					else{
						if(component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@id != ""){
							createPopupPanelElements();
							if(radioButtonIsAssociate.selected)
							{
								associateTVCID();
								component.DataSources.AllDataSources.associationArrayCollection.source = 
									component.DataSources.AllDataSources.anAssociationArray;
								newRegClear();
								component.DataSources.AllDataSources.assocCount = component.DataSources.AllDataSources.associationArrayCollection.length;
							}
							else if(radioButtonIsPrimary.selected &&(component.DataSources.AllDataSources.assocCount > 0))
							{
								clearAssocPopup();
							}
							else if(radioButtonIsPrimary.selected &&(component.DataSources.AllDataSources.assocCount == 0))
								newRegClear();
						}
						else
							createTVCCardNotCreatedPopup();
					}
					break;
					case "member.do":	
					component.DataSources.AllDataSources.bcMemberDetails = new XML(event.result[0]);
/* solution for bug # 22 raised by Ankit */
					bcMemberID.editable = false;
/* solution for bug # 22 raised by Ankit */					
					if(component.DataSources.AllDataSources.bcMemberDetails.children().length() == 0){
						createMemberRecordNotFoundPopup();
					}
					else{
						var memberInitiationStatus:String = component.DataSources.AllDataSources.bcMemberDetails.member.@initiationStatus;
						if(memberInitiationStatus == 'Uninitiated')
						{
							component.DataSources.AllDataSources.isMemberInitiated = false;
							component.DataSources.AllDataSources.isMemberJigyasu = false;
							component.DataSources.AllDataSources.isMemberUninitiated = true;
/* 							ageLabel.visible = true;
							age.visible = true;
							occupation.visible = true;
							occupationLabel.visible = true; */						
						}
						else if(memberInitiationStatus == 'Jigyasu'){
							component.DataSources.AllDataSources.isMemberInitiated = false;
							component.DataSources.AllDataSources.isMemberJigyasu = true;
							component.DataSources.AllDataSources.isMemberUninitiated = false;
						}
						else
						{
							component.DataSources.AllDataSources.isMemberInitiated = true;
							component.DataSources.AllDataSources.isMemberJigyasu = false;
							component.DataSources.AllDataSources.isMemberUninitiated = false;
/* 							ageLabel.visible = false;
							age.visible = false;
							occupation.visible = false;
							occupationLabel.visible = false; */						
						}
						if(component.DataSources.AllDataSources.anAssociationArray.length != 0){
							var arrayLength:int = component.DataSources.AllDataSources.anAssociationArray.length;
 							lAccoType.selectedIndex = component.DataSources.AllDataSources.accoType.accomodation.type.
												(@name == component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoType).@id;
							if(component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoColony == "")
								accoDetailsComboBox.selectedIndex = component.DataSources.AllDataSources.accoType.accomodation.type.
																	(@name == component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoType).field.
																	(@name == component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoSadan).@id;
							else if(component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoSadan == "")
								accoDetailsComboBox.selectedIndex = component.DataSources.AllDataSources.accoType.accomodation.type.
																	(@name == component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoType).field.
																	(@name == component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoColony).@id;
							lAddress.text = component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].accoAddress;
							toDate.selectedDate = DateField.stringToDate(component.DataSources.AllDataSources.anAssociationArray[arrayLength - 1].stayToDate, "MM/DD/YYYY");
						}
//						submitButton.enabled = false;
						var aVal:String = fName.text;
					}
/* Change put on 04-Feb to take care of the Licensee Details not showing up when associate is staying in PQ */
						if((component.DataSources.AllDataSources.anAssociationArray.length != 0) &&
								radioButtonIsPrimary.selected && 
									(lAccoType.selectedIndex == 0))
								component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
						else
							component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;					
/* Change put on 04-Feb to take care of the Licensee Details not showing up when associate is staying in PQ */
					break;
					case "tvc/licensee.do":
/* Change put in on 31-01-2011 to address the submit button getting enabled even if Licensee Name is not present*/
					component.DataSources.AllDataSources.licenseeDetails = "";
					licenseeRelation.prompt =  "Relation";	
					licenseeRelation.selectedItem = null;
/* Change put in on 31-01-2011 to address the submit button getting enabled even if Licensee Name is not present*/					
					var aTempXML:XMLList = new XMLList(event.result);
					if(aTempXML.children().length() != 0){
						errorsGettingLicenseeDetails = false;
						component.DataSources.AllDataSources.licenseeDetails = aTempXML.licensee.@name;
					}
					else{
						errorsGettingLicenseeDetails = true;
						createAlertPopup(lAddress.text + ", " + accoDetailsComboBox.text + " is'nt a Private Quarter. Please enter valid address!", "Search Licensee");
					}
					validateUs();
					break;
					default:
						Alert.show("Unrecognized service called");        		
		    	}
			}

	public function clearAssocPopup():void{
		component.DataSources.AllDataSources.anAssociationArray.length = 0;	//resetting the array for next use
		component.DataSources.AllDataSources.associationArrayCollection.removeAll();
		component.DataSources.AllDataSources.assocCount = component.DataSources.AllDataSources.associationArrayCollection.length;
		PopUpManager.removePopUp(component.DataSources.AllDataSources.aTVCAssocScreen);
		newRegClear();		
	}			
/*********************HTTP Service Calls ******************************/		

	public function assignID():void
	{
		if(branchName.selectedItem != null)
		{
			branchID = branchName.selectedItem.@id;
			branchNameString = branchName.selectedItem.@name;
			districtID = branchName.selectedItem.@districtId;
			districtNameString = branchName.selectedItem.@districtSuffix;
			regionID = branchName.selectedItem.@regionId;
			regionNameString = branchName.selectedItem.@regionSuffix;
		}
	}
	
	public function submitRequest():void
	{
		submitButton.enabled = false;
		branchID = "";
		districtID = "";
		regionID = "";
		assignID();
		
		if((lAccoType.text == "Private Quarters") || (lAccoType.text == "Sabha Quarters")){
			if((lAccoType.text == "Private Quarters") && (radioButtonIsPrimary.selected) 
                	&& (Validator.validateAll([val15]).length != 0)){
                component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
                createAlertPopup("Please select the licensee relation!", "Licensee relationship Missing");	
             }else if (branchID == "" || !initStatus){
             	createAlertPopup("Please select a branch!", "Branch Details Missing");
             }else{
             	if(radioButtonIsPrimary.selected && (component.DataSources.AllDataSources.associationArrayCollection.length > 0))
				{
					//Means we have setting up a PrimaryTVC for which associations have already been set up
					PopUpManager.addPopUp(component.DataSources.AllDataSources.aTVCAssocScreen, this, true);
					PopUpManager.centerPopUp(component.DataSources.AllDataSources.aTVCAssocScreen);				
	//				parentDocument.TaskRTViewStack.selectedIndex = 6;	//Call TVCAssociations Screen
				}
				else
				{
					callService('tvc/new.do');		
				}	
             }
			
//			if((branchID != "") && (initStatus) )
//			{
//				if(radioButtonIsPrimary.selected && (component.DataSources.AllDataSources.associationArrayCollection.length > 0))
//				{
//					//Means we have setting up a PrimaryTVC for which associations have already been set up
//					PopUpManager.addPopUp(component.DataSources.AllDataSources.aTVCAssocScreen, this, true);
//					PopUpManager.centerPopUp(component.DataSources.AllDataSources.aTVCAssocScreen);				
//	//				parentDocument.TaskRTViewStack.selectedIndex = 6;	//Call TVCAssociations Screen
//				}
//				else
//				{
//					callService('tvc/new.do');		
//				}			
//			}
//			else
//				createAlertPopup("Please select a branch!", "Branch Details Missing");
		}
		else{
			if((branchID != "") && (initStatus))
			{
				if(radioButtonIsPrimary.selected && (component.DataSources.AllDataSources.associationArrayCollection.length > 0))
				{
					//Means we have setting up a PrimaryTVC for which associations have already been set up
					PopUpManager.addPopUp(component.DataSources.AllDataSources.aTVCAssocScreen, this, true);
					PopUpManager.centerPopUp(component.DataSources.AllDataSources.aTVCAssocScreen);				
	//				parentDocument.TaskRTViewStack.selectedIndex = 6;	//Call TVCAssociations Screen
				}
				else
				{
					callService('tvc/new.do');		
				}			
			}
			else
				Alert.show("Please select a branch!");			
		}
	}
	
	private function resetForm() :void
	{
		submitButton.enabled = false;
	}
	private function validateUs() :void
	{
		if((lAccoType.text == "Private Quarters") && (radioButtonIsPrimary.selected)){
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13, val15]).length == 0);
			else if(((initStatus.text == 'Initiated') || (initStatus.text == '')) && (radioButtonIsPrimary.selected))
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val7,val8,val9,val11,val12,val13,val15,val16]).length == 0);
			else if(((initStatus.text == 'Initiated') || (initStatus.text == '')) && (!radioButtonIsPrimary.selected))
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val7,val8,val9,val11,val12,val13]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13,val15]).length == 0);
		}
		else if((lAccoType.text == "Private Quarters") && (!radioButtonIsPrimary.selected)){
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13]).length == 0);
			else if(((initStatus.text == 'Initiated') || (initStatus.text == '')) && (radioButtonIsPrimary.selected))
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val7,val8,val9,val11,val12,val13]).length == 0);
			else if(((initStatus.text == 'Initiated') || (initStatus.text == '')) && (!radioButtonIsPrimary.selected))
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val7,val8,val9,val11,val12,val13]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13]).length == 0);
		}
		else{
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13]).length == 0);
			else if((initStatus.text == 'Initiated') || (initStatus.text == ''))
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val7,val8,val9,val11,val12,val13]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,mNameValidate,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13]).length == 0);			
		}		
	}

	private function validateMe(validators:Array):void
	{
	submitButton.enabled = (Validator.validateAll(validators).length == 0);
	}
	
	private function associateTVCID():void
	{
/*		anAssociationArray.push({id:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@id, 
								fName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@fName, 
								lName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@lName,
								relation:assocToPrimaryRel.selectedItem.@label,
								gender:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@sex,
								branchName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@branch});
 */
 /* Adding fields for autopopulating fields like LocalAddress etc. in case of associations to avoid tr-typing */	
		component.DataSources.AllDataSources.anAssociationArray.push({id:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@id, 
								fName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@fName, 
								lName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@lName,
								relation:assocToPrimaryRel.selectedItem.@label,
								gender:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@sex,
								branchName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@branch,
								accoType:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@accoType,								
								accoColony:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@colony,
								accoSadan:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@sadan,
								accoColony:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@colony,
								accoAddress:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@localAddress,
								licenseeName:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@localAddress,
								stayToDate:component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@toDate});
 /* Adding fields for autopopulating fields like LocalAddress etc. in case of associations to avoid tr-typing */	
 }
	
	private function getAssociationCount():String{
		return "Associations Stored: " + component.DataSources.AllDataSources.anAssociationArray.length;
	}
	
		private function createPopupPanelElements():void
		{
			var vb:VBox = new VBox();
			var hb:HBox = new HBox();
			var aLabel:Label = new Label();
			var image:Image = new Image();

			var cb:ControlBar = new ControlBar();
			var b1:Button = new Button();
		
            b1.label = "OK";
            b1.addEventListener(MouseEvent.CLICK, closePopUp);

			cb.setStyle("horizontalAlign", "center");
			cb.setStyle("verticalAlign", "middle");
            cb.addChild(b1);

            panel = new Panel();
            panel.setStyle("horizontalAlign", "center");
            panel.setStyle("verticalAlign", "middle");
            panel.setStyle("backgroundAlpha", "1");
            panel.width = 500;
            panel.height = 180;

			image.source = component.DataSources.AllImageResources.success;
			aLabel.text = 	"TVC ID # " + component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@id + " created for: " + 
							component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@fName + " " + 
							component.DataSources.AllDataSources.aTVCRegistration.registrations.registration.@lName;
			panel.title = "New TVC Registration";

			hb.setStyle("horizontalAlign", "center");
			hb.setStyle("verticalAlign", "middle");					
			hb.addChild(image);
			hb.addChild(aLabel)
			vb.setStyle("paddingBottom", 5);
			vb.setStyle("paddingLeft", 5);
			vb.setStyle("paddingRight", 5);
			vb.setStyle("paddingTop", 5);
			vb.setStyle("horizontalAlign", "center");
			vb.setStyle("verticalAlign", "middle");				
			vb.addChild(hb);										

            panel.addChild(vb);
            panel.addChild(cb);

            PopUpManager.addPopUp(panel, this, true);
            PopUpManager.centerPopUp(panel);
        }
        
		private function createTVCCardAlreadyExitsPopup():void
		{
			var vb:VBox = new VBox();
			var hb:HBox = new HBox();
			var aLabel:Label = new Label();
			var image:Image = new Image();

			var cb:ControlBar = new ControlBar();
			var b1:Button = new Button();
		
            b1.label = "OK";
            b1.addEventListener(MouseEvent.CLICK, closePopUp);

			cb.setStyle("horizontalAlign", "center");
			cb.setStyle("verticalAlign", "middle");
            cb.addChild(b1);

            panel = new Panel();
            panel.setStyle("horizontalAlign", "center");
            panel.setStyle("verticalAlign", "middle");
            panel.setStyle("backgroundAlpha", "1");
            panel.width = 500;
            panel.height = 180;

			image.source = component.DataSources.AllImageResources.success;
			aLabel.text = 	"A TVC Card has already been printed for this TVC ID";
			panel.title = "New TVC Registration";

			hb.setStyle("horizontalAlign", "center");
			hb.setStyle("verticalAlign", "middle");					
			hb.addChild(image);
			hb.addChild(aLabel)
			vb.setStyle("paddingBottom", 5);
			vb.setStyle("paddingLeft", 5);
			vb.setStyle("paddingRight", 5);
			vb.setStyle("paddingTop", 5);
			vb.setStyle("horizontalAlign", "center");
			vb.setStyle("verticalAlign", "middle");				
			vb.addChild(hb);										

            panel.addChild(vb);
            panel.addChild(cb);

            PopUpManager.addPopUp(panel, this, true);
            PopUpManager.centerPopUp(panel);
        }

		private function createMemberRecordNotFoundPopup():void
		{
			var vb:VBox = new VBox();
			var hb:HBox = new HBox();
			var aLabel:Label = new Label();
			var image:Image = new Image();

			var cb:ControlBar = new ControlBar();
			var b1:Button = new Button();
		
            b1.label = "OK";
            b1.addEventListener(MouseEvent.CLICK, closePopUp);

			cb.setStyle("horizontalAlign", "center");
			cb.setStyle("verticalAlign", "middle");
            cb.addChild(b1);

            panel = new Panel();
            panel.setStyle("horizontalAlign", "center");
            panel.setStyle("verticalAlign", "middle");
            panel.setStyle("backgroundAlpha", "1");
            panel.width = 500;
            panel.height = 180;

			image.source = component.DataSources.AllImageResources.success;
			aLabel.text = 	"Member Does Not Exist!";
			panel.title = "Search Member Record";

			hb.setStyle("horizontalAlign", "center");
			hb.setStyle("verticalAlign", "middle");					
			hb.addChild(image);
			hb.addChild(aLabel)
			vb.setStyle("paddingBottom", 5);
			vb.setStyle("paddingLeft", 5);
			vb.setStyle("paddingRight", 5);
			vb.setStyle("paddingTop", 5);
			vb.setStyle("horizontalAlign", "center");
			vb.setStyle("verticalAlign", "middle");				
			vb.addChild(hb);										

            panel.addChild(vb);
            panel.addChild(cb);

            PopUpManager.addPopUp(panel, this, true);
            PopUpManager.centerPopUp(panel);
        }
        
		private function createTVCCardNotCreatedPopup():void
		{
			var vb:VBox = new VBox();
			var hb:HBox = new HBox();
			var aLabel:Label = new Label();
			var image:Image = new Image();

			var cb:ControlBar = new ControlBar();
			var b1:Button = new Button();
		
            b1.label = "OK";
            b1.addEventListener(MouseEvent.CLICK, closePopUp);

			cb.setStyle("horizontalAlign", "center");
			cb.setStyle("verticalAlign", "middle");
            cb.addChild(b1);

            panel = new Panel();
            panel.setStyle("horizontalAlign", "center");
            panel.setStyle("verticalAlign", "middle");
            panel.setStyle("backgroundAlpha", "1");
            panel.width = 500;
            panel.height = 180;

			image.source = component.DataSources.AllImageResources.success;
			aLabel.text = 	"TVC ID Not Created. Please Retry!";
			panel.title = "TVC Creation Error";

			hb.setStyle("horizontalAlign", "center");
			hb.setStyle("verticalAlign", "middle");					
			hb.addChild(image);
			hb.addChild(aLabel)
			vb.setStyle("paddingBottom", 5);
			vb.setStyle("paddingLeft", 5);
			vb.setStyle("paddingRight", 5);
			vb.setStyle("paddingTop", 5);
			vb.setStyle("horizontalAlign", "center");
			vb.setStyle("verticalAlign", "middle");
			vb.setStyle("color", "red");
			vb.addChild(hb);										

            panel.addChild(vb);
            panel.addChild(cb);
			panel.setStyle("color", "red");            

            PopUpManager.addPopUp(panel, this, true);
            PopUpManager.centerPopUp(panel);
        }        
        
		private function createAlertPopup(messageString:String, titleString:String):void
		{
			var vb:VBox = new VBox();
			var hb:HBox = new HBox();
			var aLabel:Label = new Label();
			var image:Image = new Image();

			var cb:ControlBar = new ControlBar();
			var b1:Button = new Button();
		
            b1.label = "OK";
            b1.addEventListener(MouseEvent.CLICK, closePopUp);

			cb.setStyle("horizontalAlign", "center");
			cb.setStyle("verticalAlign", "middle");
            cb.addChild(b1);

            panel = new Panel();
            panel.setStyle("horizontalAlign", "center");
            panel.setStyle("verticalAlign", "middle");
            panel.setStyle("backgroundAlpha", "1");
            panel.width = 500;
            panel.height = 180;

			image.source = component.DataSources.AllImageResources.success;
			aLabel.text = 	messageString;
			panel.title = titleString;

			hb.setStyle("horizontalAlign", "center");
			hb.setStyle("verticalAlign", "middle");					
			hb.addChild(image);
			hb.addChild(aLabel)
			vb.setStyle("paddingBottom", 5);
			vb.setStyle("paddingLeft", 5);
			vb.setStyle("paddingRight", 5);
			vb.setStyle("paddingTop", 5);
			vb.setStyle("horizontalAlign", "center");
			vb.setStyle("verticalAlign", "middle");				
			vb.addChild(hb);										

            panel.addChild(vb);
            panel.addChild(cb);

            PopUpManager.addPopUp(panel, this, true);
            PopUpManager.centerPopUp(panel);
        }


            private function closePopUp(evt:MouseEvent):void {
                PopUpManager.removePopUp(panel);
				if((lAccoType.text == "Private Quarters") && (lAddress.text == ""))
					submitButton.enabled = false;
/* change done on 31-01-2011 to address problems with submit button getting enabled when colony prefix does'nt match */
                else if(!colonyStringsMatch)
                	submitButton.enabled = false;
/* change done on 31-01-2011 to address problems with submit button getting enabled when colony prefix does'nt match */                	
/* Change made on 5-Feb to take care of anything-to-PQ transitions */
                else if (errorsGettingLicenseeDetails)
                	submitButton.enabled = false;
                else if((lAccoType.text == "Private Quarters") && (radioButtonIsPrimary.selected) 
                	&& (Validator.validateAll([val15]).length != 0)){
                	submitButton.enabled = false;
                	component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */
                }else
                	submitButton.enabled = true;		
            }
            
            private function validateSmartAssist(event:Event):void{
            	var aTempString:String = (event.target).selectedItem.@name;
            	/* Added by Gaurav Caprihan on 23-July for Bug # 96 */
            	if(lAddress.text != "") lAddress.text = "";
            	/* Added by Gaurav Caprihan on 23-July for Bug # 96 */
            	switch (aTempString)
					{
						case "Private Quarters":
							btnSmartAssist.enabled = true;
							component.DataSources.AllDataSources.smartAssistTargetTabToBeEnabled = 2;							
							if(radioButtonIsPrimary.selected)
							{
								component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
							}
						break;						
						case "Outside Dayalbagh":
							btnSmartAssist.enabled = false;
							component.DataSources.AllDataSources.licenseeDetails = "";
							licenseeRelation.text =  "";						
							component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;							
						break;
						case "Sabha Quarters":
						case "Sadan":
							if((event.target).selectedLabel == 'Sabha Quarters')
								component.DataSources.AllDataSources.smartAssistTargetTabToBeEnabled = 0;
							else if ((event.target).selectedLabel == 'Sadan')
								component.DataSources.AllDataSources.smartAssistTargetTabToBeEnabled = 1;
							component.DataSources.AllDataSources.smartAssistAccoDetailsIndex = -1;
							btnSmartAssist.enabled = true;
							component.DataSources.AllDataSources.licenseeDetails = "";
							licenseeRelation.text =  "";						
							
							component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;
						break;						
						default:
							Alert.show("Unrecognized Option called");
					}
            }
            
            private function filterSmartAssist(event:Event):void{
            	component.DataSources.AllDataSources.smartAssistAccoDetailsIndex = (event.target).selectedIndex;
/* Change added to speed automate entry of data in Local Address instead of having to explicitly add through Smart Assist */            	
				if(lAccoType.selectedLabel == 'Sadan')
					lAddress.text = (event.target).selectedLabel;
				else
					lAddress.text = "";
/* Change added to speed automate entry of data in Local Address instead of having to explicitly add through Smart Assist */            	
            }

	private function getMemberDetails():void{
		if(bcMemberID.text != "") 
			{
				component.DataSources.AllDataSources.bcMemberDetails = null;
				callService('member.do');	
			}
	}
	
	private function resetBCEntries():void{
		bcMemberID.text = "";
	}
	
	private function setFocusOnTextBox():void{
		if (isBarCodedTVCAvailable.selected) bcMemberID.setFocus();
	}
	
	private function handleAssociateRadioButtonClick():void{
		component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;
//		component.DataSources.AllDataSources.isAssociateSelected = true;
		validateUs();
	};
	
	private function handlePrimaryRadioButtonClick():void{
//		component.DataSources.AllDataSources.isAssociateSelected = false;		
		if(lAccoType.text == "Private Quarters"){
			component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
		}
		validateUs();
	};