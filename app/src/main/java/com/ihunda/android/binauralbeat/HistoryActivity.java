package com.ihunda.android.binauralbeat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.HistoryModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<HistoryModel> arrayList;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyTv;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_nav_drawer);
        //drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

        recyclerView = findViewById(R.id.recyclerView);
        emptyTv = findViewById(R.id.tvEmpty);
        try {
            arrayList = (ArrayList<HistoryModel>) ((BBeatApp) getApplicationContext()).getDbHelper().getAll(HistoryModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (arrayList != null && arrayList.size() > 0) {
            emptyTv.setVisibility(View.GONE);
            adapter = new HistoryAdapter(arrayList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            emptyTv.setVisibility(View.VISIBLE);
        }
    }
}
