
package com.onerays.bhavna.oneraysrechargeapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LandlinePayment extends AppCompatActivity implements OnTapList {

    Bundle bundle;
    TextView textView;
    ImageView imageView;
    EditText telephone,amount;
    Button pay;
    String numberS,amountS,b,dateD,timeD;
    long number, userMobile, cardno;
    int am;
    DbHandlersUsers db;
    TimeToDate dateObj;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_land_pay);

        sessionManager = new SessionManager(this);

        //Code for getting the telephone and amount & sending the information to the payment page
        bundle = getIntent().getExtras();
        int a = bundle.getInt("Image");
        b = bundle.getString("provider");

        db = new DbHandlersUsers(LandlinePayment.this);
        sessionManager = new SessionManager(this);
        userMobile = sessionManager.getMobile();

        // Check if Payment Card is Added by the User
        boolean res1 = db.BankDetailsCount();
        boolean res2 = db.CardDetailsUser(userMobile);

        if (res1 == false){
            AlertDialog();
        }
        else if(res2 == false){
            AlertDialog();
        }
        else {

            textView = (TextView) findViewById(R.id.textView1);
            imageView = (ImageView) findViewById(R.id.imageView1);
            telephone = (EditText) findViewById(R.id.telephoneNumber);
            amount = (EditText) findViewById(R.id.paymentAmount);
            pay = (Button) findViewById(R.id.buttonContinue);
            userMobile = sessionManager.getMobile();

            textView.setText(b);
            imageView.setImageResource(a);

            //Getting the current date and time from TimeToDate Class,
            dateObj = new TimeToDate(this);
            dateD = dateObj.getDate();
            timeD = dateObj.getTime();

            pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    numberS = telephone.getText().toString();
                    amountS = amount.getText().toString();

                    //Basic error handling to check for valid inputs,
                    if (numberS.equals("") || amountS.equals("")) {
                        Toast.makeText(getApplicationContext(), "Enter All The Details", Toast.LENGTH_SHORT).show();
                    } else if (numberS.length() < 11) {
                        Toast.makeText(getApplicationContext(), "Enter Valid 11-digit Telephone Number", Toast.LENGTH_SHORT).show();
                    } else if (!numberS.startsWith("0")) {
                        Toast.makeText(getApplicationContext(), "Telephone Number should start with 0 as Prefix", Toast.LENGTH_SHORT).show();
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


    public void AlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(LandlinePayment.this);

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

        number = Long.parseLong(numberS);
        am = Integer.parseInt(amountS);
        int balance = db.checkBalance(cardno);

        if (balance < am){
            Toast.makeText(getApplicationContext(), "Payment Not Done ! Balance is Low !", Toast.LENGTH_SHORT).show();
        }
        else {

            long id = db.addLandline(userMobile, number, am, b, dateD, timeD, cardno);
            boolean res = db.updateBalance(cardno, balance - am);
            long transID = db.addTrans(cardno, "LandLine", number, am, balance - am , userMobile, dateD, timeD);

            if ((id != 0) && (transID != 0) && (res == true)) {

                Toast.makeText(getApplicationContext(), "Landline Recharge Done Successfully !!", Toast.LENGTH_SHORT).show();
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

