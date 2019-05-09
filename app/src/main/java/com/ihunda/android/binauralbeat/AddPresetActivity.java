package com.ihunda.android.binauralbeat;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class AddPresetActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private ArrayList<RecyclerViewItem> arrayList;
    private Button btnAddPeriod;
    private int durationSeconds;
    protected static final int LONGPRESS_INC = 1;
    private AddPresetAdapter addPresetAdapter;
    private int level = 1;
    private MultiLevelRecyclerView multiLevelRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_preset_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(getString(R.string.preset_builder));

        btnAddPeriod = findViewById(R.id.btnAddPeriod);
        btnAddPeriod.setOnClickListener(this);

        multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        List<Item> itemList = (List<Item>) recursivePopulateFakeData(0, 12);
        arrayList = new ArrayList<>();

      /*  PeriodModel periodModel = new PeriodModel(1);
        periodModel.setBackground("bg");
        periodModel.setDuration(10);
        periodModel.setBackgroundVolume(11);
        periodModel.setViz("voz");

        VoiceModel voiceModel = new VoiceModel(2);
        voiceModel.setFreqEnd(10);
        voiceModel.setFreqStart(1);
        voiceModel.setPitch(11);
        voiceModel.setVolume(50);

        List<RecyclerViewItem> voiceModels1 = new ArrayList<>();
        ArrayList<VoiceModel> voiceModels = new ArrayList<>();
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);

//        PeriodModel periodModel1 = new PeriodModel();
//        periodModel1.setBackground("bg");
//        periodModel1.setDuration(10);
//        periodModel1.setBackgroundVolume(11);
//        periodModel1.setViz("voz");
//        List<RecyclerViewItem> voiceModels = new ArrayList<>();
//        List<PeriodModel> voiceModelsA = new ArrayList<>();
//        voiceModels.add(periodModel1);
//        voiceModels.add(periodModel1);
//        voiceModels.add(periodModel1);
//        voiceModelsA.add(periodModel1);
//        voiceModelsA.add(periodModel1);
//        voiceModelsA.add(periodModel1);

        periodModel.setVoiceModelArrayList(voiceModels);
        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
        arrayList.add(periodModel);
        periodModel = new PeriodModel(3);
        periodModel.setBackground("bg");
        periodModel.setDuration(10);
        periodModel.setBackgroundVolume(11);
        periodModel.setViz("voz");
        voiceModel = new VoiceModel(4);
        voiceModel.setFreqEnd(10);
        voiceModel.setFreqStart(1);
        voiceModel.setPitch(11);
        voiceModel.setVolume(50);
        voiceModels1 = new ArrayList<>();
        voiceModels = new ArrayList<>();
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        periodModel.setVoiceModelArrayList(voiceModels);
        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
        arrayList.add(periodModel);

        periodModel = new PeriodModel(5);
        periodModel.setBackground("bg");
        periodModel.setDuration(10);
        periodModel.setBackgroundVolume(11);
        periodModel.setViz("voz");
        voiceModel = new VoiceModel(6);
        voiceModel.setFreqEnd(10);
        voiceModel.setFreqStart(1);
        voiceModel.setPitch(11);
        voiceModel.setVolume(50);
        voiceModels1 = new ArrayList<>();
        voiceModels = new ArrayList<>();
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        periodModel.setVoiceModelArrayList(voiceModels);
        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
        arrayList.add(periodModel);
        periodModel = new PeriodModel(7);
        periodModel.setBackground("bg");
        periodModel.setDuration(10);
        periodModel.setBackgroundVolume(11);
        periodModel.setViz("voz");
        voiceModel = new VoiceModel(8);
        voiceModel.setFreqEnd(10);
        voiceModel.setFreqStart(1);
        voiceModel.setPitch(11);
        voiceModel.setVolume(50);
        voiceModels1 = new ArrayList<>();
        voiceModels = new ArrayList<>();
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        periodModel.setVoiceModelArrayList(voiceModels);
        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
        arrayList.add(periodModel);
        periodModel = new PeriodModel(9);
        periodModel.setBackground("bg");
        periodModel.setDuration(10);
        periodModel.setBackgroundVolume(11);
        periodModel.setViz("voz");
        voiceModel = new VoiceModel(10);
        voiceModel.setFreqEnd(10);
        voiceModel.setFreqStart(1);
        voiceModel.setPitch(11);
        voiceModel.setVolume(50);
        voiceModels1 = new ArrayList<>();
        voiceModels = new ArrayList<>();
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        voiceModels1.add(voiceModel);
        periodModel.setVoiceModelArrayList(voiceModels);
        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
        arrayList.add(periodModel); */
        refreshAdapter();
        //If you are handling the click on your own then you can
        // multiLevelRecyclerView.removeItemClickListeners();
        multiLevelRecyclerView.setToggleItemOnClick(false);
        multiLevelRecyclerView.setAccordion(false);
//        multiLevelRecyclerView.openTill(0, 1, 2, 3);
    }

    private void refreshAdapter() {
        if (addPresetAdapter == null) {
            if (arrayList != null && arrayList.size() > 0) {
                addPresetAdapter = new AddPresetAdapter(this, arrayList, multiLevelRecyclerView, this);
                multiLevelRecyclerView.setAdapter(addPresetAdapter);
            }
        } else {
            addPresetAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnAddPeriod) {
            showAddEditPeriodDialog(-1);
        }
    }

    private int utils_EditTextTimeToInt(String v) {
        int ret = 0;

        try {
            String[] tokens = v.split(":");
            if (tokens.length == 1) {
                ret = Integer.parseInt(tokens[0]);
            } else if (tokens.length == 2) {
                int minutes = Integer.parseInt(tokens[0]);
                int seconds = Integer.parseInt(tokens[1]);
                ret = seconds + minutes * 60;
            } else if (tokens.length == 3) {
                int hours = Integer.parseInt(tokens[0]);
                int minutes = Integer.parseInt(tokens[1]);
                int seconds = Integer.parseInt(tokens[2]);
                ret = seconds + minutes * 60 + hours * 3600;
            }
        } catch (NumberFormatException e) {
        }

        return ret;
    }

    private String formatTime(int timeInSecs) {
        int minutes = timeInSecs / 60;
        return String.format("%s:%s",
                formatTimeNumberwithLeadingZero(minutes),
                formatTimeNumberwithLeadingZero(timeInSecs - minutes * 60));
    }

    private String formatTimeNumberwithLeadingZero(int t) {
        if (t > 9)
            return String.format("%2d", t);
        else
            return String.format("0%1d", t);
    }


    public void showAddEditPeriodDialog(final int position) {
        durationSeconds = 0;
        final Dialog dialog = new Dialog(AddPresetActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_period);
        final Spinner spinner = dialog.findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("None");
        list.add("White Noise");
        list.add("Unity");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.row_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spinner.setAdapter(dataAdapter);
        ImageView ivArrow = dialog.findViewById(R.id.ivArrow);
        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });
        final TextView tvVolume = dialog.findViewById(R.id.tvVolume);
        final SeekBar seekBar = dialog.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvVolume.setText("" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
                if (position == -1) {
                    PeriodModel periodModel = new PeriodModel(level);
                    periodModel.setBackground(spinner.getSelectedItem().toString());
                    periodModel.setDuration(durationSeconds);
                    periodModel.setBackgroundVolume(tvVolume.getText().toString());
//                    if (level == 1) {
//                        VoiceModel voiceModel = new VoiceModel(2);
//                        voiceModel.setFreqEnd(10);
//                        voiceModel.setFreqStart(1);
//                        voiceModel.setPitch(11);
//                        voiceModel.setVolume(50);
//
//                        List<RecyclerViewItem> voiceModels1 = new ArrayList<>();
//                        ArrayList<VoiceModel> voiceModels = new ArrayList<>();
//                        voiceModels.add(voiceModel);
//                        voiceModels.add(voiceModel);
//                        voiceModels.add(voiceModel);
//                        voiceModels.add(voiceModel);
//                        voiceModels1.add(voiceModel);
//                        voiceModels1.add(voiceModel);
//                        voiceModels1.add(voiceModel);
//                        voiceModels1.add(voiceModel);
//                        periodModel.setVoiceModelArrayList(voiceModels);
//                        periodModel.addChildren((List<RecyclerViewItem>) voiceModels1);
//                    }
                    arrayList.add(periodModel);
                    level = level + 1;
                    refreshAdapter();
                    dialog.dismiss();
                } else {
                    ((PeriodModel) arrayList.get(position)).setBackground(spinner.getSelectedItem().toString());
                    ((PeriodModel) arrayList.get(position)).setDuration(durationSeconds);
                    ((PeriodModel) arrayList.get(position)).setBackgroundVolume(tvVolume.getText().toString());
                    refreshAdapter();
                    dialog.dismiss();
                }
            }
        });
        // Number of lead in time

        final EditText etDuration = (EditText) dialog.findViewById(R.id.etDuration);
        etDuration.setOnFocusChangeListener(new OnEditTextFocusChange() {
            @Override
            void onTextChange(EditText v, String text) {
                int i = utils_EditTextTimeToInt(text);
                durationSeconds = i;
                etDuration.setText(formatTime(durationSeconds));
            }
        });
        etDuration.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    int i = utils_EditTextTimeToInt(etDuration.getText().toString());
                    durationSeconds = i;
                    etDuration.setText(formatTime(durationSeconds));
                    return true;
                }
                return false;
            }
        });
        ImageView ivPlus = dialog.findViewById(R.id.ivPlus);

        ivPlus.setOnTouchListener(new OnTouchContinuousListener() {
            @Override
            public void onTouchRepeat(View view, boolean longpress) {

                durationSeconds += longpress ? LONGPRESS_INC : 1;
                etDuration.setText(formatTime(durationSeconds));
            }
        });

        ImageView ivMinus = dialog.findViewById(R.id.ivMinus);
        ivMinus.setOnTouchListener(new OnTouchContinuousListener() {
            @Override
            public void onTouchRepeat(View view, boolean longpress) {
                durationSeconds = Math.max(0, durationSeconds - (longpress ? LONGPRESS_INC : 1));
                etDuration.setText(formatTime(durationSeconds));
            }
        });
        if (position != -1) {
            PeriodModel periodModel = (PeriodModel) arrayList.get(position);
            durationSeconds = periodModel.getDuration();
            etDuration.setText(formatTime(durationSeconds));
            if (periodModel.getBackground().equalsIgnoreCase("None")) {
                spinner.setSelection(0);
            } else if (periodModel.getBackground().equalsIgnoreCase("White Noise")) {
                spinner.setSelection(1);
            } else if (periodModel.getBackground().equalsIgnoreCase("Unity")) {
                spinner.setSelection(2);
            }
            tvVolume.setText("" + periodModel.getBackgroundVolume());
            seekBar.setProgress(Integer.valueOf(periodModel.getBackgroundVolume()));
            btnAdd.setText(getString(R.string.save));
        }
        dialog.show();
    }
}
