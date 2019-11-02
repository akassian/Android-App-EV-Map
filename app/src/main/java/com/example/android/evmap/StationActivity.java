package com.example.android.evmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class StationActivity extends AppCompatActivity {
    private static final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        Log.d("StationActivity", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
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

    }
}
