package com.ihunda.android.binauralbeat;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.HistoryModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class HistoryActivity extends Activity {
    private ArrayList<HistoryModel> arrayList;
    private HistoryAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
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
