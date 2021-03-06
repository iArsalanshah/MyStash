package com.citemenu.mystash.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.citemenu.mystash.R;

/**
 * Created by Arsalan on 1/21/2016.
 */
public class Tracker implements LocationListener {
    private static boolean shouldStopGPS = false;
    Location location;
    boolean isGpsEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    double lat, lng;
    LocationManager locationManager;
    private Context mContext;

    public Tracker(Context context) {
        this.mContext = context;
        accessLocation();
    }

    public Tracker(){
    }

    private Location accessLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGpsEnabled) {
                Toast.makeText(mContext, mContext.getString(R.string.enable_gps), Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                    }
                }
                if (isGpsEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lng = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void stopGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(Tracker.this);
        }
    }

    public double getLat() {
        if (location != null) {
            lat = location.getLatitude();
        }
        return lat;
    }

    public double getLng() {
        if (location != null) {
            lng = location.getLongitude();
        }
        return lng;
    }

    public boolean isCanGetLocation() {
        return this.canGetLocation;
    }

    public void settingGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(mContext.getString(R.string.gps_settings));
        builder.setMessage(mContext.getString(R.string.enable_gps_message));
        builder.setPositiveButton(mContext.getString(R.string.setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    private void settingGPS(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(mContext.getString(R.string.gps_settings));
        builder.setMessage(mContext.getString(R.string.enable_gps_message));
        builder.setPositiveButton(mContext.getString(R.string.setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isGpsEnabled(Context context){
        return ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean checkGPS(Context context){
        if (isGpsEnabled(context)){
            return true;
        }else {
            settingGPS(context);
            return false;
        }
    }
}