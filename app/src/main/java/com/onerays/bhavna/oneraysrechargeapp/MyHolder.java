package com.onerays.bhavna.oneraysrechargeapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


public class MyHolder extends RecyclerView.ViewHolder{

    public TextView cno;
    public TextView bank;
    public TextView balance;

    public MyHolder(View itemView) {
        super(itemView);
        cno = (TextView) itemView.findViewById( R.id.cardNo);
        bank = (TextView) itemView.findViewById(R.id.bankName);
        balance = (TextView) itemView.findViewById(R.id.balance);

    }

}
