package com.example.fisegoogle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);

		
		Intent i = getIntent();
		
		// Place referece id
		String reference = i.getStringExtra(KEY_REFERENCE);
		
		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
	}
	
	// Distance from current location to place
	private double calculateDistance(double fromLong, double fromLat,
    double toLong, double toLat) {
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Загрузка информации ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}


		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place details status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								
								// Place location
								double lat2 = placeDetails.result.geometry.location.lat;
								double lng2 = placeDetails.result.geometry.location.lng;
								
								// Current location
								Criteria criteria = new Criteria();
								LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
								String provider = locationManager.getBestProvider(criteria, true);
								Location location = locationManager.getLastKnownLocation(provider);
								double lat1 = location.getLatitude();
							    double long1 = location.getLongitude();
								
							    // Calculate distance
								double dist = calculateDistance(lat1,long1,lat2,lng2);
				
								Log.d("Place ", name + address + phone);
								
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);
								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Отсутствует" : name; // if name is null display as "Not present"
								address = address == null ? "Отсутствует" : address;
								phone = phone == null ? "Отсутствует" : phone;
								
								// Output information
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html.fromHtml("<b>Телефон:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>До места:</b> " + dist + " м"));
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(SinglePlaceActivity.this, "Ближайшие места",
									"Извините, места не найдены",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
									"Извините, неизвестная ошибка",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
									"Извините, лимит запросов исчерпан",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
									"Извините, произошла ошибка",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
									"Вы ввели неверный запрос. Попробуйте снова",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
									"Извините, произошла ошибка",
									false);
						}
					}else{
						alert.showAlertDialog(SinglePlaceActivity.this, "Ошибка",
								"Извините, произошла ошибка",
								false);
					}
					
					
				}
			});

		}

	}

}

