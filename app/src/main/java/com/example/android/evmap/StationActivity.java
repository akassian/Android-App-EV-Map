package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.model.PhotoMetadata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class StationActivity extends AppCompatActivity {
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private LatLng curStationPosition;
    private ImageView photoView;
    private TextView infoTextView, nameTextView;
    private PlacesClient placesClient;
    private String place_name = "";
    private String vicinity = "";
    private  String rating = "none";
    private String ratingStr = "none";
    double lat = 37.398965;
    double lng = -122.110555;
    private String stationStr = "";
    private String infoStr = "";
    private String hours = "";
    //private String phone;
    private HashMap<String, String> placeHashMap;
    private  String photo_reference = "";
    //String refer = "CmRZAAAAR7vmVRuPVJ2lB9KE_FHkon_s0ocGU0DCZZW13KStKEbwNUs-GVwuGMX_SCyatIVlzolAn4nVGhLinG_NwfN1fLCirZEt2u0O7zawg02Wvd8Ro0fvsICA0ADMINyo0Yd6EhCHBgbMKtbkWT8dKNyJn9pqGhRFSvStQtiFTAnIHJt4sfmE4fp5pA";

    //String refer = "CmRaAAAADliBeIQ6sg1g4YXFJlcptBqVFp0oug3kJ_qdF7vwCZKIdiPXUPtPmOx7dOV_IVdEglCprMpjqpsd__gQl5bi8g8o83SegrJZ7fx0WNVK1VofPBO_YbzXIE-c0jZ327iHEhDZ0kzpUPJAGLXLCKtaUw0NGhTJtlNtIZ5i7-3Xoyq0XMvc9mzZEA";

    String refer = "CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station1);

        placesClient = Places.createClient(this);
        //photoView = (ImageView) findViewById(R.id.IV_station);
        infoTextView = (TextView) findViewById((R.id.TV2_info));
        nameTextView = (TextView) findViewById((R.id.TV1_station));

        Intent intent = getIntent();
        String place_id = intent.getStringExtra("place_id");
        //Log.d("Intent_start" , place_id);

        lat = intent.getDoubleExtra("lat", 37.398965);

        Log.d("STATION_LAT: ", "" + lat);


        lng = intent.getDoubleExtra("lng", -122.110555);
        Log.d("STATION_Lng: ", "" + lng);
        place_name = intent.getStringExtra("place_name");
        vicinity = intent.getStringExtra("vicinity");
        nameTextView.setText(place_name + "\n" + vicinity);
        stationStr = place_name + " StreetView" + "\nAddress: "+vicinity;
        rating = intent.getStringExtra("rating");
        double ratingNum = Double.parseDouble(rating);
        if (ratingNum > 0) {
            ratingStr = Double.toString(ratingNum);

        }

            curStationPosition = new LatLng(lat, lng);

            SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                    (SupportStreetViewPanoramaFragment)
                            getSupportFragmentManager().findFragmentById(R.id.street_view);
            streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                    new OnStreetViewPanoramaReadyCallback() {
                        @Override
                        public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                            // Only set the panorama to SYDNEY on startup (when no panoramas have been
                            // loaded which is when the savedInstanceState is null).
                            if (savedInstanceState == null) {
                                panorama.setPosition(curStationPosition);
                            }
                        }
                    });


            // GET PLACE DETAILS
            Object parameters[] = new Object[1];
            GetPlaceDetails getPlaceDetails = new GetPlaceDetails();
            String url = buildURLforPlaceDetailsRequest(place_id);
            parameters[0] = url;
            getPlaceDetails.execute(parameters); // SHOW PHONE, WEBSITE, OPENING HOURS


            //================================================================================
            // PHOTO

            String URL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA";

            String URL1 = "https://lh4.googleusercontent.com/-1wzlVdxiW14/USSFZnhNqxI/AAAAAAAABGw/YpdANqaoGh4/s1600-w400/Google%2BSydney";
            String URL2 = "https://res.cloudinary.com/akass1122/image/upload/v1568104436/murqngl0khkxcakbkwmd.png";
            //Picasso.get().load(URL2).into(photoView);

            Log.d("StationActivityGGGG", "GGG");
            //Picasso.get().load("https://res.cloudinary.com/akass1122/image/upload/v1568104436/murqngl0khkxcakbkwmd.png").resize(50, 50).into(photoView);

            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").resize(50, 50).into(photoView);


            //        Picasso.get()
//                .load(url)
//                .resize(50, 50)
//                .centerCrop()
//                .into(imageView)


//        Picasso.with(StationActivity)
////                .load(URL2)
////                .placeholder(R.drawable.thumbnail_placeholder)
////                .resize(width,height)
////                .into(imageView);


            PhotoMetadata photoMetadata = PhotoMetadata.builder(refer).build();
            FetchPhotoRequest.Builder photoRequestBuilder = FetchPhotoRequest.builder(photoMetadata);
            photoRequestBuilder.setMaxWidth(100);
            photoRequestBuilder.setMaxHeight(200);

            Task<FetchPhotoResponse> photoTask = placesClient.fetchPhoto(photoRequestBuilder.build());


            //===================================================
//SHOULD WORK BUT NOT

//        photoTask.addOnSuccessListener(
//                response -> {
//                    Log.d("Station_PhotoHHH", "HHH");
//                    photoView.setImageBitmap(response.getBitmap());
//
//                    //StringUtil.prepend(responseView, StringUtil.stringify(response.getBitmap()));
//                });


            //  =======================================================
////
//        photoTask.addOnFailureListener(
//                exception -> {
//                    exception.printStackTrace();
//                    //StringUtil.prepend(responseView, "Photo: " + exception.getMessage());
//                });


            //END OF PHOTO
            //=============================================================


        }




    //    https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJu9NecjO3j4AR9XmI7Qnc9Wk&fields=name,rating,formatted_phone_number,website,opening_hours,photos,price_level,icon&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA


    private String buildURLforPlaceDetailsRequest(String place_id)
    {

        StringBuilder googlePlaceDetailsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
        googlePlaceDetailsUrl.append("place_id="+place_id);
        googlePlaceDetailsUrl.append("&fields=name,rating,formatted_phone_number,website,opening_hours,photos,price_level,icon");
        googlePlaceDetailsUrl.append("&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA");

        //Log.d("MapsActivity_EVcharging", "url = "+googlePlaceUrl.toString());
        String str = googlePlaceDetailsUrl.toString();

        Log.d("STATION_URL", str );

        return str;
    }

    private final class GetPlaceDetails extends AsyncTask<Object, String, String> {
        private String placeDetailsData;
        //private GoogleMap mMap;
        String url;

        @Override
        protected String doInBackground(Object... objects) {
            //mMap = (GoogleMap) objects[0];
            url = (String) objects[0];

            LoadDataViaURL loadDataViaURL = new LoadDataViaURL();
            try {
                placeDetailsData = loadDataViaURL.loadDataString(url);
                Log.d("STATION_RESPONSE",placeDetailsData);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return placeDetailsData; // a string
        }

        @Override
        protected void onPostExecute(String s) { // SHOW PHONE, WEBSITE, OPENING HOURS
            Log.d("STATION_POST", s);
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            placeHashMap = makePlaceHashMap(jsonObject);

            //stationStr  = "Place: "+ place_name + "\nAddress: "+vicinity;

            infoStr = "\nPhone: "+(placeHashMap.get("phone")!=null?placeHashMap.get("phone"):"none") + "\nWebsite: "+ (placeHashMap.get("website")!=null?placeHashMap.get("website"):"none");
            hours = placeHashMap.get("mon") == null?"": "\n" +
                    "Open:\n"+ placeHashMap.get("mon")+"\n" + placeHashMap.get("tue")+"\n" + placeHashMap.get("wed")+"\n" +
                    placeHashMap.get("thur")+"\n" + placeHashMap.get("fri")+"\n" + placeHashMap.get("sat")+"\n" + placeHashMap.get("sun")+"\n";
            infoTextView.setText( stationStr+ infoStr + hours);

    }
    }
    //==============================

        private HashMap<String, String> makePlaceHashMap(JSONObject googlePlaceJson)

        {
            JSONObject result = null;

            Log.d("STATION_IN_MAKE_PLACE", "begin");

            HashMap<String, String> googlePlaceHashMap = new HashMap<>();
            String placeName = "--NA--";
            String vicinity = "--NA--";
            String latitude = "";
            String longitude = "";
            String reference = "";
            String phone = "";
            String website = "";
            String mon;
            String tue;
            String wed;
            String thur;
            String fri;
            String sat;
            String sun;

            JSONObject opening_hours = null;
            JSONArray weekday_text = null;
            JSONArray photos = null;

            Log.d("STATION_MID", "jsonobject =" + googlePlaceJson.toString()); // good json string

            try {
                result= googlePlaceJson.getJSONObject("result");
                //Log.d("STATION_RESULT: ", result.toString()); // good JSON string
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (!result.isNull("website")) {
                    website = result.getString("website");
                    googlePlaceHashMap.put("website", website);
                    //Log.d("STATION_WEBSITE: ", website);
                }
            } catch (JSONException e) {
                        e.printStackTrace();
            }
            if (!result.isNull("formatted_phone_number")) {
                //Log.d("STATION_MID","phone not null");
                try {

                    phone = result.getString("formatted_phone_number");
                    googlePlaceHashMap.put("phone", phone);
                    //Log.d("STATION_PHONE", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                //Log.d("STATION_MID","phone is null: ");
            }
            if (!result.isNull("photos")) {
                try {
                    photos = result.getJSONArray("photos");
                    if (photos != null && photos.length() > 0) {
                        try {
                            JSONObject photo = (JSONObject) photos.get(0);
                            if (!photo.isNull("photo_reference")) {
                                try {

                                    photo_reference = photo.getString("photo_reference");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (!result.isNull("opening_hours")){
                try {
                    opening_hours = result.getJSONObject("opening_hours");
                    Log.d("STATION_Opening: ",opening_hours.toString());
                    if (!opening_hours.isNull("weekday_text"))
                    {
                        try {
                            weekday_text = opening_hours.getJSONArray("weekday_text");

                            //weekday_text= c.getJSONArray("kl");
                            if(weekday_text!= null && weekday_text.length() == 7){
                                Log.d("STATION_weekday: ",weekday_text.toString());

                                mon = weekday_text.getString(0);
                                Log.d("STATION_MONDAY: ", mon);

                                tue = weekday_text.getString(1);
                                wed = weekday_text.getString(2);
                                thur = weekday_text.getString(3);
                                fri = weekday_text.getString(4);
                                sat = weekday_text.getString(5);
                                sun = weekday_text.getString(6);
                                googlePlaceHashMap.put("mon", mon);
                                googlePlaceHashMap.put("tue", tue);
                                googlePlaceHashMap.put("wed", wed);
                                googlePlaceHashMap.put("thur", thur);
                                googlePlaceHashMap.put("fri", fri);
                                googlePlaceHashMap.put("sat", sat);
                                googlePlaceHashMap.put("sun", sun);
                                Log.d("STATION_HASHMAP", googlePlaceHashMap.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                    }
                return googlePlaceHashMap;
            }
//    public void goToInfo(View v) {
//        //Intent intent;
//        //Log.d("STATION_Intent", "Starting");
//        Intent intent = new Intent(this, InfoActivity.class);
//
//        intent.putExtra("lat", lat);
//        intent.putExtra("lng", lng);
//        intent.putExtra("place_name", place_name);
//        intent.putExtra("vicinity", vicinity);
//        intent.putExtra("stationStr", stationStr);
//        intent.putExtra("ratingStr", ratingStr);
//
//
//        //Log.d("STATION_Intent", "Starting");
//        startActivity(intent);
//    }
    public void goToMorInfo(View v) {
        //Intent intent;
        //Log.d("STATION_Intent", "Starting");
        Intent intent = new Intent(this, MoreInfoActivity.class);

        intent.putExtra("lat", lat);
        intent.putExtra("lng", lng);
        intent.putExtra("place_name", place_name);
        intent.putExtra("vicinity", vicinity);
        intent.putExtra("stationStr", stationStr);
        intent.putExtra("ratingStr", ratingStr);
        intent.putExtra("photo_reference", photo_reference);
        Log.d("STATION_REFER", photo_reference);
        //Log.d("STATION_Intent", "Starting");
        startActivity(intent);
    }



}







