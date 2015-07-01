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
    		$("#pricePeriod").attr("disabled", true);
    		$("#transactionStart").attr("disabled", true);
    		$("#transactionEnd").attr("disabled", true);    		
    	}
	 });
    
    $(window).load(function() {
    	var fullDate = new Date();    
        var twoDigitDate = fullDate.getDate()+"";if(twoDigitDate.length==1) twoDigitDate="0" +twoDigitDate;
        var twoDigitMonth = fullDate.getMonth()+"";if(twoDigitMonth.length==1)  twoDigitMonth="0" +twoDigitMonth;
        var currentDate = twoDigitDate + "." + twoDigitMonth + "." + fullDate.getFullYear();
    	$("#transactionStart").val(currentDate);
    	
    	var twoDigitMonth = ((fullDate.getMonth()+1)%12)+"";if(twoDigitMonth.length==1)  twoDigitMonth="0" +twoDigitMonth;
        var currentDate = twoDigitDate + "." + twoDigitMonth + "." + fullDate.getFullYear();
        
        $("#transactionEnd").val(currentDate);
        $("#transactionExpire").val(currentDate);
    	
	});
});