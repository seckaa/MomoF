var dropDownCountry;
	var dataListState;
	
	$(document).ready(function(){
		dropDownCountry = $("#country");
		dataListState = $("#listStates");
		fieldState = $("#state");
		
		dropDownCountry.on("change", function(){
			loadStateForCountry();
		});
	});
	
function loadStateForCountry(){
	selectedCountry = $("#country option:selected");
	// 		alert(selectedCountry.text());
	countryId = selectedCountry.val();
	url = contextPath + "settings/list_state_by_country/" + countryId;
	
	$.get(url, function(responseJSON){
		dataListState.empty();
		$.each(responseJSON, function(index, state){
			$("<option>").val(state.name).text(state.name).appendTo(dataListState);
			fieldState.val("").focus();
		});
		
	});
}

function checkPasswordMatch(confirmPassword){
	if(confirmPassword.value != $("#password").val()){
		confirmPassword.setCustomValidity("Passwords do not Match");
	}else{
		confirmPassword.setCustomValidity("");
	}
}	
