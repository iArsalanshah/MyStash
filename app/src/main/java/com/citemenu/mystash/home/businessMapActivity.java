package com.citemenu.mystash.home;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.citemenu.mystash.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class BusinessMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int ERROR_DIALOG_REQUEST = 9001;
    private SupportMapFragment mapFragment;
    private String lat, lng, businessName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_map);
        lat = getIntent().getStringExtra("lat");
        lng = getIntent().getStringExtra("lng");
        businessName = getIntent().getStringExtra("businessName");
        initialization();
    }

    private void initialization() {
        //SupportMapFragment
        mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.mapSearchBusinessClick);
        mapFragment.getMapAsync(this);
        findViewById(R.id.topbarImageBackMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView tvTile = (TextView) findViewById(R.id.tvBusinessnameToolbar);
        tvTile.setText(businessName);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(Double.valueOf(lat), Double.valueOf(lng)))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pointer_icon)));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), 12);
        googleMap.moveCamera(update);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private boolean servicesOK() {
        GoogleApiAvailability googleAPI =
                GoogleApiAvailability.getInstance();
        int isAvailable = googleAPI
                .isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else {
            if (googleAPI.isUserResolvableError(isAvailable)) {
                Dialog dialog = googleAPI.
                        getErrorDialog(this, ERROR_DIALOG_REQUEST, isAvailable);
                dialog.show();
            } else {
                Toast.makeText(BusinessMapActivity.this,
                        "Can't connect to mapping service",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
