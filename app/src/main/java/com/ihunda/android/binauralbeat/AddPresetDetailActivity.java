package com.ihunda.android.binauralbeat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.PresetModel;
import com.ihunda.android.binauralbeat.db.VoiceModel;

import java.util.ArrayList;

public class AddPresetDetailActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText etDescription;
    private EditText etAuthor;
    private Button btnDone;
    private String name;
    private PresetModel presetModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_preset_details);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etDescription = findViewById(R.id.etPresetDescription);
        etAuthor = findViewById(R.id.etPresetAuthor);
        btnDone = findViewById(R.id.btnDone);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("data")) {
                presetModel = getIntent().getParcelableExtra("data");
                if (presetModel != null) {
                    name = presetModel.getName();
                    etAuthor.setText(presetModel.getAuthor());
                    etDescription.setText(presetModel.getDescription());
                }
            }
        }
        getSupportActionBar().setTitle(name);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etDescription.getText().toString().trim().length() > 0) {
                    if (etAuthor.getText().toString().trim().length() > 0) {
                        try {
                            if (presetModel == null) {
                                presetModel = new PresetModel();
                            }
                            presetModel.setAuthor(etAuthor.getText().toString().trim());
                            presetModel.setDescription(etDescription.getText().toString().trim());
                            presetModel.setName(name);
                            ArrayList<PeriodModel> arrayList = presetModel.getPeriodModelArrayList();
                            if (arrayList != null && arrayList.size() > 0) {
                                for (int i = 0; i < arrayList.size(); i++) {
                                    JsonArray result = (JsonArray) new Gson().toJsonTree(arrayList.get(i).getVoiceModelArrayList(),
                                            new TypeToken<ArrayList<VoiceModel>>() {
                                            }.getType());
                                    arrayList.get(i).setVoiceModelArray(result.toString());
                                }
                                JsonArray result = (JsonArray) new Gson().toJsonTree(arrayList,
                                        new TypeToken<ArrayList<PeriodModel>>() {
                                        }.getType());
                                presetModel.setPeriodModelArray(result.toString());
                            }
                            ((BBeatApp) getApplicationContext()).getDbHelper().fillObject(PresetModel.class, presetModel);
                            Toast.makeText(AddPresetDetailActivity.this, getString(R.string.preset_saved), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddPresetDetailActivity.this, BBeat.class);
                            intent.putExtra("refresh", true);
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
