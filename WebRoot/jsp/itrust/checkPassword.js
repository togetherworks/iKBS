function Check_Ess_Field(Field_Name, Error_Message) {

	if (Field_Name.value == "") {
		alert(Error_Message);
		Field_Name.focus();
		return false;
	}
	else {
		var Forbit_Char = /"/; 
		var Forbit_Char2 = /'/;
		var User_String = Field_Name.value;
		var Output1 = User_String.search(Forbit_Char);
		var Output2 = User_String.search(Forbit_Char2);
		
		if (Output1 != -1 || Output2 != -1) { 
			alert("Character ' is forbitted.");
			Field_Name.focus();
			return false;
		} 	
	}
	return true;
}

function form_validator(theform)
 {
 	var All_Field_Ok;
 	All_Field_Ok  = true;
 	
	if (Check_Ess_Field(theform.oriPassword, "Please provide us with the original password") == false) {
	 	All_Field_Ok  = false;
	}	
	
	if (All_Field_Ok) {
		if (Check_Ess_Field(theform.newPassword, "Please key in your new password") == false) {
		 	All_Field_Ok  = false;
		}
	}	
	
	if (All_Field_Ok) {
		if (Check_Ess_Field(theform.newPassword2, "Please re-confirm your new password") == false) {
			All_Field_Ok  = false;
		}
	}	

	if (All_Field_Ok) {
		if ( !(theform.newPassword2.value ==  theform.newPassword.value)) {
			alert("The 2 entries for the new Password does not match, Please check again!");
			All_Field_Ok  = false;
		}
	}	
	
	if (All_Field_Ok) {
			if ( theform.newPassword.value == theform.oriPassword.value ) {
			alert("New Password cannot be the same as Old Password !");
			All_Field_Ok  = false;
		}
	}	
	
	// Will only be true if all cond pass
	return All_Field_Ok;
} 

function dode(){
	return form_validator(this.form);
}
function dode2(filename){
	document.form.action= filename;
	document.form.submit();
}