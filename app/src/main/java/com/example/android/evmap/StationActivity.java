package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.PhotoMetadata;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class StationActivity extends AppCompatActivity {
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private ImageView photoView;
    private TextView infoTextView;
    private PlacesClient placesClient;
    //private String phone;
    private HashMap<String, String> placeHashMap;
    //String refer = "CmRZAAAAR7vmVRuPVJ2lB9KE_FHkon_s0ocGU0DCZZW13KStKEbwNUs-GVwuGMX_SCyatIVlzolAn4nVGhLinG_NwfN1fLCirZEt2u0O7zawg02Wvd8Ro0fvsICA0ADMINyo0Yd6EhCHBgbMKtbkWT8dKNyJn9pqGhRFSvStQtiFTAnIHJt4sfmE4fp5pA";

    //String refer = "CmRaAAAADliBeIQ6sg1g4YXFJlcptBqVFp0oug3kJ_qdF7vwCZKIdiPXUPtPmOx7dOV_IVdEglCprMpjqpsd__gQl5bi8g8o83SegrJZ7fx0WNVK1VofPBO_YbzXIE-c0jZ327iHEhDZ0kzpUPJAGLXLCKtaUw0NGhTJtlNtIZ5i7-3Xoyq0XMvc9mzZEA";

    String refer = "CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);


        Log.d("StationActivityAA", "AAAAAAA");
        placesClient = Places.createClient(this);
        photoView = (ImageView) findViewById(R.id.IV_station);
        infoTextView = (TextView) findViewById((R.id.TV2_station));
        Log.d("StationActivityAA", "A2");
        //infoTextView.setText("HELLO");


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
                            panorama.setPosition(SYDNEY);
                        }
                    }
                });

        Intent intent = getIntent();
        String place_id = intent.getStringExtra("place_id");
        Log.d("Intent_start" , place_id);
        //infoTextView.setText("Place Id: "+ place_id);

        double lat = intent.getDoubleExtra("lat", -33.87365);
        double lng = intent.getDoubleExtra("lng", 151.20689 );
        String place_name = intent.getStringExtra("place_name");
        String vicinity = intent.getStringExtra("vicinity");
        infoTextView.setText("Place Id: "+ place_id+ " lat: "+lat+ " lng: "+lng + "\n Place: "+ place_name + " Address: "+vicinity);

        // GET PLACE DETAILS
        Object dataTransfer[] = new Object[1];
        GetPlaceDetails getPlaceDetails = new GetPlaceDetails();
        String url = buildURLforPlaceDetailsRequest(place_id);
        dataTransfer[0] = url;
        getPlaceDetails.execute(dataTransfer);











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
                placeDetailsData = loadDataViaURL.readUrl(url);
                Log.d("STATION_RESPONSE",placeDetailsData);

            } catch (IOException e) {
                e.printStackTrace();
            }

            return placeDetailsData; // a string
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("STATION_POST", s);
            JSONObject jsonObject = null;

//            List<HashMap<String, String>> nearbyPlaceList;
            try {
                jsonObject = new JSONObject(s);
//                jsonArray = jsonObject.getJSONArray("results");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //String phone1 = jsonObject.getString("formatted_phone_number");


            placeHashMap = makePlaceHashMap(jsonObject);
            //Log.d("STATION_JSON: ", placeHashMap.get("phone"));


            infoTextView.setText("Phone: "+placeHashMap.get("phone"));


        }
    }

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

            Log.d("STATION_MID", "jsonobject =" + googlePlaceJson.toString()); // good json string

            try {
                result= googlePlaceJson.getJSONObject("result")
                Log.d("STATION_RESULT: ", result.toString()); // good JSON string
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (!result.isNull("website")) {
                    website = result.getString("website");
                    googlePlaceHashMap.put("website", website);
                    Log.d("STATION_WEBSITE: ", website);
                }
            } catch (JSONException e) {
                        e.printStackTrace();
            }
            if (!result.isNull("formatted_phone_number")) {
                Log.d("STATION_MID","phone not null");
                try {

                    phone = result.getString("formatted_phone_number");
                    googlePlaceHashMap.put("phone", phone);
                    Log.d("STATION_PHONE", phone);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.d("STATION_MID","phone is null: ");
            }
            if (!result.isNull("opening_hours")){
                //Log.d("STATION_MID","phone not null");

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
        }







