package com.ihunda.android.binauralbeat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PresetBuilderActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etName;
    private EditText etDescription;
    private EditText etAuthor;
    private Button btnNext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preset_builder);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.preset_builder));

        etName = findViewById(R.id.etPresetName);
        etDescription = findViewById(R.id.etPresetDescription);
        etAuthor = findViewById(R.id.etPresetAuthor);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPeriodNameDialog();
            }
        });
    }

    private void addPeriodNameDialog() {
        final Dialog dialog = new Dialog(PresetBuilderActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
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
                    Intent i = new Intent(PresetBuilderActivity.this, AddPresetActivity.class);
                    i.putExtra("name", etName.getText().toString().trim());
                    startActivity(i);
                    dialog.dismiss();
                } else {
                    Toast.makeText(PresetBuilderActivity.this, getString(R.string.name_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }
}
