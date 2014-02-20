package com.example.fisegoogle;

import java.lang.reflect.Field;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewConfiguration;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import android.view.Menu;

public class MapActivity extends FragmentActivity {

	SupportMapFragment mapFragment;
	GoogleMap map;
	final String TAG = "myLogs";
	
	
	@Override
	 protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		getOverflowMenu();
		int statusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext()); 
		if (statusCode == ConnectionResult.SUCCESS) {                                    // Проверка GooglePlayService
			mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			map = mapFragment.getMap();
			if (map == null) {
			      finish();
			      return;
			}
			else {
				CameraPosition cameraPosition = new CameraPosition.Builder()
		        	.target(new LatLng(56.5, 85))                                     // Координаты точки города
		        	.zoom(11)                                                         // Приближение
		        	.build();
				CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
				map.animateCamera(cameraUpdate);
			}
			Initialization();
		}
	 }
	
	private void getOverflowMenu() {

	     try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void Initialization() {
		
    }
	
	public void onClickTest(View view) {
	    map.setMapType(GoogleMap.MAP_TYPE_HYBRID);                // Тип отображения карты
	  }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
