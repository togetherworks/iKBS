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
 	
	if (Check_Ess_Field(theform.user, "Please provide us with your Login ID/Name") == false) {
	 	All_Field_Ok  = false;
	}	
	
	// Will only be true if all cond pass
	return All_Field_Ok;
} 

function dode(){

	return form_validator(this.form);

}