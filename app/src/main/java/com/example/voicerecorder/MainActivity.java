package com.example.voicerecorder;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainCallbacks {

    public static final String SCALE = "scale";
    public static final String OUTPUT_DIRECTORY = "VoiceRecorder";
    public static final String OUTPUT_FILENAME = "recorder.mp3";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    int scale = 50;

    int backContinue = 1;

    boolean clicked = false;
    boolean isRecording = false;
    boolean isStandardMode = true;
    long pauseOffset;
    String path;

    Toolbar toolbar;
    Button standardBtn, speech2textBtn;
    Chronometer currentPlayingRecordTime;
    GraphView graphView;
    List samples;

    FragmentTransaction ft;
    StandardFragment standardFragment;
    StartRecordFragment startRecordFragment;
    RecordingNavbarFragment recordingNavbarFragment;
    SpeechToTextFragment speechToTextFragment;

    RecordManager recordManager;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recordManager = new RecordManager();
        path = getApplicationContext().getFilesDir().getPath() + "/test.mp3";
        //create fragment start record
        ft = getSupportFragmentManager().beginTransaction();
        startRecordFragment = StartRecordFragment.newIntance("second_fragment");
        ft.replace(R.id.button_function_fragment, startRecordFragment);
        ft.commit();

        graphView = findViewById(R.id.graphView);
        standardBtn = findViewById(R.id.standard_button);
        speech2textBtn = (Button) findViewById(R.id.speech2text_button);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);
        currentPlayingRecordTime = findViewById(R.id.time_record);

        //set up the grapphView
        graphView.setMaxAmplitude(30000);
        graphView.setGraphColor(getColor(R.color.orange));
        graphView.setCanvasColor(getColor(R.color.white));
        graphView.setTimeColor(getColor(R.color.orange));
        graphView.setNeedleColor(getColor(R.color.orange));
        graphView.setMarkerColor(getColor(R.color.white));


        if (savedInstanceState != null) {
            scale = savedInstanceState.getInt(SCALE);
            graphView.setWaveLengthPX(scale);
            if (!recordManager.isRecording()) {
                samples = recordManager.getSamples();
                graphView.showFullGraph(samples);
            }
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListRecord.class);
                startActivity(intent);
                finish();
            }
        });

        standardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isStandardMode == false && isRecording == true) {

                    AlertDialog.Builder dlgAlert;
                    dlgAlert = new AlertDialog.Builder(MainActivity.this);
                    dlgAlert.setMessage("You are in speech to text record. Your record is not saved! Do you want to switch.");
                    dlgAlert.setTitle("Warning");
                    dlgAlert.setPositiveButton("Leave anyway", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Switch and dont save
                            startStandard();

                        }
                    });

                    dlgAlert.setNeutralButton("Save and leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Save and switch to speech to text
                            startStandard();
                        }
                    });

                    dlgAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stay

                        }

                    });

                    dlgAlert.create().show();

                } else {
                    startStandard();
                }
            }
        });

        speech2textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStandardMode == true && isRecording == true) {

                    AlertDialog.Builder dlgAlert;
                    dlgAlert = new AlertDialog.Builder(MainActivity.this);
                    dlgAlert.setMessage("You are in standard record. Your record is not saved! Do you want to switch.");
                    dlgAlert.setTitle("Warning");
                    dlgAlert.setPositiveButton("Leave anyway", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Switch and dont save
                            Speech2Text();
                            isStandardMode = false;
                        }
                    });

                    dlgAlert.setNeutralButton("Save and leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Save and switch to speech to text
                            Speech2Text();
                            isStandardMode = false;
                        }
                    });

                    dlgAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Stay

                        }

                    });

                    dlgAlert.create().show();

                } else {
                    startActivity(new Intent(MainActivity.this, SpeechToTextActivity.class));
                }

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SCALE, scale);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home_funnytalk: {
                Intent intent = new Intent(MainActivity.this, FunnyTalkLayout.class);
                startActivity(intent);
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //status 'recording'
        if (clicked) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your record is not saved!");
            dlgAlert.setTitle("Warning");
            dlgAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Luu lai và chuyen den activity listrecord

                    Intent intent = new Intent(MainActivity.this, ListRecord.class);
                    startActivity(intent);

                    finish();

                }
            });

            dlgAlert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startStandard();
                }

            });

            dlgAlert.create().show();
        }
        if (clicked == false && backContinue < 2) {
            //status 'do nothing' at mainActivity
            backContinue++;
            Toast.makeText(this, "Press BACK one more to exit.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (backContinue >= 2) {
            return super.onKeyDown(keyCode, event);
        }
        return false;
    }

    @Override
    public void onMsgFromFragmentToMain(String sender, String booleanstr) {
        if (sender.equals("READY_RECORD") == true) {

            if (booleanstr.equals("START") == true) {

                clicked = true;
                //create fragment recording button fragment
                ft = getSupportFragmentManager().beginTransaction();
                recordingNavbarFragment = RecordingNavbarFragment.newIntance("third_fragment");
                ft.replace(R.id.button_function_fragment, recordingNavbarFragment);
                ft.commit();

                isRecording = true;
                currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                currentPlayingRecordTime.start();

                try {
                    recordManager.setOutputFile(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                recordManager.startRecord();
                recordManager.startPlotting(graphView);
                samples = recordManager.getSamples();
                graphView.showFullGraph(samples);

            }

        }

        if (sender.equals("RECORDING") == true) {

            if (booleanstr.equals("STOP") == true) {

                isRecording = false;
                currentPlayingRecordTime.stop();
                pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();

                //Stop recording
                recordManager.stopRecord();
                graphView.stopPlotting();
                samples = recordManager.getSamples();
                graphView.showFullGraph(samples);

                Intent intent = new Intent(this, PauseRecord.class);
                startActivity(intent);
                finish();
            }
            if (booleanstr.equals("PLAY") == true) {

            }
            if (booleanstr.equals("PAUSE") == true) {

            }
        }
    }

//    public void zoomIn(View v) {
//        scale = scale + 1;
//        if (scale > 15) {
//            scale = 15;
//        }
//        graphView.setWaveLengthPX(scale);
//        if (!recordManager.isRecording()) {
//            samples = recordManager.getSamples();
//            graphView.showFullGraph(samples);
//        }
//    }
//
//    public void zoomOut(View v) {
//        scale = scale - 1;
//        if (scale < 2) {
//            scale = 2;
//        }
//        graphView.setWaveLengthPX(scale);
//        if (!recordManager.isRecording()) {
//            samples = recordManager.getSamples();
//            graphView.showFullGraph(samples);
//        }
//    }


    void startStandard() {

        standardBtn.setBackgroundColor(getResources().getColor(R.color.white));
        standardBtn.setTextColor(getResources().getColor(R.color.orange));

        speech2textBtn.setBackgroundResource(R.drawable.custom_convertbutton);
        speech2textBtn.setTextColor(getResources().getColor(R.color.white));

        isStandardMode = true;
        isRecording = false;
        currentPlayingRecordTime.stop();
        pauseOffset = 0;
        currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        //create fragment Standard fragment
        ft = getSupportFragmentManager().beginTransaction();
        standardFragment = StandardFragment.newIntance("first_fragment");
        ft.replace(R.id.mode_fragment, standardFragment);
        ft.commit();

        //create fragment start record
        ft = getSupportFragmentManager().beginTransaction();
        startRecordFragment = StartRecordFragment.newIntance("second_fragment");
        ft.replace(R.id.button_function_fragment, startRecordFragment);
        ft.commit();
        //xoa ban ghi tro ve trang chinh
        backContinue = 1;
        clicked = false;
    }

    void Speech2Text() {

        speech2textBtn.setBackgroundColor(getResources().getColor(R.color.white));
        speech2textBtn.setTextColor(getResources().getColor(R.color.orange));

        standardBtn.setBackgroundResource(R.drawable.custom_convertbutton);
        standardBtn.setTextColor(getResources().getColor(R.color.white));

        isStandardMode = false;
        isRecording = false;
        currentPlayingRecordTime.stop();
        pauseOffset = 0;
        currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);

        //create fragment Standard fragment
        ft = getSupportFragmentManager().beginTransaction();
        speechToTextFragment = SpeechToTextFragment.newIntance("fourth_fragment");
        ft.replace(R.id.mode_fragment, speechToTextFragment);
        ft.commit();


        //create fragment start record
        ft = getSupportFragmentManager().beginTransaction();
        startRecordFragment = StartRecordFragment.newIntance("second_fragment");
        ft.replace(R.id.button_function_fragment, startRecordFragment);
        ft.commit();
        clicked = false;
        backContinue = 1;
    }


}