package com.sixthsemester.project.displace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Prefrences extends AppCompatActivity {

    int size = 10;
    int i = 0;
    String Email;
    ArrayList<String> ButtonName = new ArrayList<String>();
    // ArrayList<String> Customer_credentials =  new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefrences);
        Intent intent = getIntent();
        Email = intent.getStringExtra("Email");
    }

    public void select(View view) {

        view.setSelected(!view.isSelected());
        // GradientDrawable gd = new GradientDrawable();
       // Log.e("cat", "i am here");
        //gd.setStroke(5, Color.WHITE);
        if (view.isSelected()) {

            switch (view.getId()) {
                case R.id.shoe:

                    ButtonName.add("shoe");

                    break;

                case R.id.food:
                    ButtonName.add("food");

                    break;

                case R.id.beauty:
                    ButtonName.add("beauty");

                    break;
                case R.id.shirt:
                    ButtonName.add("shirt");

                    break;
                case R.id.books:
                    ButtonName.add("books");

                    break;
                case R.id.ring:
                    ButtonName.add("ring");

                    break;
                case R.id.drug:
                    ButtonName.add("drug");

                    break;
                case R.id.electronic:
                    ButtonName.add("electronic");

                    break;
                case R.id.microwave:
                    ButtonName.add("microwave");

                    break;
                case R.id.sports:
                    ButtonName.add("sports");

                    break;
            }
            //Handle selected state change
        } else {

            switch (view.getId()) {
                case R.id.shoe:
                    ButtonName.remove("shoe");
                    break;

                case R.id.food:
                    ButtonName.remove("food");

                    break;

                case R.id.beauty:
                    ButtonName.remove("beauty");
                    break;
                case R.id.shirt:
                    ButtonName.remove("shirt");
                    break;
                case R.id.books:
                    ButtonName.remove("books");
                    break;
                case R.id.ring:
                    ButtonName.remove("ring");
                    break;
                case R.id.drug:
                    ButtonName.remove("drug");
                    break;
                case R.id.electronic:
                    ButtonName.remove("electronic");
                    break;
                case R.id.microwave:
                    ButtonName.remove("microwave");
                    break;
                case R.id.sports:
                    ButtonName.remove("sports");
                    break;
            }
        }
    }

    public void Continue_btn_click(View view) {
        if (ButtonName.size() > 0) {
            InsertAsyncTask tsk = new InsertAsyncTask(ButtonName);
            tsk.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Please select your interests", Toast.LENGTH_LONG).show();
        }

    }
    public class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser=new JSONParser();
        ArrayList<String> ButtonName =  new ArrayList<String>();
        private ProgressDialog pDialog;
        JSONObject myJson = null;

        public InsertAsyncTask(ArrayList<String> ButtonName){
            this.ButtonName = ButtonName;


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Prefrences.this);
            pDialog.setMessage("Sending Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                JSONArray jarr = getData();
                for(int i = 0;i<jarr.length();i++) {
                    JSONObject jsonobj = jarr.getJSONObject(i);
                    if (jsonobj.getString("email").equals(Email)) {
                        JSONArray arr_obj = new JSONArray();
                        for (int j = 0; j < ButtonName.size(); j++) {
                            arr_obj.put(ButtonName.get(j));
                        }
                        jsonobj.put("preference", arr_obj);
                        myJson = jparser.makeHttpPOSTRequest(Constants.url + Constants.API_KEY, jsonobj);
                        break;
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
            if(myJson!=null) {
                Toast.makeText(Prefrences.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Prefrences.this, customer_picks.class);
                intent.putExtra("NameArray",ButtonName);
                startActivity(intent);
            }else{
                Toast.makeText(Prefrences.this, "Failed Check Network", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(Prefrences.this, customer_picks.class);
                //intent.putExtra("NameArray",ButtonName);
                //startActivity(intent);
            }
        }
        public JSONArray getData(){

            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);

        }
    }

}
