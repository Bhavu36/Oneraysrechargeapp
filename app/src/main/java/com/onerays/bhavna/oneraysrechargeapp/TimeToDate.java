package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeToDate {
    private Context context;
    public TimeToDate(Context context) {
        this.context=context;
    }

    //Code to get the date and time in the required and desired format,
    public String getDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = sdf.format(date);
        return strDate;
    }

    public String getTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss z");
        String time = sdf.format(date);
        return time;
    }
}
