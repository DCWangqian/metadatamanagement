 
var lastFocused='';
VariableSearchForm = {};

// search as the user types but not more then every 500 ms
VariableSearchForm.search = _.throttle(function(form) {
	"use strict";
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url
		history.pushState({},'' , newUrl);
	    // Set focus on last selected element
		$("#"+replaceId(lastFocused)).focus();
		getLastFocused();
	});
},500);

$(document).ready(function() {
	window.onpopstate = function(event) {
		"use strict";
		if (event.state) {
			// reload if back button has been clicked
			location.reload();
		}
	};
	$('dateRange.startDate').focus();
	getLastFocused();
});

//set the mode for datepicker
function setmode(){
	return 'search';
}
// get id of last focused element
function getLastFocused(){
	$("input").focus(function(){
		lastFocused=$(this).attr('id');
	});
}