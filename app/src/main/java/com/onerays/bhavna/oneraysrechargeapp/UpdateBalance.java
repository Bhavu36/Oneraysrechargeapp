package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static java.lang.Boolean.TRUE;

public class UpdateBalance extends AppCompatActivity {

    EditText number , amount;
    Button update;
    long cardNumber;
    int cardAmount;
    String num , amt ;

    DbHandlersUsers db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_update_balance);

        number = (EditText)findViewById(R.id.number);
        amount = (EditText)findViewById(R.id.balance);
        update = (Button)findViewById(R.id.submit);

        db = new DbHandlersUsers(this);

        //This class takes the debit card no of the user whose balance is to be updated,
        //The new balance will directly be overwritten instead of adding it to the old balance value,

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                num = number.getText().toString();
                amt = amount.getText().toString();

                if(num.equals("") || amt.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter All The Details", Toast.LENGTH_SHORT).show();
                }
                else
                    {

                    cardNumber = Long.parseLong(num);
                    cardAmount = Integer.parseInt(amt);

                    // Getting Prev Balance

                    int prevBalance = db.checkBalance(cardNumber);

                     // Adding to the prev Balance

                    boolean flag = db.updateBalance(cardNumber, cardAmount + prevBalance);

                    if(flag == TRUE){

                        Toast.makeText(getApplicationContext(),"Balance Updated", Toast.LENGTH_SHORT).show();

                    }
                    else {

                        Toast.makeText(getApplicationContext(),"Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                }
            }
        });
    }
}
