package com.example.fisegoogle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MapActivity extends FragmentActivity implements LocationListener{
	
	private GoogleMap map;
	PlacesList nearPlaces;
	PlaceDetails placeDetails;
    String placeName;
	double latPlaceMarker;
	double longPlaceMarker;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		Intent i = getIntent();
		nearPlaces = (PlacesList) i.getSerializableExtra("near_places"); 
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
		if (status != ConnectionResult.SUCCESS) {
			int requestCode = 10;
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
			dialog.show();
		}
		else {
			// Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
            // Getting GoogleMap object from the fragment
            map = fm.getMap();
 
            // Enabling MyLocation Layer of Google Map
            map.setMyLocationEnabled(true);
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location != null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
		}
	}
	
	@Override
    public void onLocationChanged(Location location) {
 
        // Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
       
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
 
        // Showing the current location in Google Map
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        // Setting latitude and longitude in the TextView tv_location
        //tvLocation.setText("Широта:" +  latitude  + ", Долгота:"+ longitude );
        
        // Маркер местоположения
        map.addMarker(new MarkerOptions()
        .position(new LatLng(latitude, longitude))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .title("Вы здесь"));
        
        // Маркеры мест
        if (nearPlaces != null) {
        	for (Place place : nearPlaces.results) {
        		placeName = place.name.toString();
        		latPlaceMarker = place.geometry.location.lat;
        		longPlaceMarker = place.geometry.location.lng;
        		map.addMarker(new MarkerOptions()
        		.position(new LatLng(latPlaceMarker, longPlaceMarker))
        		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        		.title(placeName));
        	}
        
        }
        
        
    }
	@Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
