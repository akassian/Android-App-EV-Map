package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

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
        photo_reference = intent.getStringExtra("photo_reference");
        if (!photo_reference.equals("") && photo_reference != null) {
            url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=700&photoreference="+photo_reference+ "&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA";
            Picasso.get().load(url).into(imageView);
        }







        //Picasso.get().load("https://res.cloudinary.com/akass1122/image/upload/v1568104436/murqngl0khkxcakbkwmd.png").into(imageView);
        //Picasso.get().load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=CnRtAAAATLZNl354RwP_9UKbQ_5Psy40texXePv4oAlgP4qNEkdIrkyse7rPXYGd9D_Uj1rVsQdWT4oRz4QrYAJNpFX7rzqqMlZw2h2E2y5IKMUZ7ouD_SlcHxYq1yL4KbKUv3qtWgTK0A6QbGh87GB3sscrHRIQiG2RrmU_jF4tENr9wGS_YxoUSSDrYjWmrNfeEHSGSc3FyhNLlBU&key=AIzaSyCf0eLTEerAe9pzbB-mFWLe_LifjQRhEoA").into(imageView);
    }

}
