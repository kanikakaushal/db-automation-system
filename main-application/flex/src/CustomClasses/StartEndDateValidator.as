package CustomClasses
{
	import mx.validators.DateValidator;
	import mx.validators.ValidationResult;

	public class StartEndDateValidator extends DateValidator
	{
		
		// Define Array for the return value of doValidation().
        private var results:Array;

		// Define compare String
		private var _matches : String = "";
		
		//Define dateformat
		private var _dFormat : String = "MM/DD/YYYY";

		// Define mismatch error messsage
		private var _mismatchError : String = "End date should be greater than start date";		
		
		// Constructor.
		public function StartEndDateValidator()
		{
			super();
		}

		// let's get the start date 
		public function set matches (s : String ) : void {
			_matches = s;
		}

		// let's get the correct date format
		public function set dFormat (s : String ) : void {
			_dFormat = s;
		}
		
		// create the mismatcherror
		public function set mismatchError (s : String) : void {
				_mismatchError = s;
		}		
		
        // Define the doValidation() method.
        override protected function doValidation(value:Object):Array 
        {

            var sDate: String = _matches; //start date
            var eDate: String = source.text; //end date
            var dFormat: String= _dFormat; //date format

			results = [];
        	results = super.doValidation(value);

			if(sDate == "" || eDate == ""){
				return results;
			}

			var Yindex:Number = dFormat.indexOf("YYYY"); //let's get the correct year from to place
			var Dindex:Number = dFormat.indexOf("DD"); //let's get the correct day from to place
			var Mindex:Number = dFormat.indexOf("MM"); ////let's get the correct month from to place

			var StartDate:Date = new Date(sDate.substr(Yindex, 4), sDate.substr(Mindex, 2), sDate.substr(Dindex, 2)); //create a real date from it
			var StartEnd:Date  = new Date(eDate.substr(Yindex, 4), eDate.substr(Mindex, 2), eDate.substr(Dindex, 2)); //create a real date from it

			var StartTimestamp : Number = StartDate.getTime (); // create milisec vrom start date
			var EndTimestamp : Number = StartEnd.getTime ();    // create milisec vrom end date    	
                       

	
			// any results yet ?
			if (results.length > 0)
			{
				return results;
			}
				
			if (StartTimestamp == EndTimestamp){// start date same as end date = OKAY
				 return results;
			} else if (StartTimestamp < EndTimestamp){ //end date bigger then start date = OKAY
				 return results;
			} else { // error error, get the delorean ready to fix this time disortion.
                results.push(new ValidationResult(true, null, "Date Error", _mismatchError));
                return results;
			}

        }

	}
}