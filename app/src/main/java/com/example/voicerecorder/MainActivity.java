package com.example.voicerecorder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity implements MainCallbacks {

    public static final String SCALE = "scale";
    public static final String OUTPUT_DIRECTORY = "VoiceRecorder";
    public static final String OUTPUT_FILENAME = "recorder.mp3";
    private static final int MY_PERMISSIONS_REQUEST_CODE = 0;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int INITIAL_REQUEST = 1401;
    private static final String TAG = MainActivity.class.getSimpleName();
    int scale = 50;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation = null;

    int backContinue = 1;

    boolean recorded = false;
    boolean clicked = false;
    boolean isRecording = false;
    boolean isStandardMode = true;
    int isInterupted = 0;
    long pauseOffset = 0;
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

    @Override
    protected void onResume() {
        //Toast.makeText(this, "OnResume", Toast.LENGTH_SHORT).show();

        super.onResume();

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestRecordPermission();
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        recordManager = new RecordManager();
        path = MainActivity.this.getExternalFilesDir("/").getAbsolutePath() + "/temp.mp3";
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


//        if (savedInstanceState != null) {
//            scale = savedInstanceState.getInt(SCALE);
//            graphView.setWaveLengthPX(scale);
//            if (!recordManager.isRecording()) {
//                samples = recordManager.getSamples();
//                graphView.showFullGraph(samples);
//            }
////            int isInterupted = savedInstanceState.getInt("isInterupted");
////            if (isInterupted == 1) {
////                long pause = savedInstanceState.getLong("pauseOffset");
////                String filePath = savedInstanceState.getString("filePath");
////
////                Intent intent = new Intent(this, PauseRecord.class);
////                intent.putExtra("time", pause);
////                intent.putExtra("filePath", filePath);
////                startActivity(intent);
////                finish();
////            }
//        }

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
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            askLocationPermission();
            Log.d(TAG, "This is Permission request");
        }

        if (isInterupted == 1 && recorded) {

            Intent intent = new Intent(this, PauseRecord.class);
            intent.putExtra("time", pauseOffset);
            intent.putExtra("filePath", path);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRecording = false;
        currentPlayingRecordTime.stop();
        pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();

        //Stop recording
        recordManager.stopRecord();
        graphView.stopPlotting();
        samples = recordManager.getSamples();
        graphView.showFullGraph(samples);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        isInterupted = 1;
        outState.putInt(SCALE, scale);
        //outState.putInt("isInterupted", isInterupted);
        //outState.putString("filePath", path);
        //outState.putLong("pauseOffset", pauseOffset);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onMsgFromFragmentToMain(String sender, String booleanstr) {
        if (sender.equals("READY_RECORD") == true) {

            if (booleanstr.equals("START") == true) {

                startRecordDisplay();
            }

        }

        if (sender.equals("RECORDING") == true) {

            if (booleanstr.equals("STOP") == true) {
                stopRecordDisplay();
            }
            if (booleanstr.equals("PLAY") == true) {

            }
            if (booleanstr.equals("PAUSE") == true) {

            }
        }
    }

    RecordManager.Callback recordCallback= new RecordManager.Callback() {
        @Override
        public void onReachedMaxDuration() {
            super.onReachedMaxDuration();
            stopRecordDisplay();
        }
    };

    private void stopRecordDisplay() {
        if (isRecording) {
            pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();
        }
        isRecording = false;
        currentPlayingRecordTime.stop();


        //Stop recording
        recordManager.stopRecord();
        graphView.stopPlotting();
        samples = recordManager.getSamples();
        graphView.showFullGraph(samples);

        Intent intent = new Intent(this, PauseRecord.class);
        intent.putExtra("time", pauseOffset);
        startActivity(intent);
        finish();
    }

    private void startRecordDisplay() {
        recorded = true;
        clicked = true;

        //create fragment recording button fragment
        ft = getSupportFragmentManager().beginTransaction();
        recordingNavbarFragment = RecordingNavbarFragment.newIntance("third_fragment");
        ft.replace(R.id.button_function_fragment, recordingNavbarFragment);
        ft.commit();

        isRecording = true;
        if (recorded) {
            currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }
        recordManager = new RecordManager();
        currentPlayingRecordTime.start();

        try {
            recordManager.setOutputFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getLocation();

        recordManager.startRecord(recordCallback);

        recordManager.startPlotting(graphView);
        samples = recordManager.getSamples();
        graphView.showFullGraph(samples);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        if (checkPermissions()) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null) {
                      recordManager.setLocation((float) location.getLatitude(), (float) location.getLongitude());
                      Log.d(TAG, location.toString());
                      Log.d(TAG, "Get location successful");
                    }
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Cannot get location");
                }
            });


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

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkRecordPermissions(String perm) {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                perm);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//            showSnackbar(R.string.permission_rationale,
//                    android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_PERMISSIONS_REQUEST_CODE);
//                        }
//                    });
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }

    public void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: alertbox here");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public void requestRecordPermission() {
        ArrayList<String> perms=new ArrayList<String>();
        for (String perm: INITIAL_PERMS) {
            if (!checkRecordPermissions(perm)) {
                perms.add(perm);
            }
        }
        ActivityCompat.requestPermissions(MainActivity.this, perms.toArray(new String[]{}), INITIAL_REQUEST);

    }
}
