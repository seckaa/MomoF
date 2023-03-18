$(document).ready(function(){   
		
		$("a[name = 'linkRemoveDetail']").each(function(index){
			$(this).click(function(){
				removeDetailSectionByIndex(index);
			});
		});
	});
	
function addNextDetailSection(){
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	
	htmlDetailsection =	
		`
		<div class="form-inline" id="divDetail${divDetailsCount}">
			<input type="hidden" name="detailIDs" value="0"/>
			<label class="m-3">Name:</label>
			<input type="text"  class="form-control w-25" name="detailNames" maxlength="255"/>
			<label class="m-3">Value:</label>
			<input type="text"  class="form-control w-25" name="detailValues" maxlength="255"/>
		</div>
		`;
		
		$("#divProductDetails").append(htmlDetailsection);
		
		previousDivDetailSection = allDivDetails.last();
		previousDivdetailId = previousDivDetailSection.attr("id");
		
				htmlLinkRemove = 
					`
						<a class="btn fas fa-times-circle fa-2x icon-dark" 
						href="javascript:removeDetailSectionById('${previousDivdetailId}')"
						title="Remove this details"></a>
					`;
			
		previousDivDetailSection.append(htmlLinkRemove);
		$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id){
	$("#"+id).remove();
	
}

function removeDetailSectionByIndex(index){
	$("#divDetail"+index).remove();
}