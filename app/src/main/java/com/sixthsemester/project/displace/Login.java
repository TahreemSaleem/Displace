package com.sixthsemester.project.displace;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sixthsemester.project.displace.jsonhandle.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    EditText email,pass;
    private ArrayList<String> pref_array       = new ArrayList<String>();

    String email_string;
    String password_string;
    JSONObject jsonobj;
    private JSONParser jparser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.email);

// Set drawables for left, top, right, and bottom - send 0 for nothing
        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
        pass = (EditText)findViewById(R.id.password);

// Set drawables for left, top, right, and bottom - send 0 for nothing
        pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, 0, 0);


        TextView signup = (TextView)findViewById(R.id.signup_text);
        String htmlString="No account yet? Create <u>one</u>";
        signup.setText(Html.fromHtml(htmlString));
    }

    public void signup(View view){
        //add app code to launch signup activity
        Intent intent = new Intent(Login.this, Signup_selection.class);
        startActivity(intent);
    }
    public void login(View view){

        email_string = email.getText().toString();
        password_string = pass.getText().toString();

        ViewAsyncTask vat = new ViewAsyncTask(email_string,password_string);
        vat.execute();

    }

    public class ViewAsyncTask extends AsyncTask<Void, Void, Void> {
        int flag =0 ;
        private JSONParser jparser=new JSONParser();
        private ProgressDialog pDialog;
        String email,pass;
        public ViewAsyncTask(String email,String password){
            this.email = email;
            this.pass = password;

        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Checking Credentials");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected Void doInBackground(Void... arg0) {
            try {

                // Log.e("name is ",getData().toString());

                JSONArray jarr = getData();

                for(int i = 0;i<jarr.length();i++){
                    jsonobj = jarr.getJSONObject(i);

                    if(jsonobj.getString("email").equals(email) && jsonobj.getString("pass").equals(pass))
                    {
                        flag = 1;
                        if(jsonobj.getString("type").equals("provider"))
                        {
                            //change Ad_post to provider_dashboard
                            //Toast.makeText(Login.this, "going to dashboard", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, ProviderDashBoard.class);
                                intent.putExtra("Email",email);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(Login.this, customer_picks.class);

                            JSONArray jArray =  jsonobj.getJSONArray("preference");
                            if (jArray != null)
                            {
                                for (int k = 0; k < jArray.length(); k++)
                                {
                                    pref_array.add(jArray.get(k).toString());
                                }
                            }
                            intent.putExtra("NameArray",pref_array);
                            intent.putExtra("Email",email);
                            startActivity(intent);
                            break;
                        }

                    }
                    //email_list.add(jsonobj.getString("email"));
                    //password_list.add(jsonobj.getString("password"));

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            pDialog.dismiss();
            try {
                if (flag == 0)
                    Toast.makeText(Login.this, "Email/password incorrect", Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public JSONArray getData(){

            String url = Constants.url + Constants.API_KEY;
            return jparser.makeHttpGETRequest(url);

        }
    }
}
