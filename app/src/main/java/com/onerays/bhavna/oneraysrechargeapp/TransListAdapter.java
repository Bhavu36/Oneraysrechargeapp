package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class TransListAdapter extends RecyclerView.Adapter<TransListAdapter.MyViewHolder> {

    private List<TransBean> transRecord;
    private Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView subsNo,type,cardNo,amountPaid,date,balance;

        public MyViewHolder(View view) {
            super(view);

            subsNo = (TextView) view.findViewById( R.id.subsNo);
            type = (TextView) view.findViewById(R.id.subsType);
            cardNo = (TextView) view.findViewById(R.id.subsCardNo);
            amountPaid = (TextView) view.findViewById(R.id.subsAmount);
            balance = (TextView)view.findViewById(R.id.bankBalance);
            date = (TextView) view.findViewById(R.id.subsDate);


        }
    }

    public TransListAdapter(Context c, List<TransBean> transRecord) {
        this.c = c;
        this.transRecord = transRecord;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trans_data_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransBean tb = transRecord.get(position);

        holder.subsNo.setText("Subscriber No - " + tb.getSerNum());
        holder.type.setText("Type - " + tb.getSerName());
        holder.cardNo.setText("Card No - " + tb.getCardNo());
        holder.amountPaid.setText("Amount Paid - Rs." + tb.getAmount());
        holder.balance.setText("Balance - Rs." + tb.getBalance());
        holder.date.setText("Date - " + tb.getDate());

    }

    @Override
    public int getItemCount() {
        return transRecord.size();
    }
}
