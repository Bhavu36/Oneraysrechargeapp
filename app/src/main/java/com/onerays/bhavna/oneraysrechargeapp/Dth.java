package com.onerays.bhavna.oneraysrechargeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Dth extends AppCompatActivity implements OnTapList {

    EditText dthNo,amount;
    Spinner spinnerCompany;
    long mobile,cardno;
    DbHandlersUsers db;
    TimeToDate dateObj;

    SessionManager sessionManager;
    Bundle bundle;
    Button paySubmit;
    String dthN,amt,dateD,timeD;

    String provider="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_dth);

        db = new DbHandlersUsers(Dth.this);
        sessionManager = new SessionManager(this);
        mobile = sessionManager.getMobile();

        dateObj = new TimeToDate(this);

        dateD = dateObj.getDate();
        timeD = dateObj.getTime();

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

            dthNo = (EditText)findViewById(R.id.dthNumber);
            amount = (EditText)findViewById(R.id.amount);
            paySubmit = (Button)findViewById(R.id.buttonDone);


            spinnerCompany = (Spinner) findViewById(R.id.spinnerCompanyName);

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dthproviders, android.R.layout.simple_spinner_item);
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

            paySubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dthN = dthNo.getText().toString();
                    amt = amount.getText().toString();

                    // Convert cardN to Long
                    long dth = Long.parseLong(dthN);
                    int a = Integer.parseInt(amt);

                    if (dthN.equals("") || provider.equals("") || amt.equals("")) {
                        Toast.makeText(getApplicationContext(), "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                    } else if (dthN.length() != 11) {
                        Toast.makeText(getApplicationContext(), "Enter 11 digit DTH Number", Toast.LENGTH_SHORT).show();
                    } else if (amt.equals("0") || amt.startsWith("0")) {
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

    public void AlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Dth.this);
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

    @Override
    public void OnTapView(long cno) {
        cardno = cno;

        long dthNo = Long.parseLong(dthN);
        int amount = Integer.parseInt(amt);
        int balance = db.checkBalance(cardno);

        if (balance < amount){
            Toast.makeText(getApplicationContext(), "Payment Not Done ! Balance is Low !", Toast.LENGTH_SHORT).show();
        }
        else {

            long id = db.addDth(mobile, dthNo, amount, provider, dateD, timeD, cardno);
            boolean res = db.updateBalance(cardno, balance - amount);
            long transID = db.addTrans(cardno, "DTH", dthNo, amount, balance - amount ,mobile, dateD, timeD);

            if ((id != 0) && (transID != 0) && (res == true)) {

                Toast.makeText(getApplicationContext(), "DTH Payment Done Successfully !!", Toast.LENGTH_SHORT).show();
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
