package com.onerays.bhavna.oneraysrechargeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    ImageView batman;
    TextView username;
    long mobileSession;
    SessionManager session;
    CoordinatorLayout cl;
    DbHandlersUsers db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        session = new SessionManager(this);

        session.checkLogin();

        db = new DbHandlersUsers(this);

        cl = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        batman = (ImageView)findViewById(R.id.imgProfile);
        username = (TextView)findViewById(R.id.userName);

        mobileSession = session.getMobile();

        //Getting mobile no of user and Image which was saved in shared preferences during registration or login
        String[] values = db.getData(mobileSession);
        username.setText("Welcome "+values[1]);

    }

    public void onButtonClicker(View v)
    {
        Intent intent;

        switch (v.getId()) {

            case R.id.recharge:
                intent = new Intent(this, RechargeMobile.class);
                startActivity(intent);
                break;

            case R.id.dth:
                intent = new Intent(this, Dth.class);
                startActivity(intent);
                break;

            case R.id.landline:
                intent = new Intent(this, LandlinePayListView.class);
                startActivity(intent);
                break;

            case R.id.addCard:
                intent = new Intent(this, AddCard.class);
                startActivity(intent);
                break;

            case R.id.profile:
                intent = new Intent(this, Account.class);
                startActivity(intent);
                break;

            case R.id.db:
                intent = new Intent(this, TransHistory.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    //Code for Overflow menu,
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.updateBalance:

                Intent in = new Intent(this, UpdateBalance.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                return true;

            case R.id.logout:
                session.logoutUser();

                //Code for deleting all the transactions
                // db.deleteAllData();
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout,menu);
        return true;
    }

    //Code for clicking two times on HomeScreen to exit,
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;

        Snackbar snackbar=Snackbar.make(cl,"Please click BACK again to exit",Snackbar.LENGTH_SHORT);
        View sv=snackbar.getView();
        sv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //snackbar.setActionTextColor(getResources().getColor(R.color.textColor));
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
