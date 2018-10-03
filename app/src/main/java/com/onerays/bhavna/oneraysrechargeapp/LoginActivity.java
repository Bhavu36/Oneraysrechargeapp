
package com.onerays.bhavna.oneraysrechargeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    EditText l_emailorphonenumber, l_password;
    Button l_login, l_signup;
    ImageView l_imageview;
    SharedPreferences sp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        sp = getSharedPreferences(Constant.PREF, MODE_PRIVATE);
       l_emailorphonenumber = (EditText) findViewById(R.id.l_emailorphonenumber);
        l_password = (EditText) findViewById(R.id.l_password);
        l_login = (Button) findViewById(R.id.l_login);
        l_signup = (Button) findViewById(R.id.l_signup);
        l_imageview = (ImageView) findViewById(R.id.imageView);
        forgot = (TextView) findViewById(R.id.forgotPass);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this, ForgotPasswordActivtiy.class));
            }
        });
        l_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (l_emailorphonenumber.getText().toString().equalsIgnoreCase("")) {
                    l_emailorphonenumber.setError("The Field Can't Be Empty");
                } else if (l_emailorphonenumber.getText().toString().matches(emailPattern)) {
                    l_emailorphonenumber.setError("Email Id is Invalid");
                } else if (l_password.getText().toString().equalsIgnoreCase("")) {
                    l_password.setError("The Field Can't Be Empty");
                } else if (l_password.getText().toString().length() != 8) {
                    l_password.setError("Password must be 8 Charector");
                } else {
                    Toast.makeText( LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    if (new Connectioncall(LoginActivity.this).isConnectingToInternet()) {
                        new loginclass().execute();
                    } else {
                        new Connectioncall(LoginActivity.this).connectiondetect();

                    }
                }
            }

        });
        l_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, singup.class);
                startActivity(intent);
            }
        });
    }

    private class loginclass extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(LoginActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("username", l_emailorphonenumber.getText().toString());
            hashMap.put("password", l_password.getText().toString());
            return new MakeServiceCall().MakeServiceCall(Constant.URL + "login.php", MakeServiceCall.POST, hashMap);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("Status").equalsIgnoreCase("True")) {
                    JSONArray array = object.getJSONArray("response");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                       sp.edit().putString(Constant.ID, jsonObject.getString("wsi_id")).commit();
                        sp.edit().putString(Constant.TYPE, jsonObject.getString("wsi_type")).commit();
                        if (jsonObject.getString("wsi_type").equalsIgnoreCase("Worker")) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(LoginActivity.this,MainActivity .class);
                            startActivity(intent);
                        }
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(LoginActivity.this, object.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}

