package com.sixthsemester.project.displace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;


public class Signup extends AppCompatActivity implements View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    private TextView mStatusTextView;
    private Object OnConnectionFailedListener;
    String pass, repass,email;
    EditText email1;
    EditText pass1;
    EditText repass1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

      email1 = (EditText)findViewById(R.id.Email);
        email1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
       pass1 = (EditText)findViewById(R.id.Password);
        pass1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, 0, 0);

       repass1 = (EditText)findViewById(R.id.enterPassword);
        repass1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.enterpassword, 0, 0, 0);
        TextView signup = (TextView)findViewById(R.id.signup_text);
        String htmlString="Already a memeber? <u>Login</u>";
        signup.setText(Html.fromHtml(htmlString));



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this ,(GoogleApiClient.OnConnectionFailedListener) OnConnectionFailedListener )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.


        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            // ...
        }
    }
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            email=acct.getEmail();

            //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Intent intent = new Intent(Signup.this, customer_picks.class);
            startActivity(intent);
        } else {
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);

        }
    }
public void login(View view){

    Intent intent = new Intent(Signup.this, Login.class);
    startActivity(intent);

}
public void create_account(View view)
{
   pass= pass1.getText().toString();
    pass=pass.trim();
   repass= repass1.getText().toString();
    repass=repass.trim();
   email= email1.getText().toString();
    email=email.trim();


   // Toast.makeText(getApplicationContext(), " "+email+" " +pass+ " " +repass, Toast.LENGTH_SHORT).show();
    //conditions
    if((!(pass.isEmpty()) & !(repass.isEmpty()) & !(email.isEmpty()))&( (pass.equals(repass))) &(!((email.contains(" "))|(pass.contains(" "))|(pass.contains(" "))) )& (email.contains("@")) & (email.contains(".com")) & (pass.length()>= 6)&(repass.length()>= 6) ){


        email = email1.getText().toString();
        pass = pass1.getText().toString();
        repass = repass1.getText().toString();

        InsertAsyncTask tsk = new InsertAsyncTask(email,pass);
        tsk.execute();


    }
    else{

        if((pass.isEmpty()) | (repass.isEmpty()) | (email.isEmpty())) {
            Toast.makeText(getApplicationContext(), "Empty and invalid", Toast.LENGTH_SHORT).show();


        }
        else {
           if (!(pass.equals(repass))){
               Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();

           }
            if ((pass.length() < 6) | (repass.length() < 6)) {
                Toast.makeText(getApplicationContext(), "Password Too Short. Minimum Length: 6 digits", Toast.LENGTH_SHORT).show();
            }
           if(!( (email.contains("@")) & (email.contains(".com")))){
               Toast.makeText(getApplicationContext(), "Invalid email ID", Toast.LENGTH_SHORT).show();
           }
            if ((email.contains(" "))|(pass.contains(" "))|(pass.contains(" "))){
                Toast.makeText(getApplicationContext(), "No spaces allowed.Please remove spaces", Toast.LENGTH_SHORT).show();
            }
        }
       // Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();
    }

}
    public class InsertAsyncTask extends AsyncTask<Void, Void, Void> {
        private JSONParser jparser=new JSONParser();
        private String email,pass;
        private ProgressDialog pDialog;
        JSONObject myJson = null;

        public InsertAsyncTask(String email,String password){
            this.email = email;
            this.pass = password;

        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Signup.this);
            pDialog.setMessage("Sending Data");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {
                JSONArray jarr = getData();
                JSONObject jsonobj = new JSONObject();
                JSONObject jsonParam = new JSONObject();

                jsonParam.put("email", email);
                jsonParam.put("pass", pass);
                jsonParam.put("type","Customer");
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
                Toast.makeText(Signup.this, "Data Inserted Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Signup.this, Prefrences.class);
                intent.putExtra("Email",email);
                startActivity(intent);
            }else{
                Toast.makeText(Signup.this, "Network failed", Toast.LENGTH_SHORT).show();

            }
        }
        public JSONArray getData(){

            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);

        }
    }

}
