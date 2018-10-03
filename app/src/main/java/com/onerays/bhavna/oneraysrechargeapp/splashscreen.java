package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class splashscreen extends AppCompatActivity {
    ImageView sp_imageview;
    SharedPreferences sp;
    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splashscreen );
        sp = getSharedPreferences(Constant.PREF,MODE_PRIVATE);
        sp_imageview = (ImageView) findViewById(R.id.sp);
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(4000);
        sp_imageview .startAnimation(animation);
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                if(sp.getString(Constant.ID,"").equalsIgnoreCase("")) {
                    Intent intent = new Intent(splashscreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Intent intent = new Intent(splashscreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
