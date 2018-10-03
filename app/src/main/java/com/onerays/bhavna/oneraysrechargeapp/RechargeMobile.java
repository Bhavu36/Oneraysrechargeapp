package com.onerays.bhavna.oneraysrechargeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.sun.mail.imap.Utility;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;



public class RechargeMobile extends AppCompatActivity implements OnTapList{

    EditText MobileNo, Amount;
    Button buttonRecharge;
    Spinner spinnerCompany, spinnerState;
    RadioButton radioPrepaid, radioPostpaid;
    long mobile;
    long cardno;
    String mode = "", provider = "", state = "", dateD, timeD;
    String mobileS, amountS;
    CardListFragment cd;
    DbHandlersUsers db;
    TimeToDate dateObj;
    SessionManager sessionManager;
    Bundle bundle;
    Intent i;
    private long cardno1;
    private Utility myUtility;

    private RechargeMVP.Presenter mRechargePresenter;
    private String intentTitleStr;
     //private RechargePresenter mRechargePresenter;
   // private PaymentGatewayPresenter mPaymentGatewayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_recharge_mobile);

        db = new DbHandlersUsers(RechargeMobile.this);
        sessionManager = new SessionManager(this);
        mobile = sessionManager.getMobile();

        // Check if Payment Card is Added by the User
        boolean res1 = db.BankDetailsCount();
        boolean res2 = db.CardDetailsUser(mobile);
        if (res1 == false){
            AlertDialog();
        }
        else if(res2 == false){
            AlertDialog();
        }
        else {

            MobileNo = (EditText) findViewById(R.id.MobileNo);
            Amount = (EditText) findViewById(R.id.Amount);
            radioPrepaid = (RadioButton) findViewById(R.id.prepaidRadioButton);
            radioPostpaid = (RadioButton) findViewById(R.id.postpaidRadioButton);

            // Getting Date and Time
            dateObj = new TimeToDate(this);
            dateD = dateObj.getDate();
            timeD = dateObj.getTime();

            //Handling Spinner events
            spinnerCompany = (Spinner) findViewById(R.id.spinnerCompanyName);
            spinnerState = (Spinner) findViewById(R.id.spinnerStateName);

            final ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.providers, android.R.layout.simple_spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCompany.setAdapter(adapter1);

            spinnerCompany.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Value for provider variable is obtained here
                    provider = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerState.setAdapter(adapter2);

            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    //Value for state variable is obtained here
                    state = adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            buttonRecharge = (Button) findViewById(R.id.buttonRecharge);
            buttonRecharge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mobileS = MobileNo.getText().toString();
                    amountS = Amount.getText().toString();

                    if (mobileS.equals("") || amountS.equals("") || mode.equals("") || provider.equals("") || state.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                    } else if (mobileS.length() < 10) {
                        Toast.makeText(getApplicationContext(), "Enter Correct Mobile Number", Toast.LENGTH_SHORT).show();
                    } else if (amountS.equals("0") || amountS.startsWith("0")) {
                        Toast.makeText(getApplicationContext(), "Enter Valid Amount", Toast.LENGTH_SHORT).show();
                    } else {

                        final android.app.FragmentManager fm = getFragmentManager();
                        final CardListFragment cd = new CardListFragment();
                        cd.show(fm, "Card_List");
                        Toast.makeText(getApplicationContext(), "Select Your Payment Card", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    public void onRadioButtonClicked(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch(view.getId()){
            case R.id.prepaidRadioButton:
                if(checked){
                    mode="prepaid";
                }
                break;
            case R.id.postpaidRadioButton:
                if(checked){
                    mode="postpaid";
                }
                break;
        }
    }

    public void AlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(RechargeMobile.this);
        builder.setTitle("You Didnt Add Any Payment Card Yet !!");
        // Set up the buttons
        builder.setPositiveButton("Add Card", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    Intent i = new Intent(getApplicationContext(), AddCard.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
        });
        builder.setNegativeButton("Back To Main", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });

        builder.show();
    }

    protected String doInBackground(String... params) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("RECHARGE_MOBILE_NO", MobileNo.getText().toString());
        hashMap.put("AMOUNT", Amount.getText().toString());
        hashMap.put( "RECHARGE_MODE",mode.getBytes().toString() );
        hashMap.put( "OPERATOR",provider.getBytes().toString() );
        hashMap.put( "STATE",state.getBytes().toString() );
        hashMap.put( "RE_DATE",dateD.getBytes().toString() );
        hashMap.put( "RE_TIME",timeD.getBytes().toString() );
        return new MakeServiceCall().MakeServiceCall(Constant.URL + "submit.php", MakeServiceCall.POST, hashMap);

    }

    public class Recharge extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection;
        String urlString;
        @Override
        protected String doInBackground(String... strings) {
            urlString = "https://agonistical-threat.000webhostapp.com/omg/submit.php";
            try {
                URL url = new URL(urlString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");

                urlConnection.setDoOutput(true);
                com.onerays.bhavna.oneraysrechargeapp.Recharge recharge;
                AsyncTask<String, Void, String> asyncTask;


                String postingData = URLEncoder.encode("pin", "UTF-8") + "=" + URLEncoder.encode("123456789012345", "UTF-8") +
                        "&" + URLEncoder.encode("mobile-no", "UTF-8") + "=" + URLEncoder.encode("9845621456", "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                bufferedWriter.write(postingData);
                bufferedWriter.flush();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                String result = null;
                while ((line = bufferedReader.readLine()) != null) {
                    result = result + line;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


    }

    @Override
    public String toString () {
        return "RechargeMobile{" + "MobileNo=" + MobileNo + ", Amount=" + Amount + ", buttonRecharge=" + buttonRecharge + ", spinnerCompany=" + spinnerCompany + ", spinnerState=" + spinnerState + ", radioPrepaid=" + radioPrepaid + ", radioPostpaid=" + radioPostpaid + ", mobile=" + mobile + ", cardno=" + cardno + ", mode='" + mode + '\'' + ", provider='" + provider + '\'' + ", state='" + state + '\'' + ", dateD='" + dateD + '\'' + ", timeD='" + timeD + '\'' + ", mobileS='" + mobileS + '\'' + ", amountS='" + amountS + '\'' + ", cd=" + cd + ", db=" + db + ", dateObj=" + dateObj + ", sessionManager=" + sessionManager + ", bundle=" + bundle + ", i=" + i + ", cardno1=" + cardno1 + ", myUtility=" + myUtility + ", mRechargePresenter=" + mRechargePresenter + ", intentTitleStr='" + intentTitleStr + '\'' + '}';
    }

    public void onClick(DialogInterface dialog, int whichButton) {
        long time = System.currentTimeMillis();
        try {
            String url = AppConstants.RECHARGE_LIVE_URL + AppConstants.RECHARGE_API + AppConstants.FORMAT_KEY + AppConstants.FORMAT_JSON_VALUE +
                    AppConstants.TOKEN_KEY + AppConstants.TOKEN_VALUE + AppConstants.MOBILE_KEY +MobileNo  +
                    AppConstants.AMOUNT_KEY + Amount + AppConstants.OPERATOR_ID_KEY +provider  +
                    AppConstants.UNIQUE_ID_KEY + time + AppConstants.OPIONAL_VALUE1_KEY + URLEncoder.encode(intentTitleStr, "utf-8") +
                    AppConstants.OPIONAL_VALUE2_KEY + URLEncoder.encode("Recharge", "utf-8");

                   // myUtility.printLogcat("API::::" + url);


            mRechargePresenter.callRechargeAPI(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        dialog.dismiss();
    }



    @Override
    public void OnTapView(long cno) {
        cardno = cno;

        long rmobno = Long.parseLong(mobileS);
        int am = Integer.parseInt(amountS);
        int balance = db.checkBalance(cardno);

        if (balance < am){
            Toast.makeText(getApplicationContext(), "Payment Not Done ! Balance is Low !", Toast.LENGTH_SHORT).show();
        }
        else {

            long id = db.addRecharge(mobile, rmobno, mode, am, provider, state, dateD, timeD, cardno);
            boolean res = db.updateBalance(cardno, balance - am);
            long transID = db.addTrans(cardno, "Mobile", rmobno, am, balance - am ,mobile, dateD, timeD);


            if ((id != 0) && (transID != 0) && (res == true)) {

                Toast.makeText(getApplicationContext(), "Mobile Recharge Done Successfully !!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Card Not Added ! Please Try Again !", Toast.LENGTH_SHORT).show();
            }
        }
    }




}

