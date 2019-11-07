package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class InfoActivity extends AppCompatActivity {
    double lat, lng;
    String stationStr = "";
    String ratingStr = "";
    String infoStr = "";
    String extraInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Intent intent = getIntent();
        lat = intent.getDoubleExtra("lat", -33.87365);
        lng = intent.getDoubleExtra("lng", -33.87365);
        ratingStr = intent.getStringExtra("ratingStr");

        String stationStr = intent.getStringExtra("stationStr");
        TextView stationName = (TextView) findViewById((R.id.TV1_info));
        TextView infoText = (TextView) findViewById((R.id.TV2_info));

        stationName.setText(stationStr);
        infoStr = stationStr + "\nRating: " + ratingStr;
        extraInfo = "\nCurrent waiting time: 20 minutes.\nCharging duration: 30 minutes.\nAmenities: Food and drinks, nearby supermarket. ";
        infoText.setText(infoStr + extraInfo);
        SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                (SupportStreetViewPanoramaFragment)
                        getSupportFragmentManager().findFragmentById(R.id.street_view2);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(
                new OnStreetViewPanoramaReadyCallback() {
                    @Override
                    public void onStreetViewPanoramaReady(StreetViewPanorama panorama) {
                        // Only set the panorama to SYDNEY on startup (when no panoramas have been
                        // loaded which is when the savedInstanceState is null).
                        if (savedInstanceState == null) {
                            panorama.setPosition(new LatLng(lat, lng));
                        }
                    }
                });

    }
}
