package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MoreInfoActivity extends AppCompatActivity {
    ImageView imageView;


    double lat, lng;
    String stationStr = "";
    String ratingStr = "";
    String infoStr = "";
    String extraInfo = "";
    String place_name = "";
    String vicinity = "";
    String photo_reference = "";
    String url = "";
    String opening_status = "";
    String open_now_Str = "";
    String openingInfo = "";
//     intent.putExtra("opening_status", opening_status);
//        intent.putExtra("open_now", open_now_Str);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        imageView = (ImageView) findViewById(R.id.image1);


        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", 37.398965);
        lng = intent.getDoubleExtra("lng", -122.110555);
        ratingStr = intent.getStringExtra("ratingStr");
        place_name = intent.getStringExtra("place_name");
        vicinity = intent.getStringExtra("vicinity");
        opening_status = intent.getStringExtra("opening_status");
        Log.d("MORE_", opening_status);
        open_now_Str = intent.getStringExtra("open_now");

        photo_reference = intent.getStringExtra("photo_reference");
        if (!photo_reference.equals("") && photo_reference != null) {
            url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=700&photoreference="+photo_reference+ "&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA";
            Picasso.get().load(url).into(imageView);
        }

        String stationStr = intent.getStringExtra("stationStr");
        TextView stationName = (TextView) findViewById((R.id.TV1_more_info));
        TextView infoText = (TextView) findViewById((R.id.TV2_more_info));

        stationName.setText(place_name+"\n"+vicinity);
        infoStr = place_name + " Station View" + "\n\n" + vicinity + "\nRating: " + ratingStr;
        openingInfo = "\n"+ opening_status + "\n" + open_now_Str;
        extraInfo = "\nCurrent waiting time: 20 minutes.\nCharging duration: 30 minutes.\nAmenities: Food and drinks, nearby supermarket. ";
        infoText.setText(infoStr + openingInfo + extraInfo);


        //Picasso.get().load("https://res.cloudinary.com/akass1122/image/upload/v1568104436/murqngl0khkxcakbkwmd.png").into(imageView);
        //Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA").into(imageView);
    }

}
