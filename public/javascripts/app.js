$(document).on('change', '.btn-file :file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
});

$(document).ready( function() {
		
    $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
        $("#picture_field").val(label);
    });
    
    $('.listDetails').on('click', function() {
    	var id = this.id;
    	    	    
		$("#postUserId").val($("#"+id+" #listingUserId").text());
    	$("#postListingId").val($("#"+id+" #listingId").text());
    	if($("#"+id+" #listingTransactionType").text() == "R"){
    		$("#postTransactionType").text("Rent");   
    		if($("#"+id+" #listingPeriod").text() == "D") {
    			$("#postPeriod").text("Daily");    			
    		} else if($("#"+id+" #listingPeriod").text() == "W") {
    			$("#postPeriod").text("Weekly");    			
    		} else if($("#"+id+" #listingPeriod").text() == "M") {
    			$("#postPeriod").text("Monthly");    			
    		} else if($("#"+id+" #listingPeriod").text() == "Y") {
    			$("#postPeriod").text("Yearly");    			
    		}
        	$("#postFrom").text($("#"+id+" #listingFrom").text().split(" ")[0]);
        	$("#postTill").text($("#"+id+" #listingTill").text().split(" ")[0]);
    	} else if($("#"+id+" #listingTransactionType").text() == "S"){
    		$("#postTransactionType").text("Sell"); 
    		$("#postPeriod").text("N/A"); 
    		$("#postFrom").text("N/A");
        	$("#postTill").text("N/A");
    	} else if($("#"+id+" #listingTransactionType").text() == "B"){
    		$("#postTransactionType").text("Buy");    		
    		$("#postPeriod").text("N/A"); 
    		$("#postFrom").text("N/A");
        	$("#postTill").text("N/A");
    	}
    	$("#postTitle").text($("#"+id+" #listingTitle").text());
    	$("#postCategory").text($("#"+id+" #listingCategory").text());
    	$("#postPrice").text($("#"+id+" #listingPrice").text() + " €");  
    	if($("#"+id+" #listingNegotiable").text()) {
    		$("#postNegotiable").text("Yes");   		
    	} else {
    		$("#postNegotiable").text("No"); 
    	}
    	$("#postDescription").text($("#"+id+" #listingDescription").text());
    	
    	if($("#"+id+" #listingImgPath").text()!="") {
    		$("#postImg").attr("src","assets/uploaded/"+ $("#"+id+" #listingImgPath").text());    		
    	} else {
    		$("#postImg").attr("src","assets/images/request.png");
    	}
    	
    });
    
    $(function(){
        $('.showTooltip').tooltip({
			animated : 'fade',
			placement : 'top',
			container: 'body',
			delay: {show:500, hide:0}
        });
    });
    
    $('#listingTypeInfo').change( function() {
    	var selectedValue = $("#listingTypeInfo option:selected").val();
    	
    	if(selectedValue == "R") {
    		$("#pictureId").hide();
    		$("#transactionType")
	    		.find('option')
			    .remove()
			    .end()
	    		.append('<option value="R">Rent</option>')
	    		.append('<option value="B">Buy</option>');
    	} else {
    		$("#pictureId").show();
    		$("#transactionType")
	    		.find('option')
			    .remove()
			    .end()
	    		.append('<option value="R">Rent</option>')
	    		.append('<option value="S">Sell</option>');
    	}
	 });
    
    $('#transactionType').change( function() {
    	var selectedValue = $("#transactionType option:selected").val();    	
    	if(selectedValue == "R") {
    		$("#pricePeriod").attr("disabled", false);
    		$("#transactionStart").attr("disabled", false);
    		$("#transactionEnd").attr("disabled", false);    		
    	} else {
    		$("#pricePeriod").attr("disabled", false);
    		$("#transactionStart").attr("disabled", false);
    		$("#transactionEnd").attr("disabled", true);    		
    	}
	 });
    
 
});