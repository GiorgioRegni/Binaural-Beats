package com.ihunda.android.binauralbeat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mToolbar = (Toolbar) findViewById(R.id.historyToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
