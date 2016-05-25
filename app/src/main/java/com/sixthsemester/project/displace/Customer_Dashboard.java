package com.sixthsemester.project.displace;


import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hira on 5/12/2016.
 */
public class Customer_Dashboard extends AppCompatActivity {

    float results[] = new float[1];
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private RecyclerView recyclerView;
    float Kilometers;

    private List<ItemObjects> gaggeredList;
    private  SolventRecyclerViewAdapter rcAdapter;

    private CharSequence mTitle;
    String Email, Password,CustomerDistanceValue;
    public static  ArrayList<String> Brand = new ArrayList<String>();
    ArrayList<String> Ad= new ArrayList<String>();
    ArrayList<String> Product = new ArrayList<String>();
    ArrayList<String> Price = new ArrayList<String>();
    ArrayList<String> Category = new ArrayList<String>();
    ArrayList<String> Outlet = new ArrayList<String>();
    ArrayList<String> Location1 = new ArrayList<String>();

    public static int ad_Size=0;
    //  String ButtonNamePass;
    String ButtonOption;
    Double sllat,slon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_dashboard);

        Intent intent = getIntent();
        Email = intent.getStringExtra("Email");
        Password = intent.getStringExtra("Password");
        Intent intent1 =  getIntent();
        // String st= intent1.getStringExtra("Lon");
        slon= intent1.getDoubleExtra("Lon",0.0);
        sllat= intent1.getDoubleExtra("Lat", 0.0);
        ButtonOption= intent1.getStringExtra("preference");

        CustomerDistanceValue = intent.getStringExtra("DistanceOption");
        Kilometers=Float.parseFloat(CustomerDistanceValue);
        //   Toast.makeText(getApplicationContext(), " Button bla bla: " + ButtonOption, Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);//RecyclerView component already created
        recyclerView.setHasFixedSize(true);
        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);  //sets a grid of 2,1
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        callData();






      /*  mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));*/

    }





    public void onSectionAttached(int number) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.customer_dashboard, container, false);
            return rootView;
        }


    }





    public void callData(){
        InsertAsyncTask tsk = new InsertAsyncTask(Email,Password);
        tsk.execute();
    }



    public String mapping(){
        String ButtonC="";
        if (ButtonOption.equals("shoe")){
            ButtonC="Shoes";
        }
        if (ButtonOption.equals("food")){
            ButtonC="Food";
        }
        if (ButtonOption.equals("drug")){
            ButtonC="Pharmacy";
        }
        if (ButtonOption.equals("beauty")){
            ButtonC="Cosmetics";
        }
        if (ButtonOption.equals("books")){
            ButtonC="Books";
        }
        if (ButtonOption.equals("electronic")){
            ButtonC="Electronics";
        }
        if (ButtonOption.equals("microwave")){
            ButtonC="Home Appliances";
        }
        if (ButtonOption.equals("ring")){
            ButtonC="Jewelery";
        }
        if (ButtonOption.equals("shirt")){
            ButtonC="Clothing";
        }
        if (ButtonOption.equals("shoe")){
            ButtonC="Shoes";
        }
        if (ButtonOption.equals("sports")){
            ButtonC="Sports";
        }

        return ButtonC;
    }
    public class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser=new JSONParser();
        ArrayList<String> ButtonName =  new ArrayList<String>();
        private ProgressDialog pDialog;
        JSONObject myJson = null;
        String Email, Pass;
        String bc;
        //float result[]=new float[10];
        ArrayList<Float> result = new ArrayList<Float>();
        public InsertAsyncTask(String Email, String Pass){
            this.Email = Email;
            this.Pass= Pass;
            bc=mapping();

            //  Toast.makeText(getApplicationContext(), "ButtonOption"+ButtonOption, Toast.LENGTH_LONG).show();
            //Log.d("Test1ing", "0");


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Customer_Dashboard.this);
            pDialog.setMessage("getting Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                Log.d("name is ","Hira");
                JSONArray jarr = getData();

                int ad_no_eachbrand=0;   // size of array.. no of ads(objects) in one brand

                JSONArray ad = new JSONArray();
                JSONObject jsonobj;


                int m = 0;

                for(int i = 0;i<jarr.length();i++) {
                    Double platitude,plongitude;
                    boolean flag;

                    jsonobj = jarr.getJSONObject(i);
                    if (jsonobj.getString("type").equals("provider")) {

                        ad = jsonobj.getJSONArray("Ad");
                        ad_no_eachbrand= ad.length();
                        JSONObject ad_object;
                        JSONObject loc;
                        ad_Size = ad_Size + ad_no_eachbrand;
                        for(int j=0; j< ad_no_eachbrand; j++) {
                            ad_object = ad.getJSONObject(j);

                            Location1.add(ad_object.getString("Location"));
                            loc = ad_object.getJSONObject("Location");

                            platitude= loc.getDouble("latitude");
                            plongitude= loc.getDouble("longitude");


                            Location.distanceBetween(platitude,plongitude,33.642546, 72.990495,results);
                            results[0]= results[0]/1000;


                            if ((results[0] <= Kilometers) & (results[0] != 0.0)) {
                                result.add(distance_call(platitude, plongitude));
                                Log.d("Test","Thats it");


                                if(ad_object.getString("Category").equals(bc)) {
                                    Category.add(ad_object.getString("Category"));
                                    Product.add(ad_object.getString("Product"));
                                    Log.d("Test","Categroy"+bc);
                                    Price.add(ad_object.getString("price"));
                                    Outlet.add(ad_object.getString("outlet_name"));
                                    Brand.add(jsonobj.getString("brand"));
                                }
                            }
                            m++;

                        }

                        // for( int k=0; k< ad_no_eachbrand; k++){
                        //    Brand.add(jsonobj.getString("brand"));
                        //}

                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if(result.size()>0){
                //for(int i=0; i< Brand.size(); i++) {
                callLists(Category.size());

                for (int i = 0; i < Category.size(); i++) {
                    //      Toast.makeText(Customer_Dashboard.this, "No of shops in vicinity" + i + "with " + result, Toast.LENGTH_SHORT).show();


                }
                //Toast.makeText(Customer_Dashboard.this, "Button name" + bc , Toast.LENGTH_SHORT).show();
            }
            else
            {
                //Toast.makeText(Customer_Dashboard.this, "jar is empty" , Toast.LENGTH_SHORT).show();
            }

        }
        public JSONArray getData(){
            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);

        }

        private List<ItemObjects> getListItemData(int size) {

            List<ItemObjects> listViewItems = new ArrayList<ItemObjects>();

            for (int i = 0; i < size; i++) {
                if (Brand.get(i).equals("Espice"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.espice));
                else if (Brand.get(i).equals( "Old Books House"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.bookshop));
                else if (Brand.get(i).equals( "Kyber Shinwari Tikka House"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.khyber));
                else if (Brand.get(i).equals("Crispy Broast"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.crisp));
                else if (Brand.get(i).equals("Concordia 1"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.concordia));
                else if (Brand.get(i).equals( "Zeeshan Electronics"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.ze));
                else if (Brand.get(i).equals( "Femina"))
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.femina));
                else
                    listViewItems.add(new ItemObjects("Brand: " + Brand.get(i) + "\nOutlet: " + Outlet.get(i) + "\nProduct: " + Product.get(i) + "\n Price: " + Price.get(i) + "\nCategory: " + Category.get(i), R.drawable.rand));


                //  Toast.makeText(Customer_Dashboard.this, "no of ads: " + size, Toast.LENGTH_SHORT).show();
            }
            return listViewItems;
        }

        public void callLists(int size){


            gaggeredList = getListItemData(size);

            rcAdapter = new SolventRecyclerViewAdapter(Customer_Dashboard.this, gaggeredList);
            recyclerView.setAdapter(rcAdapter);



        }
        public float distance_call(Double plat, Double plng){

            float results1[]=new float[1];
            results1[0]=0;
            Location.distanceBetween(plat,plng,33.642546, 72.990495,results1);
            results1[0]= results1[0]/1000;
            //   Location.distanceBetween(plat,plng,sllat,slon,results1);

            return results1[0];
        }

    }




}
