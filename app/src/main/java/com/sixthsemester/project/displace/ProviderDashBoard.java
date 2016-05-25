package com.sixthsemester.project.displace;


import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.sixthsemester.project.displace.jsonhandle.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;



public class ProviderDashBoard  extends AppCompatActivity {

        // This is the Adapter being used to display the list's data

   // private CharSequence mTitle;
   // String Email, Password;
    ArrayList<String> Brand=new ArrayList<String>();
    ArrayList<String> Outlet= new ArrayList<String>();
    ArrayList<String> Product = new ArrayList<String>();
    ArrayList<String> Price = new ArrayList<String>();
    ArrayList<String> view = new ArrayList<String>();
    public Bitmap[] img ;


    ProviderDasboardAdapter adapter;
    private MongoDB mongoDB;
    private ArrayList<String> arrayListUsernames;
    private ArrayList<Bitmap> arrayListBitmaps;
    private ArrayList<String> arrayListEncodedData;
    private RetrieveAsyncTask asynchronousReceiver;
    private ProgressDialog progressDialog;
    String Email;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.provider_dashboard);
            Intent intent = getIntent();
            Email = intent.getStringExtra("Email");
            img =  new Bitmap[20];
            RetrieveAsyncTask rat = new RetrieveAsyncTask();
            rat.execute();
          //  Toast.makeText(ProviderDashBoard.this, "in dashboard", Toast.LENGTH_SHORT).show();


           // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.rowlayout,R.id.list_content,Brand );


        }
    public void graph(View view){
        Intent intent = new Intent(ProviderDashBoard.this, ViewStatistics.class);
        intent.putExtra("Email",Email);
        startActivity(intent);


    }
    public void Ad(View view){
        Intent intent = new Intent(ProviderDashBoard.this,Ad_post.class);
        intent.putExtra("Email", Email);
        startActivity(intent);

    }
    public void Display(){

            DashboardDetails Dashboard_data[] = new DashboardDetails[]{
                    new DashboardDetails(Brand, Outlet, Product, Price,img)
            };


        adapter=new ProviderDasboardAdapter(this,Brand, Outlet, Product, Price,img,view);
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
     //   Toast.makeText(ProviderDashBoard.this, "Printing", Toast.LENGTH_SHORT).show();

    }
    public class RetrieveAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser=new JSONParser();
        private ProgressDialog pDialog;
        int myJson = 2;
        String encodedImage;

        /*private Object object[];
        private ArrayList<String> arrayListUsrnms;
        private ArrayList<Bitmap> arrayListBtmps;
        private ArrayList<String> arrayListEncddData;*/


        public RetrieveAsyncTask(){}

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
            pDialog = new ProgressDialog(ProviderDashBoard.this);
            pDialog.setMessage("Loading Fetching Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            mongoDB = MongoDB.getInstance();
            try {
                JSONArray jarr = getData();
                JSONArray add ;
                for(int i = 0;i<jarr.length();i++) {
                    JSONObject jsonobj = jarr.getJSONObject(i);
                    if (jsonobj.getString("email").equals(Email)) {
                        System.out.println("email found");
                        //Brand.add(jsonobj.getString("brand"));
                        myJson=1;

                        //System.out.println(" "+jsonobj.getString("brand"));
                        add = jsonobj.getJSONArray("Ad");
                        for(int j=0; j< add.length(); j++) {
                            Brand.add(jsonobj.getString("brand"));
                            JSONObject obj = add.getJSONObject(j);
                            Outlet.add(obj.getString("outlet_name"));
                            //System.out.println(" " + obj.getString("outlet_name"));
                            //   myJson=0;
                            Product.add(obj.getString("Product"));
                            Price.add(obj.getString("price"));
                            view.add(obj.getString("price"));


                            JSONArray js = jsonobj.getJSONArray("Image");
                            encodedImage = js.getString(0);
                            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            img[j] = decodedByte;
                            //break;
                        }

                       // Log.d("ad details", jsonobj.getString("Ad"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*try{while (true) {
                object = mongoDB.retrieveImages(arrayListEncodedData);
                arrayListUsrnms = (ArrayList<String>) object[0];
                arrayListBtmps = (ArrayList<Bitmap>) object[1];
                arrayListEncddData = (ArrayList<String>) object[2];

                System.out.println(arrayListUsrnms);
                System.out.println(arrayListBtmps);
                System.out.println(arrayListEncddData);

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
        }*/

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if(myJson==1) {
                Toast.makeText(ProviderDashBoard.this, "Data Retrieved Successfully", Toast.LENGTH_SHORT).show();

                Log.d("test", "1: " + Product.toString());

                Display();


            }
            else if (myJson==2){
                Toast.makeText(ProviderDashBoard.this, "No Ads Posted", Toast.LENGTH_SHORT).show();


            }
            else{
                Toast.makeText(ProviderDashBoard.this, "Failed Check Network", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Customer_Dashboard.this, customer_picks.class);
                // intent.putExtra("NameArray",ButtonName);
                //startActivity(intent);
            }
        }

        public JSONArray getData(){

            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);
        }
    }


}
