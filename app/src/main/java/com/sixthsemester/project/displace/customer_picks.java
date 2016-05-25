package com.sixthsemester.project.displace;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by Smile on 5/3/2016.
 */
public class customer_picks extends Activity implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    double size=0;

    private LocationRequest mLocationRequest;


    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    String Email, Password;
    public static Double lat, lon;
    public static String slat, slon;
    //    ArrayList<String> ButtonNamePass =  new ArrayList<String>();
    String ButtonNamePass1 = "";

    ArrayList<String> ButtonName =  new ArrayList<String>();
    String  distanceOptionValue;
    int k = 0;

    Button im2;
    public TableLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_picks);
        ll = (TableLayout)findViewById(R.id.ll);

        Intent intent = getIntent();
        ButtonName = intent.getStringArrayListExtra("NameArray");
        Email = intent.getStringExtra("Email");
        Password = intent.getStringExtra("Password");
        size = (double)ButtonName.size();
        createButton();
        buildGoogleApiClient();
    }

    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // mLocationRequest.setInterval(100); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            String lat2 = String.valueOf(mLastLocation.getLatitude());
            lat= Double.parseDouble(lat2);
            String lon2 = String.valueOf(mLastLocation.getLongitude());
            lon= Double.parseDouble(lon2);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }





    public void onLocationChanged(Location location) {
        String lat2 = String.valueOf(mLastLocation.getLatitude());
        lat= Double.parseDouble(lat2);
        String lon2 = String.valueOf(mLastLocation.getLongitude());
        lon= Double.parseDouble(lon2);
        updateUI();
    }
    public void updateUI(){
        slat="" +lat;
        slon= "" +lon;
        Toast.makeText(getApplicationContext(), "Location:" +lat+ lon, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mGoogleApiClient.disconnect();
    }



    public void createButton(){
        TableRow tr;
        // Toast.makeText(getApplicationContext(), "MADE", Toast.LENGTH_SHORT).show();
        for(int no=0; no< (int) Math.ceil(size/2); no++) {
            tr = new TableRow(this);

            for(int i=0; i< 2; i++)
            {
                if(k<size)
                {

                    ImageButton im = new ImageButton(this);
                    im.setBackgroundResource(getResources().getIdentifier(ButtonName.get(k), "drawable", getPackageName()));

                    //ButtonNamePass.add(ButtonName.get(k));

                    im.setImageResource(R.drawable.selector_prefrence);
                    im.setPadding(1,1,1,1);
                    im.setTag(ButtonName.get(k));
                    TableRow.LayoutParams r1 = new TableRow.LayoutParams(380,380);
//                    if (size == 5 && i ==0 && k== 3)
                    //                      r1.setMargins(150,150,10,10);
                    //                else
                    r1.setMargins(110,80,10,10);
                    im.setLayoutParams(r1);

                    im.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            view.setSelected(!view.isSelected());
                            //  ButtonNamePass.add("" +view.getTag());
                            ButtonNamePass1="" +view.getTag();

                            distanceOptions();


                        }



                    });
                    tr.addView(im);
                    k++;
                }
            }
            ll.addView(tr);

        }

    }
    public void distanceOptions(){
        Toast.makeText(getApplicationContext(), "Clicked button:" + ButtonNamePass1 , Toast.LENGTH_SHORT).show();
        final CharSequence option[] = new CharSequence[] {"1 km", "5 km", "10 km", "20 km" , "30 km"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Look for outlets in how much radius");
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intent = new Intent(customer_picks.this, Customer_Dashboard.class);
                intent.putExtra("Email",Email);
                intent.putExtra("Password", Password);
                intent.putExtra("Lat", lat);
                intent.putExtra("Lon", lon);
                intent.putExtra("preference",ButtonNamePass1);
                //   intent.putExtra("preference",ButtonNamePass.get(0));

                distanceOptionValue = ""+ option[which];
                distanceOptionValue= distanceOptionValue.replace("km","");
                distanceOptionValue= distanceOptionValue.trim();
                //     Toast.makeText(getApplicationContext(), "Stuff:" +distance + Email + Password, Toast.LENGTH_SHORT).show();

                intent.putExtra("DistanceOption",  distanceOptionValue);
                startActivity(intent);
            }
        });
        builder.show();
    }
    public void Continue_to_dashboard(View view){
        //InsertAsyncTask tsk = new InsertAsyncTask();
        //tsk.execute();

        Intent intent = new Intent(customer_picks.this, Customer_Dashboard.class);
        startActivity(intent);
    }
}
