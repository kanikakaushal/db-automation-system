        	import component.DataSources.AllDataSources;
        	
        	import flash.utils.Timer;
        	
        	import mx.collections.XMLListCollection;
        	import mx.containers.Panel;
        	import mx.containers.TitleWindow;
        	import mx.controls.Alert;
        	import mx.controls.Image;
        	import mx.events.ListEvent;
        	import mx.managers.PopUpManager;
        	import mx.rpc.events.FaultEvent;
        	import mx.rpc.events.ResultEvent;
        	import mx.rpc.http.HTTPService;
        	import mx.validators.Validator;        		
            
			
            private var titleWindow:TitleWindow;
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
			private var districtID:String = new String();
			[Bindable]
			private var regionID:String = new String();
			[Bindable]
			private var anAssociationArray:Array = new Array();
			[Bindable]
			private var hasAssociationsBeenFetched:Boolean = false;
			[Bindable]
			private var colonyStringsMatch:Boolean = false;
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
				}else{
					component.DataSources.AllDataSources.isMemberUninitiated = false;					
					ageLabel.visible = false;
					age.visible = false;
					occupation.visible = false;
					occupationLabel.visible = false;
					age.text = "";
					occupation.text = "";
				}
				validateUs();
			}

    public function showEligibilityDetailsTextBox():void
    {
    	if(eligibilityCheckBox.selected == true)
    	{
    		
    		eligibilityDetails.visible = true;
    		eligibilityDetailsTextBox.visible = true;
    		eligibilityDetailsTextBox.text = "";
    		eligibilityCBSelected = true;
    	}
    	else
    	{
    		eligibilityDetails.visible = false;
    		eligibilityDetailsTextBox.visible = false;
    		eligibilityDetailsTextBox.text = "";    		
    		eligibilityCBSelected = false;
    	}
    }
	         
    public function editRegClear():void {                
        tvcId.text = "";
        fName.text = "";
        mName.text = "";
        lName.text = "";
        age.text = "";
        genderType.selectedItem = null;
        initStatus.selectedItem = null;
        branchName.selectedItem = null;
        districtName.text = "";
        regionName.text = "";
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
		lAddress.text = "";
		toDate.selectedDate = null;
		childCount.selectedIndex = 0;
		cAgeString.text = "";
    	editRegistrationStatusLabel.text = "";
    	editRegistrationResultHBox.visible = false;                
    	editRegistrationStatusLabel.visible = false;
		component.DataSources.AllDataSources.memberMohallaSadanIndex = 0;
		component.DataSources.AllDataSources.memberMohallaSadanName = "";
		component.DataSources.AllDataSources.editRegLocalAddressString = "";
		lblInitParentName.visible = false;
		initParentName.visible = false;		
		initParentName.text = "";
		lblLetterNumber.visible = false;
		letterNumber.visible = false;
		letterNumber.text = "";
		component.DataSources.AllDataSources.isTVCAssociate = false;
		component.DataSources.AllDataSources.isMemberInitiated = false;
		component.DataSources.AllDataSources.isMemberJigyasu = false;
		component.DataSources.AllDataSources.isMemberUninitiated = false;
		component.DataSources.AllDataSources.aPrimaryTVCID = "";
		component.DataSources.AllDataSources.hasPrimaryIDBeenFetched = false;
		component.DataSources.AllDataSources.isLicenseeDetailsAvailable = false;
		hasAssociationsBeenFetched = false;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */
		licenseeName.text = "";
		licenseeRelation.prompt =  "Relation";	
		lblLicenseeName.visible = false;
		licenseeName.visible = false;
		lblLicenseeRelation.visible = false;
		licenseeRelation.selectedItem = null;
		component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */								
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
 
 /* This function modifies the Label for Local Address by prefixing the colony prefix incase PQ or SQ is selected */
			public function changeHandler(event:Event):void {
				if((accoTypeSelected == 'Private Quarter') || (accoTypeSelected == 'Sabha Quarter'))
				{
/* 					localAddressLbl.label = "Local Address:";
					localAddressLbl.label = localAddressLbl.label.concat("(" + (event.target).selectedItem.data + ")");
					lAddress.textInput
*/
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


/*********************HTTP Service Calls ******************************/		
	        private var service:HTTPService;
	        private var serviceCalled:String = new String("");
						
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
						case "tvc/associations/view.do":	
							requestObj.TvcId = tvcId.text;
							service.request = requestObj;
						break;
						case "tvc/edit.do":
							var anXML:XML = new XML(new String(branchName.selectedItem));
							requestObj.TvcId = tvcId.text;
							requestObj.FirstName = fName.text;
							requestObj.MiddleName = mName.text;
							requestObj.LastName = lName.text;
							if(!component.DataSources.AllDataSources.isTVCAssociate &&(component.DataSources.AllDataSources.assocCount > 0))
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
							requestObj.Age = age.text;
							requestObj.Gender = genderType.text;
							requestObj.Occupation = occupation.text;
							requestObj.InitiationStatus = initStatus.text;
							if(component.DataSources.AllDataSources.isMemberJigyasu)
								requestObj.RecommendationType = "ICBS";
							else
								requestObj.RecommendationType = "";							
							requestObj.JigyasuLetterNo = letterNumber.text;
							requestObj.InitiatedParent = initParentName.text;	
							requestObj.Branch = branchID;
							requestObj.District = districtID;
							requestObj.Region = regionID;
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
							requestObj.LocalAddress = lAddress.text;
							if(lAccoType.text == "Private Quarters")
								requestObj.LicenseeRelation = licenseeRelation.text;
							else
								requestObj.LicenseeRelation = "";							
							requestObj.ToDate = toDate.text;
							requestObj.ChildCount = childCount.text;
							requestObj.ChildrenAges = cAgeString.text;
							requestObj.IsDuplicate = false;
							/*requestObj.BhandaraId = component.DataSources.AllDataSources.selectedTVCRegistrationEntry.@bhandaraId;*/
							service.request = requestObj;		
						break;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */						
						case "tvc/licensee.do":
							requestObj.LocalAddress = lAddress.text;
							requestObj.ColonyName = accoDetailsComboBox.text;
							service.request = requestObj;						
						break;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */								
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
		        	editRegistrationStatusLabel.text = "Record update unsuccessful!";
	        		editRegistrationStatusLabel.visible = true;
	        		editRegistrationResultHBox.visible = true;
	        		submitButton.enabled = false;
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
					case "tvc/associations/view.do":	
					    component.DataSources.AllDataSources.aListOfAssociations = new XMLList(event.result);
					    /* Putting this check to avoid the association calls being made repetitively */
					    hasAssociationsBeenFetched = true;
			 			/* Putting this check to avoid the association calls being made repetitively */
			 			if(component.DataSources.AllDataSources.aListOfAssociations.associations.length() != 0)
						{
							for each (var child:XML in component.DataSources.AllDataSources.aListOfAssociations.associations.*)
							{
								component.DataSources.AllDataSources.anAssociationArray.push({id:child.@id, fName:child.@fName, lName:child.@lName, relation:child.@relation,
														gender:child.@sex, branchName:child.@branch});
							}
							component.DataSources.AllDataSources.associationArrayCollection.source = component.DataSources.AllDataSources.anAssociationArray;
							component.DataSources.AllDataSources.assocCount = component.DataSources.AllDataSources.associationArrayCollection.length;
						}					    
					break;										
					case "tvc/edit.do":
		        	editRegistrationStatusLabel.text = "Record update successful!";
	        		editRegistrationStatusLabel.visible = true;
	        		editRegistrationResultHBox.visible = true;
/* And clear the association arrays too */	        		
					component.DataSources.AllDataSources.anAssociationArray.length = 0;	//resetting the array for next use
					component.DataSources.AllDataSources.associationArrayCollection.removeAll();
					component.DataSources.AllDataSources.assocCount = component.DataSources.AllDataSources.associationArrayCollection.length;
/* And clear the association arrays too */
					hasAssociationsBeenFetched = false;
	        		break;
/* Change made on 5-Feb to take care of anything-to-PQ transitions */						
					case "tvc/licensee.do":
/* Change put in on 31-01-2011 to address the submit button getting enabled even if Licensee Name is not present*/
					licenseeName.text = "";
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
						createAlertPopup(lAddress.text + ", " + accoDetailsComboBox.text + " is'nt a Private Quarter. Please enter valid address!", "Search Licensee");					}
					validateUs();
					break;	        		
/* Change made on 5-Feb to take care of anything-to-PQ transitions */						
					default:
						Alert.show("Unrecognized service called");        		
		    	}
			}
/*********************HTTP Service Calls ******************************/		

	public function getAssociationInformation(url:String):void{
		if(!hasAssociationsBeenFetched)
			callService('tvc/associations/view.do');
	}

	public function assignID():void
	{
		if(branchName.selectedItem != null)
		{
			branchID = branchName.selectedItem.@id;
			districtID = branchName.selectedItem.@districtId;
			regionID = branchName.selectedItem.@regionId;
		}
	}
	
	public function submitRequest():void
	{
		branchID = "";
		districtID = "";
		regionID = "";
		assignID();
		if((branchID != ""))
			callService('tvc/edit.do');
		else
			Alert.show("Please select a branch!");
	}
/* Added on 4-Dec-2010 to implement CR # 29 */	
	private function validateUs() :void
	{
		if(lAccoType.text == "Private Quarters" && (!component.DataSources.AllDataSources.isTVCAssociate)){
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13,val15]).length == 0);
			else if((initStatus.text == 'Initiated') || (initStatus.text == ''))
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val8,val9,val11,val12,val13,val15]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13,val15]).length == 0);			
		}
		else if(lAccoType.text == "Private Quarters" && (component.DataSources.AllDataSources.isTVCAssociate)){
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13]).length == 0);
			else if((initStatus.text == 'Initiated') || (initStatus.text == ''))
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val8,val9,val11,val12,val13]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13]).length == 0);			
		}
		else{
			if(initStatus.text == 'Uninitiated')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13]).length == 0);
			else if((initStatus.text == 'Initiated') || (initStatus.text == ''))
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val8,val9,val11,val12,val13]).length == 0);
			else if(initStatus.text == 'Jigyasu')
				submitButton.enabled = (Validator.validateAll([val1,val2,val3,val4,val5,val6,val8,val9,val10,val11,val12,val13]).length == 0);			
		}

	}

	
	private function validateMe(validators:Array):void
	{
		submitButton.enabled = (Validator.validateAll(validators).length == 0);
	}
/* Added on 4-Dec-2010 to implement CR # 29 */

	private function filterSmartAssist(event:Event):void{
		component.DataSources.AllDataSources.smartAssistAccoDetailsIndex = (event.target).selectedIndex;
/* Change added to speed automate entry of data in Local Address instead of having to explicitly add through Smart Assist */            	
		if(lAccoType.selectedLabel == 'Sadan')
			lAddress.text = (event.target).selectedLabel;
/* Change made on 6-Feb to take care of Licensee Name remaining in details box */
		else if(lAccoType.selectedLabel == 'Private Quarters'){
				lAddress.text = "";
				licenseeName.text = "";
				licenseeRelation.prompt =  "Relation";	
				licenseeRelation.selectedItem = null;
		}
/* Change made on 6-Feb to take care of Licensee Name remaining in details box */
		else
			lAddress.text = "";
/* Change added to speed automate entry of data in Local Address instead of having to explicitly add through Smart Assist */            	
		validateUs();
	}
	
	private function validateSmartAssist(event:Event):void{
		var aTempString:String = (event.target).selectedItem.@name;
		/* Added by Gaurav Caprihan on 23-July for Bug # 96 */
		if(lAddress.text != "") lAddress.text = "";
		/* Added by Gaurav Caprihan on 23-July for Bug # 96 */
		switch (aTempString)
			{
				case "Private Quarters":
	/* Change made on 5-Feb to take care of anything-to-PQ transitions */							
					component.DataSources.AllDataSources.shouldSmartAssistButtonBeEnabled = true;
					if(!component.DataSources.AllDataSources.isTVCAssociate)
					{
						component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = true;
						licenseeName.text = "";
						licenseeRelation.prompt =  "Relation";	
						licenseeRelation.selectedItem = null;
					}
	/* Change made on 5-Feb to take care of anything-to-PQ transitions */														
				break;						
				case "Outside Dayalbagh":
					component.DataSources.AllDataSources.shouldSmartAssistButtonBeEnabled = false;
					component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;							
					licenseeName.text = "";
					licenseeRelation.text =  "";
					licenseeRelation.prompt =  "Relation";	
					licenseeRelation.selectedItem = null;
				break;
				case "Sabha Quarters":
				case "Sadan":
					if((event.target).selectedLabel == 'Sabha Quarters')
						component.DataSources.AllDataSources.smartAssistTargetTabToBeEnabled = 0;
					else if ((event.target).selectedLabel == 'Sadan')
						component.DataSources.AllDataSources.smartAssistTargetTabToBeEnabled = 1;
					component.DataSources.AllDataSources.smartAssistAccoDetailsIndex = -1;
					component.DataSources.AllDataSources.shouldSmartAssistButtonBeEnabled = true;
					licenseeName.text = "";
					licenseeRelation.prompt =  "Relation";	
					licenseeRelation.selectedItem = null;							
					component.DataSources.AllDataSources.shouldLicenseeDetailsBeShown = false;							
				break;
				default:
					Alert.show("Unrecognized Option called");
			}
			validateUs();
	}

	public function displayEditAssociationPopup():void
	{
		//Means we have setting up a PrimaryTVC for which associations have already been set up
		PopUpManager.addPopUp(component.DataSources.AllDataSources.anEditTVCAssocScreen, this, true);
		PopUpManager.centerPopUp(component.DataSources.AllDataSources.anEditTVCAssocScreen);
		getAssociationInformation('tvc/associations/view.do');				
	}

	public function clearAssocPopup():void{
		PopUpManager.removePopUp(component.DataSources.AllDataSources.anEditTVCAssocScreen);
	}

/* Change made on 5-Feb to take care of anything-to-PQ transitions */
	public function getLicenseeName():void
	{
		if((lAccoType.text == "Private Quarters") && (!component.DataSources.AllDataSources.isTVCAssociate)){
			if((lAddress.text != "") && (accoDetailsComboBox.text != "Select One")){
				callService("tvc/licensee.do");
			}
			else
			{
				/* Change put in on 12-02-2011 to address previously entered Licensee Details not getting cleared */
				licenseeName.text = "";
				licenseeRelation.prompt =  "Relation";	
				licenseeRelation.selectedItem = null;
				/* Change put in on 12-02-2011 to address previously entered Licensee Details not getting cleared */

				accoDetailsComboBox.setFocus();
				createAlertPopup("Please enter valid values for Colony Name and Quarter Number/Prefix!", "Search Licensee");
			}										
		}
		validateUs();
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
/* Change made on 5-Feb to take care of anything-to-PQ transitions */
        else
        	submitButton.enabled = true;		
	}				
/* Change made on 5-Feb to take care of anything-to-PQ transitions */						