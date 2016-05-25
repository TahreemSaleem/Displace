package com.sixthsemester.project.displace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.appindexing.Action;
//import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONObject;

/**
 * Created by hps on 02/05/2016.
 */
public class provider_signup extends FragmentActivity {

    String brand,email,contact,password;
    EditText email2;
    EditText brand1;
    EditText contact1;
    EditText password1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
   // private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_signup);
        brand1= (EditText)findViewById(R.id.brandname);

        email2 = (EditText)findViewById(R.id.Email);


        contact1 = (EditText)findViewById(R.id.contact);

        password1 = (EditText)findViewById(R.id.password);
        TextView signup = (TextView) findViewById(R.id.signup_text);
        String htmlString = "Already a memeber? <u>Login</u>";
        signup.setText(Html.fromHtml(htmlString));
        // category = (Button) findViewById(R.id.category1);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      //  client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }




   /* @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ProviderSignup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sixthsemester.project.displace/http/host/path")
        );
        //  AppIndex.AppIndexApi.start(client, viewAction);
    }*/

    /*@Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ProviderSignup Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.sixthsemester.project.displace/http/host/path")
        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
    public void login(View view) {

        Intent intent = new Intent(provider_signup.this, Login.class);
        startActivity(intent);
    }
    public void create_account(View view ){
        brand= brand1.getText().toString();
        brand=brand.trim();
        contact= contact1.getText().toString();
        contact=contact.trim();
        email= email2.getText().toString();
        email=email.trim();
        if((!(contact.isEmpty()) & !(brand.isEmpty()) & !(email.isEmpty())) &(!((email.contains(" ")) )& (email.contains("@")) & (email.contains(".com"))  )){

            email = email2.getText().toString();
            contact = contact1.getText().toString();
            brand= brand1.getText().toString();
            password= password1.getText().toString();
            InsertAsyncTask tsk = new InsertAsyncTask(email,password,brand,contact);
            tsk.execute();
            // Intent intent = new Intent(provider_signup.this, Ad_post.class);
            // startActivity(intent);



            // db code here


        }
        else {
            if((email.isEmpty()) | (brand.isEmpty()) | (contact.isEmpty())) {
                Toast.makeText(getApplicationContext(), "Empty and invalid", Toast.LENGTH_SHORT).show();


            }
            if(!( (email.contains("@")) & (email.contains(".com")))){
                Toast.makeText(getApplicationContext(), "Invalid email ID", Toast.LENGTH_SHORT).show();
            }
        }
        //    Intent intent = new Intent(provider_signup.this, Ad_post.class);
        //    startActivity(intent);

    }

    public class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser=new JSONParser();
        private String email,pass,brand,contact;
        private ProgressDialog pDialog;
        JSONObject myJson = null;

        public InsertAsyncTask(String email,String password, String brand, String contact){
            this.email = email;
            this.pass = password;
            this.brand = brand;
            this.contact = contact;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(provider_signup.this);
            pDialog.setMessage("Sending Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("email", email);
                jsonParam.put("pass", pass);
                jsonParam.put("type","provider");
                jsonParam.put("contact",contact);
                jsonParam.put("brand",brand);
                myJson = jparser.makeHttpPOSTRequest(Constants.url+ Constants.API_KEY, jsonParam);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            if(myJson!=null) {
                Toast.makeText(provider_signup.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(provider_signup.this, ProviderDashBoard.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }else{
                Toast.makeText(provider_signup.this, "Failed Check Network", Toast.LENGTH_SHORT).show();
            }
        }
    }
}