package com.example.android.evmap;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.content.res.Resources;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;


//import android.location.LocationListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.google.android.libraries.places.api.Places;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import android.location.Address;
import android.content.res.Resources;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 50000;
    double latitude,longitude;
    double latFromAddress = -33.87365;
    double lngFromAddress = 151.20689;
    String ratingStr;
    String EVcharging = "EV+charging+stations";
    EditText tf_location;


    //==================
    Resources res;
    int[] batterySizeArray;
    int[] kWhPer100milesArray;

    int[] minutesChargeEmptyToFull;
    private double batteryStatus = 20;
    double timeToFullChargeInMinutes22kw = 240.0;

    private double distanceCanTravel = 50.0;

    int carIndex = 0;
    double batterySize;
    double kWhPer100miles;
    double maxDistance1;
    int maxDistance;
    double maxDistanceKm1;
    int maxDistanceKm;
    boolean miles;


    //=================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MapActivityAA", "AAAAAAA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps1);
        res = getResources();

        //===============================================
        minutesChargeEmptyToFull = res.getIntArray(R.array.minutesChargeEmptyToFull);
        carIndex = ((EVapplication) this.getApplication()).getCarIndex();

        //=================================

        Spinner spinner = (Spinner) findViewById(R.id.cars_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cars_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(((EVapplication) this.getApplication()).getCarIndex());

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                ((EVapplication) MapsActivity.this.getApplication()).setCarIndex(position);
                timeToFullChargeInMinutes22kw = 0.8*minutesChargeEmptyToFull[carIndex];

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        //=============================

        tf_location =  findViewById(R.id.TF_location);

        String apiKey = getString(R.string.google_maps_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void calculateDistance(View view) {
        Log.d("MAPS_distance", "battery" );
        //TextView chargingTimeView = (TextView) findViewById(R.id.TextView3);
        EditText batteryText = (EditText) findViewById(R.id.ET_charge);
        if (batteryText.getText() != null && !(batteryText.getText().toString().equals(""))) {
            String batteryStr = batteryText.getText().toString();
            try {
                batteryStatus = Double.parseDouble(batteryStr);
                if (0.0 <= batteryStatus && batteryStatus <= 100.0) {
                    distanceCanTravel = batteryStatus * maxDistanceKm/100;
                    //Log.d("MAPS_distance", batteryStr );
                } else {
                    Toast.makeText(this,"Battery status should be a number between 0 and 100." , Toast.LENGTH_LONG).show();

                }
            } catch (NumberFormatException e) {
                Toast.makeText(this,"Battery status should be a number between 0 and 100." , Toast.LENGTH_LONG).show();

            }
        }
    }
    public void timeToChangeToFullInMinutes (View view) {
        EditText batteryText = (EditText) findViewById(R.id.ET_charge);
        if (batteryText.getText() != null && !(batteryText.getText().toString().equals(""))) {
            String batteryStr = batteryText.getText().toString();
            try {
                batteryStatus = Double.parseDouble(batteryStr);
                if (0.0 <= batteryStatus && batteryStatus <= 100.0) {
                    ((EVapplication) MapsActivity.this.getApplication()).setBatteryCharge(batteryStatus);

                    distanceCanTravel = batteryStatus * maxDistanceKm/100;
                    timeToFullChargeInMinutes22kw = ((100.0 - batteryStatus)/100.0)*minutesChargeEmptyToFull[carIndex];

                    //Log.d("MAPS_distance", batteryStr );
                } else {
                    Toast.makeText(this,"Battery status should be a number between 0 and 100." , Toast.LENGTH_LONG).show();

                }
            } catch (NumberFormatException e) {
                Toast.makeText(this,"Battery status should be a number between 0 and 100." , Toast.LENGTH_LONG).show();

            }
        }
        //return timeToFullChargeInMinutes22kw;

    }


    private double distanceKMbetweenPoints(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


//========================


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longitude = location.getLongitude();
        lastlocation = location;
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }
        Log.d("lat = ",""+latitude);
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentLocationmMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    public void onClick(View v)
    {
        Object parameters[] = new Object[2];
        GetAndShowNearbyPlaces getAndShowNearbyPlaces = new GetAndShowNearbyPlaces();
        String url;

        switch(v.getId())
        {

            case R.id.B_main_EVcharging:
                mMap.clear();

                //String EVcharging = "EV+charging+stations";
                url = buildURLforBusinessSearch(latitude, longitude, EVcharging);
                parameters[0] = mMap;
                parameters[1] = url;
                getAndShowNearbyPlaces.execute(parameters);
                Toast.makeText(MapsActivity.this, "Showing Nearby EV charging", Toast.LENGTH_SHORT).show();
                break;
                case R.id.B_search: // GEOCODER: show location based on entered location
                mMap.clear();
                tf_location =  findViewById(R.id.TF_location);
                String location = tf_location.getText().toString();
                List<Address> addressList;
                if(!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if(addressList != null && addressList.size() >= 1)
                        {
                            latFromAddress = addressList.get(0).getLatitude();
                            lngFromAddress = addressList.get(0).getLongitude();
                            LatLng latLng = new LatLng(latFromAddress , lngFromAddress);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(location);
                                mMap.addMarker(markerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                        }
                        //=====================================================
                        url = buildURLforBusinessSearch(latFromAddress, lngFromAddress, EVcharging);
                        parameters[0] = mMap;
                        parameters[1] = url;

                        getAndShowNearbyPlaces.execute(parameters);
                        Toast.makeText(MapsActivity.this, "Showing EV charging stations near entered location", Toast.LENGTH_SHORT).show();
                       //======================================================
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private String buildURLforBusinessSearch(double latitude , double longitude , String searchStr)
    {
        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&keyword="+searchStr);
        //googlePlaceUrl.append("&opennow=true");
        googlePlaceUrl.append("&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA");
        Log.d("MapsActivity_EVcharging", "url = "+googlePlaceUrl.toString());
        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
    private final class GetAndShowNearbyPlaces extends AsyncTask<Object, String, String> {
        private String googlePlacesData;
        private GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... objects){
            mMap = (GoogleMap)objects[0];
            url = (String)objects[1];

            LoadDataViaURL loadDataViaURL = new LoadDataViaURL();
            try {
                googlePlacesData = loadDataViaURL.loadDataString(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googlePlacesData; // a string
        }

        @Override
        protected void onPostExecute(String s){

            List<HashMap<String, String>> nearbyPlaceList;
            JsonToHashMapParser parser = new JsonToHashMapParser();
            nearbyPlaceList = parser.parse(s);
            Log.d("NearbyPlacesList","Got List<HashMap>");
            showNearbyPlaces(nearbyPlaceList);
        }

        private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)


        {
            for(int i = 0; i < nearbyPlaceList.size(); i++)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                double lat = Double.parseDouble( googlePlace.get("lat"));
                double lng = Double.parseDouble( googlePlace.get("lng"));
                double rating = Double.parseDouble(googlePlace.get("rating"));
                String open_now_Str = googlePlace.get("open_now_Str");
                String place_id = googlePlace.get("place_id");
                //String reference = googlePlace.get("reference");
                LatLng latLng = new LatLng( lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : "+ vicinity);

                if (rating > 0) {
                    ratingStr = Double.toString(rating);
                } else {
                    ratingStr = "none";
                }
                markerOptions.snippet(open_now_Str+ " Rating: "+ ratingStr + ". Price per KW: $0.12. Waiting: 20 minutes.");
                if (rating >= 4.5) {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                } else {
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                Marker myMarker = mMap.addMarker(markerOptions);
                myMarker.setTag(googlePlace);

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));


                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        Log.d("InfoClick", "LatLong: "+ marker.getPosition() );
                        double lat = marker.getPosition().latitude;
                        double lng = marker.getPosition().longitude;
                        double batteryCharge = ((EVapplication) MapsActivity.this.getApplication()).getBatteryCharge();
                        int carIndex = ((EVapplication) MapsActivity.this.getApplication()).getCarIndex();
                        double minutes = timeToFullChargeInMinutes22kw = ((100.0 - batteryCharge)/100.0)*minutesChargeEmptyToFull[carIndex];
                        Log.d("CHARGE_", String.valueOf(minutes));

                        Intent intent = new Intent(MapsActivity.this, com.example.android.evmap.StationActivity.class);
                        HashMap<String, String> curPlaceHashMap = (HashMap<String, String>) marker.getTag();

                        //intent.putExtra("place_id", place_id);

                        intent.putExtra("vicinity", curPlaceHashMap.get("vicinity"));
                        intent.putExtra("place_id", curPlaceHashMap.get("place_id"));
                        // These lat, lng from Marker Tag
//                        intent.putExtra("lat", curPlaceHashMap.get("lat"));
//                        intent.putExtra("lng", curPlaceHashMap.get("lng"));
                        intent.putExtra("place_name", curPlaceHashMap.get("place_name"));

                        intent.putExtra("rating", curPlaceHashMap.get("rating"));

                        // These lat, lng from Marker position
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        intent.putExtra("minutes", minutes);
                        startActivity(intent);
                    }
                });


            }
        }
    }


}
