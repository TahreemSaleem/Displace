package com.sixthsemester.project.displace;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;



public class Ad_post extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    String outName, product, pprice;
    int price;

    Location myLocation;
    private Switch mySwitch;
    private EditText outlet, Price, Product;
    String outlet_s, Product_s, discount, linkWeb = "";
    public static String category = "";
    public static String Email;
    // double lat,lng,price_i;
    public String name;


    public static String longitudeVal;
    public static String location2 = "Hey";
    double resultLocation[];
    public static double longitude, latitude;
    public String x;
    Button CategoryButton;
    private LocationRequest mLocationRequest;


    private GoogleApiClient mGoogleApiClient;
    TextView txtOutputLat, txtOutputLon;
    Location mLastLocation;

    Double lat =0.0, lon = 0.0;
    protected static final int CHOOSE_FILE_RESULT_CODE = 20;

    private String to = "", from = "";
    private MongoDB mongoDB;
    private Intent imageIntent;

    private CheckBox cbSaveChat;
    private ListView lvImageMessages;
    private Button btSendImage;

    private ArrayList<String> arrayListUsernames;
    private ArrayList<Bitmap> arrayListBitmaps;
    private ArrayList<String> arrayListEncodedData;
    // private CustomAdapter arrayAdapter;

    private AsynchronousReceiver asynchronousReceiver;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_post);


        outlet = (EditText) findViewById(R.id.Out_name);
        mySwitch = (Switch) findViewById(R.id.mySwitch);
        Price = (EditText) findViewById(R.id.Price);
        Product = (EditText) findViewById(R.id.Product_Name);
        Intent intent = getIntent();
        Email = intent.getStringExtra("Email");

        CategoryButton = (Button) findViewById(R.id.catergory);
        //set the switch to ON		        //set the switch to ON
        mySwitch.setChecked(true);
        mySwitch.setChecked(true);
        linkWeb = "www.google.com";
        //attach a listener to check for changes in state
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    discount = "yes";
                } else {
                    discount = "No";
                }

            }
        });
        try {
            initViewsAndVars();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        buildGoogleApiClient();
    }

    public void CreateDialog(View v) {
        category myCategory = new category();
        myCategory.show(getFragmentManager(), "myCategory");
        Toast.makeText(Ad_post.this, category, Toast.LENGTH_SHORT).show();
        category = myCategory.selection;
        CategoryButton.setText(category);

    }

    public void location(View view) throws IOException {

        Log.d("Test", "1");
        //  mGoogleApiClient.connect();
        //  Toast.makeText(getApplicationContext(), "Final" +lat+ ","+ lon , Toast.LENGTH_SHORT).show();

        //  resultLocation=loc();
        Log.d("Test", "2");

        //   Toast.makeText(getApplicationContext(), "NOW"+resultLocation[1] , Toast.LENGTH_SHORT).show();

        //postAd(view);

    }



    public void picture(View view) {

        System.out.println("Converting Image");
        startActivityForResult(imageIntent, CHOOSE_FILE_RESULT_CODE);
 }

    public void postAd(View view) {

        pprice = Price.getText().toString();
        pprice = pprice.trim();
        try {

            price = Integer.parseInt(pprice);
        } catch (Exception e) {
        }
        product = Product.getText().toString();
        product = product.trim();
        outName = outlet.getText().toString();
        outName = outName.trim();
        if ((!(pprice.isEmpty()) & !(product.isEmpty()) & !(outName.isEmpty()))) {

            post();
        }

        //  Toast.makeText(getApplicationContext(), "Posted" , Toast.LENGTH_SHORT).show();

        else {
            if (((pprice.isEmpty()) | (product.isEmpty()) | (outName.isEmpty()))) {
                Toast.makeText(getApplicationContext(), "Empty and invalid", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void link(View view) {
        //dummy
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter the website of your company");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                linkWeb = input.getText().toString();
                if (linkWeb.isEmpty()) {
                    //its ok if link is empty
                    linkWeb = "Does not have a website";
                    //    Toast.makeText(getApplicationContext(), "Link:" + linkWeb, Toast.LENGTH_SHORT).show();

                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void post() {
        outlet_s = outlet.getText().toString();

        Product_s = Product.getText().toString();
        //     Toast.makeText(Ad_post.this, outlet_s+  " "+outlet_s+pprice+Product_s+discount+linkWeb+category +lat+ lon, Toast.LENGTH_LONG).show();
        InsertAsyncTask vat = new InsertAsyncTask(outlet_s, pprice, Product_s, discount, linkWeb, category, lat, lon);
        //here!
        vat.execute();
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
        //  updateUI();
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
    public class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser = new JSONParser();
        //dummy
        int views = 0;
        private ProgressDialog pDialog;
        JSONObject myJson = null;
        String outlet, product, discount, link, category, price;
        double lat, lng;

        public InsertAsyncTask(String outlet, String price, String product, String discount, String link, String category, double lat, double lng) {
            this.outlet = outlet;
            this.product = product;
            this.price = price;
            this.lat = lat;
            this.lng = lng;
            this.discount = discount;
            this.link = link;
            this.category = category;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Ad_post.this);
            pDialog.setMessage("Posting Ad");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                JSONArray jarr = getData();
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject jsonobj = jarr.getJSONObject(i);
                    if (jsonobj.getString("email").equals(Email)) {

                            JSONArray arr_obj = new JSONArray();
                        JSONObject Ad = new JSONObject();
                        JSONObject location = new JSONObject();
                        location.put("latitude", lat);
                        location.put("longitude", lng);
                        Ad.put("Location", location);
                        Ad.put("outlet_name", outlet);
                        Ad.put("Product", product);
                        Ad.put("price", price);
                        Ad.put("discount", discount);
                        Ad.put("link", linkWeb);
                        Ad.put("Views", views);
                        Ad.put("Category", category);
                        //arr_obj.put(Ad);
                        if (jsonobj.getJSONArray("Ad").length()<0)

                            jsonobj.getJSONArray("Ad").put(Ad);
                        else {
                            arr_obj.put(Ad);
                            jsonobj.put("Ad", arr_obj);
                        }
                        myJson = jparser.makeHttpPOSTRequest(Constants.url + Constants.API_KEY, jsonobj);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Ad_post.this, "Failed Check Network", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if (myJson != null) {
                Toast.makeText(Ad_post.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
               Intent intent = new Intent( Ad_post.this, ProviderDashBoard.class);
                intent.putExtra("Email",Email);
                startActivity(intent);
                //back to provider dashboard
            } else {
                Toast.makeText(Ad_post.this, "Failed Check Network", Toast.LENGTH_SHORT).show();
            }
        }

        public JSONArray getData() {

            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);

        }
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            cursor.close();
            //ImageView imageView = (ImageView) findViewById(R.id.imgView);
            //imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            Context context = getApplicationContext();
            CharSequence text = picturePath;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent intent = new Intent(Ad_post.this, SaveImage.class);
            intent.putExtra("Email",Email);
            startActivity(intent);
        }
    }
*/

    public static final String TAG = "test";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static double currentLatitude = 33.6424001;
    public static double currentLongitude = 72.990529;

    //private Button connectBtn;


    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    private void initViewsAndVars() throws UnknownHostException {


        // cbSaveChat = (CheckBox) findViewById(R.id.cbSaveChat);
        //lvImageMessages = (ListView) findViewById(R.id.lvChatMessages);
        //btSendImage = (Button) findViewById(R.id.btSendImage);

        arrayListUsernames = new ArrayList<String>();
        arrayListBitmaps = new ArrayList<Bitmap>();
        arrayListEncodedData = new ArrayList<String>();
        // arrayAdapter = new CustomAdapter(this, arrayListUsernames, arrayListBitmaps);
        // lvImageMessages.setAdapter(arrayAdapter);

        imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE_RESULT_CODE) {
                Uri selectedImageUri = data.getData();

                try {

                    Bitmap imageBMP = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                    // This means image is JPG and is compressed and the compressed data is being written
                    // to byteArrayOutputStream. 100 means max quality. 0 means smallest possible size for image
                    imageBMP.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                    // First argument is the byte array. Second argument is the compression type.
                    // This is not the same compression we're talking about as in the previous command.
                    // This compression is related to conversion of byte data into Base64 string.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        new AsynchronousSender().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                                Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
                    } else {
                        new AsynchronousSender().execute(Base64.encodeToString(byteArrayOutputStream.toByteArray(),
                                Base64.DEFAULT));
                    }
                } catch (Exception e) {
                    Log.d("HAHAHA", "Exception: " + e); // :D :3
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(asynchronousReceiver.getStatus() != AsyncTask.Status.FINISHED) {
            asynchronousReceiver.cancel(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AsynchronousDeleter().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, to, from);
        } else {
            new AsynchronousDeleter().execute(to, from);
        }
        this.finish();
    }

    private class AsynchronousSender extends AsyncTask<String, Void, Void> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(Ad_post.this);
            pDialog.setMessage("Loading Fetching Data");
            System.out.println("Uploading image");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {

                mongoDB = MongoDB.getInstance();

            mongoDB.insertImage(params[0],Email);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();

            }
            Context context = getApplicationContext();
            CharSequence text = "file uploaded";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    private class AsynchronousReceiver extends AsyncTask<Void, Void, Void> {

        private Object object[];
        private ArrayList<String> arrayListUsrnms;
        private ArrayList<Bitmap> arrayListBtmps;
        private ArrayList<String> arrayListEncddData;

        @Override @SuppressWarnings("unchecked")
        protected Void doInBackground(Void... params) {
            try {
                while (true) {
                    object = mongoDB.retrieveImages( arrayListEncodedData);
                    arrayListUsrnms = (ArrayList<String>) object[0];
                    arrayListBtmps = (ArrayList<Bitmap>) object[1];
                    arrayListEncddData = (ArrayList<String>) object[2];

                    if (arrayListEncddData.size() > 0) {
                        arrayListUsernames.addAll(arrayListUsrnms);
                        arrayListBitmaps.addAll(arrayListBtmps);
                        arrayListEncodedData.addAll(arrayListEncddData);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // arrayAdapter.notifyDataSetChanged();
                            //lvImageMessages.setSelection(arrayAdapter.getCount() - 1);
                        }
                    });
                    arrayListUsrnms.clear();
                    arrayListBtmps.clear();
                    arrayListEncddData.clear();
                    if(progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Thread.sleep(3000);
                }
            } catch (InterruptedException e) {
                Log.d(getClass().getSimpleName(), "Exception thrown: " + e);
            }
            return null;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(Ad_post.this, "Exiting image share...", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsynchronousDeleter extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            mongoDB.deleteUnSavedImages(params[0], params[1]);
            return null;
        }
    }
}


