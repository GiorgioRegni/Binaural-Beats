package com.ihunda.android.binauralbeat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ihunda.android.binauralbeat.db.PresetModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class PresetListActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ArrayList<PresetModel> arrayList;
    private PresetListAdapter adapter;
    private RecyclerView recyclerView;
    private TextView emptyTv;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.preset_builder));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPeriodNameDialog();
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        emptyTv = findViewById(R.id.tvEmpty);
        try {
            arrayList = (ArrayList<PresetModel>) ((BBeatApp) getApplicationContext()).getDbHelper().getAll(PresetModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (arrayList != null && arrayList.size() > 0) {
            emptyTv.setVisibility(View.GONE);
            adapter = new PresetListAdapter(arrayList, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            emptyTv.setVisibility(View.VISIBLE);
        }
    }

    private void addPeriodNameDialog() {
        final Dialog dialog = new Dialog(PresetListActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_preset_name);
        final EditText etName = dialog.findViewById(R.id.etPeriodName);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().trim().length() > 0) {
                    Intent i = new Intent(PresetListActivity.this, AddPresetActivity.class);
                    i.putExtra("name", etName.getText().toString().trim());
                    startActivity(i);
                    dialog.dismiss();
                } else {
                    Toast.makeText(PresetListActivity.this, getString(R.string.name_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    public void edit(int adapterPosition) {
//        Intent intent = new Intent(PresetListActivity.this, AddPresetActivity.class);
//        intent.putExtra("data", arrayList.get(adapterPosition).getPeriodModelArrayList());
//        intent.putExtra("name", arrayList.get(adapterPosition).getName());
//        startActivity(intent);
    }

    public void delete(int adapterPosition) {
    }
}
