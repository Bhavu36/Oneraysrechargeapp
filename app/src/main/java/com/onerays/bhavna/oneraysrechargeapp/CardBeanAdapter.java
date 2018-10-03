package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

public class CardBeanAdapter extends RecyclerView.Adapter<MyHolder> {

    private Context c;
    private OnTapList onTapList;
    public List<CardBean> items = Collections.emptyList();

    public CardBeanAdapter(Context c, List<CardBean> items) {
        this.c = c;
        this.items = items;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.custom_card_item, parent, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final int position) {
        final CardBean item = items.get(position);
        holder.cno.setText("Card No - " + item.getCardNo());
        holder.bank.setText("Bank Name - " + item.getBankName());
        holder.balance.setText("Balance - " + item.getBalance());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTapList != null){
                    onTapList.OnTapView(item.getCardNo());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnTapList(OnTapList onTapList){
        this.onTapList = onTapList;
    }

}

