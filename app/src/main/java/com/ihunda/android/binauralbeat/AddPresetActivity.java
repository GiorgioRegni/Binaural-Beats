package com.ihunda.android.binauralbeat;

import android.app.Dialog;
import android.content.Intent;
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
import com.ihunda.android.binauralbeat.db.PresetModel;
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
    private Button btnDone;
    private int durationSeconds = 0;
    private int totalDurationSeconds = 0;
    private AddPresetAdapter addPresetAdapter;
    private int level = 1;
    private MultiLevelRecyclerView multiLevelRecyclerView;
    private PresetModel presetModel;

    private final int DEFAULT_PRESET_BUILDER_PERIOD_DURATION = 300;
    private final int DEFAULT_PRESET_BUILDER_PERIOD_VOLUME = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_preset_activity);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        arrayList = new ArrayList<>();

        tvTitle = mToolbar.findViewById(R.id.tvTitle);
        tvDuration = mToolbar.findViewById(R.id.tvDuration);
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("name")) {
                tvTitle.setText(getIntent().getStringExtra("name"));
            }
            if (getIntent().getExtras().containsKey("data")) {
                presetModel = getIntent().getParcelableExtra("data");
                if (presetModel != null) {
                    tvTitle.setText(presetModel.getName());
                    arrayList.addAll(presetModel.getPeriodModelArrayList());
                    if (arrayList != null && arrayList.size() > 0) {
                        for (int i = 0; i < arrayList.size(); i++) {
                            PeriodModel periodModel = (PeriodModel) arrayList.get(i);
                            if (periodModel.getVoiceModelArrayList() != null && periodModel.getVoiceModelArrayList().size() > 0) {
                                List<RecyclerViewItem> voiceModels = new ArrayList<>();
                                for (int j = 0; j < periodModel.getVoiceModelArrayList().size(); j++) {
                                    voiceModels.add(periodModel.getVoiceModelArrayList().get(j));
                                }
                                periodModel.addChildren(voiceModels);
                            }
                        }
                    }
                }
            }
        }
        if (totalDurationSeconds == 0) {
            tvDuration.setText(getString(R.string.duration) + " " + formatTime(totalDurationSeconds));
        }
        btnAddPeriod = findViewById(R.id.btnAddPeriod);
        btnAddPeriod.setOnClickListener(this);

        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        multiLevelRecyclerView = (MultiLevelRecyclerView) findViewById(R.id.rv_list);
        multiLevelRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        } else if (view == btnDone) {
            if (arrayList != null && arrayList.size() > 0) {
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).isExpanded()) {
                        multiLevelRecyclerView.toggleItemsGroup(i);
                    }
                }
                if (presetModel == null) {
                    presetModel = new PresetModel();
                    presetModel.setName(tvTitle.getText().toString());
                }
                ArrayList<PeriodModel> periodModelArrayList = new ArrayList<>();
                for (int i = 0; i < arrayList.size(); i++) {
                    periodModelArrayList.add((PeriodModel) arrayList.get(i));
                }
                presetModel.setPeriodModelArrayList(periodModelArrayList);
                Intent intent = new Intent(AddPresetActivity.this, AddPresetDetailActivity.class);
                intent.putExtra("data", presetModel);
                startActivity(intent);
            } else {
                Toast.makeText(AddPresetActivity.this, getString(R.string.preset_error), Toast.LENGTH_SHORT).show();
            }
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
        durationSeconds = DEFAULT_PRESET_BUILDER_PERIOD_DURATION;
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
        final Spinner spinnerVisualizer = dialog.findViewById(R.id.spinnerVisualizer);
        List<String> listVisualizer = new ArrayList<String>();
        listVisualizer.add("Aurora");
        listVisualizer.add("Black");
        listVisualizer.add("Flash");
        listVisualizer.add("GL Black");
        listVisualizer.add("Hiit");
        listVisualizer.add("Hypno Flash");
        listVisualizer.add("Hypnotic Spiral");
//        listVisualizer.add("Image");
        listVisualizer.add("Leds");
        listVisualizer.add("LSD");
        listVisualizer.add("Mandelbrot");
        listVisualizer.add("Morphine");
        listVisualizer.add("None");
        listVisualizer.add("Plasma");
        listVisualizer.add("Spiral Dots");
        listVisualizer.add("Star Field");
        listVisualizer.add("Star Field 3D");
        ArrayAdapter<String> dataAdapterVisualizer = new ArrayAdapter<String>(this,
                R.layout.row_spinner, listVisualizer);
        dataAdapterVisualizer.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spinnerVisualizer.setAdapter(dataAdapterVisualizer);
        ImageView ivArrowVisualizer = dialog.findViewById(R.id.ivArrowVisualizer);
        ivArrowVisualizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerVisualizer.performClick();
            }
        });
        final TextView tvVolume = dialog.findViewById(R.id.tvVolume);
        final SeekBar seekBar = dialog.findViewById(R.id.seekbar);
        final EditText etDuration = (EditText) dialog.findViewById(R.id.etDuration);
        etDuration.setText(formatTime(durationSeconds));

        seekBar.setProgress(DEFAULT_PRESET_BUILDER_PERIOD_VOLUME);

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
                int i = utils_EditTextTimeToInt(etDuration.getText().toString());
                durationSeconds = i;
                etDuration.setText(formatTime(durationSeconds));
                if (durationSeconds == 0) {
                    Toast.makeText(AddPresetActivity.this, getString(R.string.duration_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (position == -1) {
                    PeriodModel periodModel = new PeriodModel(1);
                    periodModel.setBackground(spinner.getSelectedItem().toString());
                    periodModel.setVisualizer(spinnerVisualizer.getSelectedItem().toString());
                    periodModel.setDuration(durationSeconds);
                    periodModel.setBackgroundVolume(tvVolume.getText().toString());
                    arrayList.add(periodModel);
                    refreshAdapter();
                    dialog.dismiss();
                } else {
                    ((PeriodModel) arrayList.get(position)).setBackground(spinner.getSelectedItem().toString());
                    ((PeriodModel) arrayList.get(position)).setVisualizer(spinnerVisualizer.getSelectedItem().toString());
                    ((PeriodModel) arrayList.get(position)).setDuration(durationSeconds);
                    ((PeriodModel) arrayList.get(position)).setBackgroundVolume(tvVolume.getText().toString());
                    refreshAdapter();
                    dialog.dismiss();
                }
            }
        });
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
            if (periodModel.getVisualizer().equalsIgnoreCase("Aurora")) {
                spinnerVisualizer.setSelection(0);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Black")) {
                spinnerVisualizer.setSelection(1);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Flash")) {
                spinnerVisualizer.setSelection(2);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("GL Black")) {
                spinnerVisualizer.setSelection(3);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Hiit")) {
                spinnerVisualizer.setSelection(4);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Hypno Flash")) {
                spinnerVisualizer.setSelection(5);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Hypnotic Spiral")) {
                spinnerVisualizer.setSelection(6);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Image")) {
                spinnerVisualizer.setSelection(7);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Leds")) {
                spinnerVisualizer.setSelection(8);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("LSD")) {
                spinnerVisualizer.setSelection(9);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Mandelbrot")) {
                spinnerVisualizer.setSelection(10);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Morphine")) {
                spinnerVisualizer.setSelection(11);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("None")) {
                spinnerVisualizer.setSelection(12);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Plasma")) {
                spinnerVisualizer.setSelection(13);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Spiral Dots")) {
                spinnerVisualizer.setSelection(14);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Star Field")) {
                spinnerVisualizer.setSelection(15);
            } else if (periodModel.getVisualizer().equalsIgnoreCase("Star Field 3D")) {
                spinnerVisualizer.setSelection(16);
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
        final EditText etNote = dialog.findViewById(R.id.etNote);
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
        dataAdapterEnd.setDropDownViewResource(R.layout.row_spinner_dropdown);
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

        final Spinner spinnerNote = dialog.findViewById(R.id.spinnerNote);
        List<String> listNote = new ArrayList<String>();
        listNote.add("A (4)");
        listNote.add("A# (4)");
        listNote.add("B (4)");
        listNote.add("C (4)");
        listNote.add("C# (4)");
        listNote.add("D (4)");
        listNote.add("D# (4)");
        listNote.add("E (4)");
        listNote.add("F (4)");
        listNote.add("F# (4)");
        listNote.add("G (4)");
        listNote.add("G# (4)");
        listNote.add("A (4)");
        listNote.add("A# (5)");
        listNote.add("B (5)");
        listNote.add("C (5)");
        ArrayAdapter<String> dataAdapterNote = new ArrayAdapter<String>(this, R.layout.row_spinner, listNote);
        dataAdapterNote.setDropDownViewResource(R.layout.row_spinner_dropdown);
        spinnerNote.setAdapter(dataAdapterNote);
        spinnerNote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    etNote.setText("432");
                } else if (i == 1) {
                    etNote.setText("458");
                } else if (i == 2) {
                    etNote.setText("485");
                } else if (i == 3) {
                    etNote.setText("514");
                } else if (i == 4) {
                    etNote.setText("544");
                } else if (i == 5) {
                    etNote.setText("577");
                } else if (i == 6) {
                    etNote.setText("611");
                } else if (i == 7) {
                    etNote.setText("647");
                } else if (i == 8) {
                    etNote.setText("686");
                } else if (i == 9) {
                    etNote.setText("727");
                } else if (i == 10) {
                    etNote.setText("770");
                } else if (i == 11) {
                    etNote.setText("816");
                } else if (i == 12) {
                    etNote.setText("864");
                } else if (i == 13) {
                    etNote.setText("915");
                } else if (i == 14) {
                    etNote.setText("970");
                } else if (i == 15) {
                    etNote.setText("1027");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ImageView ivArrowNote = dialog.findViewById(R.id.ivArrowNote);
        ivArrowNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerNote.performClick();
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
                if (etNote.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddPresetActivity.this, getString(R.string.end_freq_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (arrayList.get(parentPosition) instanceof PeriodModel) {
                    if (arrayList.get(parentPosition).isExpanded()) {
                        multiLevelRecyclerView.toggleItemsGroup(parentPosition);
                    }
                    PeriodModel periodModel = (PeriodModel) arrayList.get(parentPosition);
                    VoiceModel voiceModel = new VoiceModel(1);
                    voiceModel.setFreqEnd(Integer.parseInt(etEnd.getText().toString().trim()));
                    voiceModel.setFreqStart(Integer.parseInt(etStart.getText().toString().trim()));
                    voiceModel.setNote(Integer.parseInt(etNote.getText().toString().trim()));
                    voiceModel.setVolume(Integer.parseInt(tvVolume.getText().toString()));

                    List<RecyclerViewItem> voiceModels1 = new ArrayList<>();
                    ArrayList<VoiceModel> voiceModels = new ArrayList<>();
                    if (periodModel.getVoiceModelArrayList() != null) {
                        voiceModels = periodModel.getVoiceModelArrayList();
                    }
                    if (periodModel.getChildren() != null) {
                        voiceModels1 = periodModel.getChildren();
                    }
                    voiceModels.add(voiceModel);
                    voiceModels1.add(voiceModel);
                    periodModel.setVoiceModelArrayList(voiceModels);
                    periodModel.addChildren(voiceModels1);
                    arrayList.set(parentPosition, periodModel);
                    refreshAdapter();
                    if (!arrayList.get(parentPosition).isExpanded()) {
                        multiLevelRecyclerView.toggleItemsGroup(parentPosition);
                    }
                    dialog.dismiss();
                } else {
                    ((VoiceModel) arrayList.get(parentPosition)).setFreqEnd(Integer.parseInt(etEnd.getText().toString().trim()));
                    ((VoiceModel) arrayList.get(parentPosition)).setFreqStart(Integer.parseInt(etStart.getText().toString().trim()));
                    ((VoiceModel) arrayList.get(parentPosition)).setNote(Integer.parseInt(etNote.getText().toString().trim()));
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
            if (voiceModel.getNote() >= 1027) {
                spinnerNote.setSelection(15);
            } else if (voiceModel.getNote() >= 970) {
                spinnerNote.setSelection(14);
            } else if (voiceModel.getNote() >= 915) {
                spinnerNote.setSelection(13);
            } else if (voiceModel.getNote() >= 864) {
                spinnerNote.setSelection(12);
            } else if (voiceModel.getNote() >= 816) {
                spinnerNote.setSelection(11);
            } else if (voiceModel.getNote() >= 770) {
                spinnerNote.setSelection(10);
            } else if (voiceModel.getNote() >= 727) {
                spinnerNote.setSelection(9);
            } else if (voiceModel.getNote() >= 686) {
                spinnerNote.setSelection(8);
            } else if (voiceModel.getNote() >= 647) {
                spinnerNote.setSelection(7);
            } else if (voiceModel.getNote() >= 611) {
                spinnerNote.setSelection(6);
            } else if (voiceModel.getNote() >= 577) {
                spinnerNote.setSelection(5);
            } else if (voiceModel.getNote() >= 544) {
                spinnerNote.setSelection(4);
            } else if (voiceModel.getNote() >= 514) {
                spinnerNote.setSelection(3);
            } else if (voiceModel.getNote() >= 485) {
                spinnerNote.setSelection(2);
            } else if (voiceModel.getNote() >= 458) {
                spinnerNote.setSelection(1);
            } else {
                spinnerNote.setSelection(0);
            }
            tvVolume.setText("" + voiceModel.getVolume());
            seekBarVolume.setProgress(voiceModel.getVolume());
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
