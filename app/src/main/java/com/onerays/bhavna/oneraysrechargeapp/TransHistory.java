package com.onerays.bhavna.oneraysrechargeapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TransHistory extends AppCompatActivity {

    private List<TransBean> transData = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransListAdapter tAdapter;
    private DbHandlersUsers db;
    private SessionManager sm;
    private long mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.subscription_list);

        sm = new SessionManager(TransHistory.this);
        mobile = sm.getMobile();
        db = new DbHandlersUsers(TransHistory.this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getTransDataFromDB();
    }

    private void getTransDataFromDB() {

        transData = db.getTrans(mobile);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        tAdapter = new TransListAdapter(getApplicationContext(),transData);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(tAdapter);
        tAdapter.notifyDataSetChanged();
    }

}
