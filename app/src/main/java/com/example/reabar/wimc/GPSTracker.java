package com.example.reabar.wimc;

import android.app.AlertDialog;
import android.app.Service;
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

/**
 * Created by tomeraronovsky on 8/9/16.
 */
public class GPSTracker extends Service implements LocationListener {

    private final  Context context;

    Location location;
    protected LocationManager locationManager;
    boolean enabled  = false;
    boolean getLocation = false;
    boolean network = false;
    double latitude, longitude;

    public GPSTracker(Context context){
        this.context = context;
        getLocation();
    }

    public Location getLocation(){
        try {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if(!enabled && !network){

            }
            else {
                getLocation = true;
                if(network){
                    try{
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1000*60*1, this);
                    }catch (SecurityException e){ }

                }
                if(locationManager != null) {
                    try {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }catch (SecurityException e){ }
                }
            }

            if(enabled){
                if(location == null)
                {
                    try {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1000*60*1, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                    catch (SecurityException e){ }
                }
            }
        }
        catch (Exception e){ }

        return location;
    }

    public void stopGPS(){
        if(locationManager != null){
            try {
                locationManager.removeUpdates(GPSTracker.this);
            }
            catch (SecurityException e){ }
        }
    }

    public double getLatitude(){
        if(location != null)
            latitude = location.getLatitude();
        return  latitude;
    }

    public double getLongitude(){
        if(location != null)
            longitude = location.getLongitude();
        return longitude;
    }

    public boolean canGetLocation(){
        return this.getLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Settings");
        alertDialog.setMessage("Please Enabled GPS To Save Your Location");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        alertDialog.show();
    }
    @Override
    public void onLocationChanged(Location location) {

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
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
