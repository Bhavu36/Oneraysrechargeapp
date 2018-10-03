
package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddCard extends AppCompatActivity {

    EditText hName, bName, cardNo ,cExpiry1, cExpiry2, cCvv;
    Button addPay;

    long mobile, card;
    String cardExpiry1, cardExpiry2, cardCvv, holderName, cardN, bankName;
    String Expiry,dateD,timeD;

    DbHandlersUsers db;
    TimeToDate dateObj;
    SessionManager sessionManager;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_add_card_details);

        //Getting the current date and time from TimeToDate Class,
        db = new DbHandlersUsers(AddCard.this);
        sessionManager = new SessionManager(this);
        dateObj = new TimeToDate(this);

        dateD = dateObj.getDate();
        timeD = dateObj.getTime();


        hName = (EditText) findViewById(R.id.holderName);
        bName = (EditText) findViewById(R.id.bankName);
        cardNo = (EditText) findViewById(R.id.cardNo);
        cExpiry1 = (EditText) findViewById(R.id.cardExpiry1);
        cExpiry2 = (EditText) findViewById(R.id.cardExpiry2);
        cCvv = (EditText) findViewById(R.id.cardCvv);
        addPay = (Button) findViewById(R.id.buttonAddPayment);






        addPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the required data
                holderName = hName.getText().toString();
                bankName = bName.getText().toString();
                cardN = cardNo.getText().toString();
                cardExpiry1 = cExpiry1.getText().toString();
                cardExpiry2 = cExpiry2.getText().toString();
                Expiry = cardExpiry1 + "/" + cardExpiry2;
                cardCvv = cCvv.getText().toString();
                mobile = sessionManager.getMobile();

                // Convert cardN to Long
                card = Long.parseLong(cardN);
                System.out.println( Integer.parseInt(cardExpiry1));
                System.out.println( Integer.parseInt(cardExpiry2));
                int c1 = Integer.parseInt(cardExpiry1);
                int c2 = Integer.parseInt(cardExpiry2);

                if (holderName.equals("") || bankName.equals("") || cardN.equals("") || cardExpiry1.equals("") || cardExpiry2.equals("") || cardCvv.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter All The Details", Toast.LENGTH_SHORT).show();
                } else if ((c1 <= 0) || (c1 >= 13) ) {
                    Toast.makeText(getApplicationContext(), "Please Enter A Valid Month", Toast.LENGTH_SHORT).show();
                } else if ((c2 <= 16) || (c2 >= 40) ) {
                    Toast.makeText(getApplicationContext(), "Please Enter A Valid Year", Toast.LENGTH_SHORT).show();
                } else if (db.checkDuplicateCard(card)) {
                    Toast.makeText(getApplicationContext(), "Card No Already Added ! Try Another Card No", Toast.LENGTH_SHORT).show();
                } else {

                    long id = db.addBankDetails(card, Expiry, cardCvv, mobile, holderName, bankName, dateD, timeD);

                    if (id != 0) {

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Card Added Successfully !!", Toast.LENGTH_SHORT).show();
                        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();

                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Card Not Added ! Please Try Again !", Toast.LENGTH_SHORT).show();
                    }


                }



            }
        });


    }
}


