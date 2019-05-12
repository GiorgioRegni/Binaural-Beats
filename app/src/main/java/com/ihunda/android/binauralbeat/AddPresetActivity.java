package com.ihunda.android.binauralbeat;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ihunda.android.binauralbeat.db.PeriodModel;
import com.ihunda.android.binauralbeat.db.VoiceModel;
import com.multilevelview.MultiLevelRecyclerView;
import com.multilevelview.models.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

public class AddPresetActivity extends AppCompatActivity implements View.OnClickListener {
    protected static final int LONGPRESS_INC = 1;
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView tvDuration;
    private ArrayList<RecyclerViewItem> arrayList;
    private Button btnAddPeriod;
    private int durationSeconds = 0;
    private int totalDurationSeconds = 0;
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

        tvTitle = mToolbar.findViewById(R.id.tvTitle);
        tvDuration = mToolbar.findViewById(R.id.tvDuration);
        if (getIntent().getExtras() != null) {
            tvTitle.setText(getIntent().getStringExtra("name"));
        }
        if (totalDurationSeconds == 0) {
            tvDuration.setText(getString(R.string.duration) + " " + formatTime(totalDurationSeconds));
        }
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
                setLevel();
                addPresetAdapter = new AddPresetAdapter(this, arrayList, multiLevelRecyclerView, this);
                multiLevelRecyclerView.setAdapter(addPresetAdapter);
                totalDurationSeconds = 0;
                for (int i = 0; i < arrayList.size(); i++) {
                    totalDurationSeconds = totalDurationSeconds + ((PeriodModel) arrayList.get(i)).getDuration();
                }
                tvDuration.setText(getString(R.string.duration) + " " + formatTime(totalDurationSeconds));
            }
        } else {
            setLevel();
            addPresetAdapter.notifyDataSetChanged();
            totalDurationSeconds = 0;
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i) instanceof PeriodModel) {
                    totalDurationSeconds = totalDurationSeconds + ((PeriodModel) arrayList.get(i)).getDuration();
                }
            }
            tvDuration.setText(getString(R.string.duration) + " " + formatTime(totalDurationSeconds));
        }
    }

    private void setLevel() {
        int periodCount = 0;
        int voiceCount = 0;
        if (arrayList != null && arrayList.size() > 0) {
            level = 1;
            for (int i = 0; i < arrayList.size(); i++) {
                arrayList.get(i).setLevel(level);
                level = level + 1;
                if (arrayList.get(i) instanceof PeriodModel) {
                    periodCount++;
                    voiceCount = 0;
                    ((PeriodModel) arrayList.get(i)).setLocalPosition(periodCount);
                    ArrayList<VoiceModel> voiceModelArrayList = ((PeriodModel) arrayList.get(i)).getVoiceModelArrayList();
                    if (voiceModelArrayList != null && voiceModelArrayList.size() > 0) {
                        int localVoiceCount = 0;
                        for (int j = 0; j < voiceModelArrayList.size(); j++) {
                            voiceModelArrayList.get(j).setLevel(level);
                            localVoiceCount++;
                            voiceModelArrayList.get(j).setLocalPosition(localVoiceCount);
                            level = level + 1;
                        }
                        ((PeriodModel) arrayList.get(i)).setVoiceModelArrayList(voiceModelArrayList);
                    }
                } else {
                    voiceCount++;
                    ((VoiceModel) arrayList.get(i)).setLocalPosition(voiceCount);

                }
            }
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
        tvVolume.setText("" + seekBar.getProgress());
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
                if (durationSeconds == 0) {
                    Toast.makeText(AddPresetActivity.this, getString(R.string.duration_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (position == -1) {
                    PeriodModel periodModel = new PeriodModel(1);
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

    public void showAddToPeriodDialog(final int parentPosition) {
        durationSeconds = 0;
        final Dialog dialog = new Dialog(AddPresetActivity.this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_add_bbv);
        final EditText etStart = dialog.findViewById(R.id.etFreqStart);
        final EditText etEnd = dialog.findViewById(R.id.etFreqEnd);
        final Spinner spinnerStart = dialog.findViewById(R.id.spinnerStart);
        List<String> list = new ArrayList<String>();
        list.add("Beta = 20 Hz");
        list.add("Alpha = 10Hz");
        list.add("Theta = 5 Hz");
        list.add("Delta = 1 Hz");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.row_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spinnerStart.setAdapter(dataAdapter);
        spinnerStart.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    etStart.setText("20");
                } else if (i == 1) {
                    etStart.setText("10");
                } else if (i == 2) {
                    etStart.setText("5");
                } else if (i == 3) {
                    etStart.setText("1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ImageView ivArrowStart = dialog.findViewById(R.id.ivArrowStart);
        ivArrowStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerStart.performClick();
            }
        });
        final Spinner spinnerEnd = dialog.findViewById(R.id.spinnerEnd);
        ArrayAdapter<String> dataAdapterEnd = new ArrayAdapter<String>(this,
                R.layout.row_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spinnerEnd.setAdapter(dataAdapterEnd);
        spinnerEnd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    etEnd.setText("20");
                } else if (i == 1) {
                    etEnd.setText("10");
                } else if (i == 2) {
                    etEnd.setText("5");
                } else if (i == 3) {
                    etEnd.setText("1");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ImageView ivArrowEnd = dialog.findViewById(R.id.ivArrowEnd);
        ivArrowEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerEnd.performClick();
            }
        });
        final TextView tvVolume = dialog.findViewById(R.id.tvVolume);
        final SeekBar seekBarVolume = dialog.findViewById(R.id.seekbarVolume);
        tvVolume.setText("" + seekBarVolume.getProgress());
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        final TextView tvPitch = dialog.findViewById(R.id.tvPitch);
        final SeekBar seekBarPitch = dialog.findViewById(R.id.seekbarPitch);
        tvPitch.setText("" + seekBarPitch.getProgress());
        seekBarPitch.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPitch.setText("" + i);
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
                if (etStart.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddPresetActivity.this, getString(R.string.start_freq_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etEnd.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddPresetActivity.this, getString(R.string.end_freq_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (arrayList.get(parentPosition) instanceof PeriodModel) {
                    multiLevelRecyclerView.toggleItemsGroup(parentPosition);
                    PeriodModel periodModel = (PeriodModel) arrayList.get(parentPosition);
                    VoiceModel voiceModel = new VoiceModel(1);
                    voiceModel.setFreqEnd(Integer.parseInt(etEnd.getText().toString().trim()));
                    voiceModel.setFreqStart(Integer.parseInt(etStart.getText().toString().trim()));
                    voiceModel.setPitch(Integer.parseInt(tvPitch.getText().toString()));
                    voiceModel.setVolume(Integer.parseInt(tvVolume.getText().toString()));

                    List<RecyclerViewItem> voiceModels1 = new ArrayList<>();
                    ArrayList<VoiceModel> voiceModels = new ArrayList<>();
                    if (periodModel.getVoiceModelArrayList() != null) {
                        voiceModels1 = periodModel.getChildren();
                        voiceModels = periodModel.getVoiceModelArrayList();
                    }
                    voiceModels.add(voiceModel);
                    voiceModels1.add(voiceModel);
                    periodModel.setVoiceModelArrayList(voiceModels);
                    periodModel.addChildren(voiceModels1);
                    arrayList.set(parentPosition, periodModel);
                    refreshAdapter();
                    multiLevelRecyclerView.toggleItemsGroup(parentPosition);
                    dialog.dismiss();
                } else {
                    ((VoiceModel) arrayList.get(parentPosition)).setFreqEnd(Integer.parseInt(etEnd.getText().toString().trim()));
                    ((VoiceModel) arrayList.get(parentPosition)).setFreqStart(Integer.parseInt(etStart.getText().toString().trim()));
                    ((VoiceModel) arrayList.get(parentPosition)).setPitch(Integer.parseInt(tvPitch.getText().toString()));
                    ((VoiceModel) arrayList.get(parentPosition)).setVolume(Integer.parseInt(tvVolume.getText().toString()));
                    refreshAdapter();
                    dialog.dismiss();
                }
            }
        });
        if (arrayList.get(parentPosition) instanceof VoiceModel) {
            VoiceModel voiceModel = (VoiceModel) arrayList.get(parentPosition);
            etStart.setText("" + voiceModel.getFreqStart());
            etEnd.setText("" + voiceModel.getFreqEnd());
            if (voiceModel.getFreqStart() >= 20) {
                spinnerStart.setSelection(0);
            } else if (voiceModel.getFreqStart() >= 10) {
                spinnerStart.setSelection(1);
            } else if (voiceModel.getFreqStart() >= 5) {
                spinnerStart.setSelection(2);
            } else if (voiceModel.getFreqStart() >= 1) {
                spinnerStart.setSelection(3);
            }
            if (voiceModel.getFreqEnd() >= 20) {
                spinnerEnd.setSelection(0);
            } else if (voiceModel.getFreqEnd() >= 10) {
                spinnerEnd.setSelection(1);
            } else if (voiceModel.getFreqEnd() >= 5) {
                spinnerEnd.setSelection(2);
            } else if (voiceModel.getFreqEnd() >= 1) {
                spinnerEnd.setSelection(3);
            }
            tvVolume.setText("" + voiceModel.getVolume());
            seekBarVolume.setProgress(voiceModel.getVolume());
            tvPitch.setText("" + voiceModel.getPitch());
            seekBarPitch.setProgress(voiceModel.getPitch());
            btnAdd.setText(getString(R.string.save));
        }
        dialog.show();
    }

    public void delete(int position) {
        if (arrayList.get(position) instanceof PeriodModel) {
            arrayList.remove(position);
            int tempPosition = position;
            while (tempPosition < arrayList.size()) {
                if (arrayList.get(tempPosition) instanceof VoiceModel) {
                    arrayList.remove(tempPosition);
                } else {
                    break;
                }
            }
        } else {
            int tempPosition = position;
            while (tempPosition > 0) {
                tempPosition--;
                if (arrayList.get(tempPosition) instanceof PeriodModel) {
                    for (int i = 0; i < ((PeriodModel) arrayList.get(tempPosition)).getVoiceModelArrayList().size(); i++) {
                        if (((PeriodModel) arrayList.get(tempPosition)).getVoiceModelArrayList().get(i) == arrayList.get(position)) {
                            ((PeriodModel) arrayList.get(tempPosition)).getVoiceModelArrayList().remove(i);
                            ((PeriodModel) arrayList.get(tempPosition)).getChildren().remove(i);
                        }
                    }
                    break;
                }
            }
            arrayList.remove(position);
        }
        refreshAdapter();
    }
}
