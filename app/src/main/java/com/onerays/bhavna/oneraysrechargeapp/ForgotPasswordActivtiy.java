package com.onerays.bhavna.oneraysrechargeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class ForgotPasswordActivtiy extends AppCompatActivity {
    EditText email, otp, new_pass, confirm_pass;
    String semail, sRendom, spass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button getOtp, varify, submit;
    LinearLayout getOtp_layout, varify_layout, submit_layout;
    TextView txt_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_forgot_password_activtiy );
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        email = (EditText) findViewById(R.id.forgot_password_email);
        otp = (EditText) findViewById(R.id.forgot_password_otp);
        varify = (Button) findViewById(R.id.forgot_password_verify);
        getOtp_layout = (LinearLayout) findViewById(R.id.forgot_password_getOtp_layout);
        varify_layout = (LinearLayout) findViewById(R.id.forgot_password_varify_layout);
        getOtp = (Button) findViewById(R.id.forgot_password_next);
        new_pass = (EditText) findViewById(R.id.forgot_password_password);
        confirm_pass = (EditText) findViewById(R.id.forgot_password_cpassword);
        submit = (Button) findViewById(R.id.forgot_password_submit);
        submit_layout = (LinearLayout) findViewById(R.id.forgot_password_submit_layout);
        txt_email = (TextView) findViewById(R.id.forgot_password_txt_email);
        final Random random = new Random();
        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                semail = email.getText().toString();
                if (semail.matches(emailPattern)) {
                } else {
                    Toast.makeText(ForgotPasswordActivtiy.this, "In Valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.getText().toString().equalsIgnoreCase("")) {
                    email.setError("Email Id Required");
                    return;
                } else {
                    sRendom = String.valueOf(random.nextInt(1000000) + 1);
                    String s1 = null;
                    String s_signup_email= null;
                    new OTP(ForgotPasswordActivtiy.this, semail, Constant.OTPSUBJECT, "Hi,\t" + semail + "\n" + Constant.OTPMessage + sRendom, s_signup_email, s1 ).execute();
                    getOtp_layout.setVisibility(View.GONE);
                    submit_layout.setVisibility(View.GONE);
                    varify_layout.setVisibility(View.VISIBLE);
                    txt_email.setText(semail);
                }
            }
        });
        varify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (otp.getText().toString().equals(sRendom)) {
                    Toast.makeText(ForgotPasswordActivtiy.this, "Success", Toast.LENGTH_SHORT).show();
                    getOtp_layout.setVisibility(View.GONE);
                    submit_layout.setVisibility(View.VISIBLE);
                    varify_layout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(ForgotPasswordActivtiy.this, "UnSuccess", Toast.LENGTH_SHORT).show();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new_pass.getText().toString().equalsIgnoreCase("")) {
                    new_pass.setError("Password Required");
                    return;
                }
                String snew_pass = new_pass.getText().toString();
                String sconfirm_pass = confirm_pass.getText().toString();
                if (snew_pass.equals(sconfirm_pass)) {
                    if (new Connectioncall(ForgotPasswordActivtiy.this).isConnectingToInternet()) {
                        spass = new_pass.getText().toString();
                        new forgotPass().execute();
                    } else {
                        new Connectioncall(ForgotPasswordActivtiy.this).connectiondetect();
                    }
                } else {
                    confirm_pass.setError("Password Does Not Match");
                }
            }
        });
    }

       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           int id = item.getItemId();

           if (id == android.R.id.home) {
               startActivity(new Intent(ForgotPasswordActivtiy.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
           }
           return super.onOptionsItemSelected(item);
       }

    private class forgotPass extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ForgotPasswordActivtiy.this);
            pd.setMessage("Loading...");
            pd.setCancelable(true);
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("action", "forgot");
            hashMap.put("username", semail);
            hashMap.put("password", spass);
            return new MakeServiceCall().MakeServiceCall(Constant.URL + "register.php", MakeServiceCall.POST, hashMap);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            try {
                JSONObject jsonObject = new JSONObject(s.toString());
                if (jsonObject.getString("Status").equalsIgnoreCase("True")) {
                    Toast.makeText(ForgotPasswordActivtiy.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPasswordActivtiy.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                } else {
                    Toast.makeText(ForgotPasswordActivtiy.this, jsonObject.getString("Message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
