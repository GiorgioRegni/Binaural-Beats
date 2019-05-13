package com.ihunda.android.binauralbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.PresetModel;

import java.util.ArrayList;

public class AddPresetDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etDescription;
    private EditText etAuthor;
    private Button btnDone;
    private String name;
    private ArrayList<PeriodModel> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preset_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            name = getIntent().getStringExtra("name");
            arrayList = (ArrayList<PeriodModel>) getIntent().getSerializableExtra("data");
        }
        getSupportActionBar().setTitle(name);

        etDescription = findViewById(R.id.etPresetDescription);
        etAuthor = findViewById(R.id.etPresetAuthor);
        btnDone = findViewById(R.id.btnDone);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDescription.getText().toString().trim().length() > 0) {
                    if (etAuthor.getText().toString().trim().length() > 0) {
                        try {
                            PresetModel presetModel = new PresetModel();
                            presetModel.setAuthor(etAuthor.getText().toString().trim());
                            presetModel.setDescription(etDescription.getText().toString().trim());
                            presetModel.setName(name);
                            presetModel.setPeriodModelArrayList(arrayList);
                            ((BBeatApp) getApplicationContext()).getDbHelper().fillObject(PresetModel.class, presetModel);
                            Toast.makeText(AddPresetDetailActivity.this, getString(R.string.preset_saved), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPresetDetailActivity.this, BBeat.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(AddPresetDetailActivity.this, getString(R.string.author_error), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddPresetDetailActivity.this, getString(R.string.description_error), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
