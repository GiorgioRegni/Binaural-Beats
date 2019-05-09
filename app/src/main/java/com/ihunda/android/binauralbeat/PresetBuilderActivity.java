package com.ihunda.android.binauralbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                Intent i = new Intent(PresetBuilderActivity.this, AddPresetActivity.class);
                startActivity(i);
            }
        });
    }
}
