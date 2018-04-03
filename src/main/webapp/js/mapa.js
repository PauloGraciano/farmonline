var jQueryScript = document.createElement('script');  
jQueryScript.setAttribute('src','https://maps.googleapis.com/maps/api/js?key=AIzaSyC9Z_D96ex8C6nL6vkJG-jBQWL4uw5FAwg&libraries=places');
document.head.appendChild(jQueryScript);

var map;
var infowindow;

debugger;


$(window).load(function(){
	initMap();
  });


function initMap() {
	
	var pyrmont = {
		lat : -23.6991070,
		lng : -46.6504700
	};
	
	 var request = {
			    location: pyrmont,
			    radius: '500',
			    query: 'farmacia'
			  };

	map = new google.maps.Map(document.getElementById('map'), {
		center : pyrmont,
		zoom : 15
	});

	infowindow = new google.maps.InfoWindow();
	var service = new google.maps.places.PlacesService(map);
	service.textSearch(request, callback);
	

}

function callback(results, status) {
	if (status === google.maps.places.PlacesServiceStatus.OK) {
		for (var i = 0; i < results.length; i++) {
			createMarker(results[i]);
		}
	}
}

function createMarker(place) {
	var placeLoc = place.geometry.location;
	var marker = new google.maps.Marker({
		map : map,
		position : place.geometry.location
	});

	google.maps.event.addListener(marker, 'click', function() {
		infowindow.setContent(place.name);
		infowindow.open(map, this);
	});
}