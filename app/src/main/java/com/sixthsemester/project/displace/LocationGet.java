package com.sixthsemester.project.displace;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by Smile on 5/14/2016.
 */
public class LocationGet extends Activity {
/* Reference Link: https://developers.google.com/maps/documentation/android-api/start */

    public String name;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    public static String longitudeVal;
    public static String City;
    public static String location2="Hey";
    public static double longitude, latitude;
    public String x;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     /*   try {
           location2= loc();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       location2= loc();

    }


    public String loc(){ //} throws  IOException{
        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
 // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);


        // Get latitude of the current location
        latitude = myLocation.getLatitude();

        // Get longitude of the current location
        longitude = myLocation.getLongitude();

        longitudeVal = String.valueOf(longitude);
        String latitudeVal = String.valueOf(latitude);
        location2=longitude + "," + latitude;
        Toast.makeText(getApplicationContext(), "Longitude,latitude : "+location2 , Toast.LENGTH_SHORT).show();

        return latitudeVal;
    }
}
