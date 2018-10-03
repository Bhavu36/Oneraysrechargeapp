
package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends BaseAdapter {

    private Context context;
    private String[] provider;
    private int[] images;

    public ListAdapter(Context context, String[] p, int[] i) {
        this.context=context;
        provider=p;
        images=i;
    }

    @Override
    public int getCount() {
        return provider.length;//this is the length of listview's list items,
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //This will include how many fields we have in our listview's list item,
    class Holder{
        TextView textView;
        ImageView imageView;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holderView;

        if(convertView==null){
            convertView= View.inflate(context, R.layout.custom_list_item, null);

            holderView=new Holder();

            holderView.textView = (TextView) convertView.findViewById(R.id.textView1);
            holderView.imageView = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holderView);//setTag on View object,

        }
        else{
            holderView=(Holder)convertView.getTag();//getTag on Holder class object,
        }
        holderView.textView.setText(provider[position]);
        holderView.imageView.setImageResource(images[position]);

        //Code for getting which list item was clicked and sending the same to LandlinePayments2,
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context,LandlinePayment.class);
                Bundle bundle=new Bundle();
                bundle.putInt("Image",images[position]);
                bundle.putString("provider",provider[position]);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
        return convertView;
    }
}

