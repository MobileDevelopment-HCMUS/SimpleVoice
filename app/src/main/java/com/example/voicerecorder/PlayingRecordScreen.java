package com.example.voicerecorder;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PlayingRecordScreen extends AppCompatActivity {

    Toolbar toolbar;
    Chronometer currentPlayingRecordTime;
    Button repeatButton, prevSecondButton, nextSecondButton, playButton, settingButton, playingRecordVolumeButton;
    TextView playingRecordName;
    TextView newName;
    Button OKButton_Rename, CancelButton_Rename;
    TextView name, size, lastModified, bitRate, path;
    Button OKButton_Detail;
    SeekBar toneSeekBar, speedSeekBar;
    Button OKButton_Setting, CancelButton_Setting;
    Button yesButton_Delete, noButton_Delete;

    long pauseOffset;
    int TotalPlayingRecordTime = 5;
    public int currentSpeed = 2;
    public int currentTone = 3;

    boolean isVolume = true;
    boolean isRepeat = false;
    boolean isPlaying = false;
    String pathStr ;

    Dialog dialog_Rename;
    Dialog dialog_Detail;
    Dialog dialog_Setting;
    Dialog dialog_Delete;

    PlaybackManager playbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_record_screen);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
//        pathStr = bundle.getString("path");
        playbackManager = new PlaybackManager();
        pathStr = getApplicationContext().getFilesDir().getPath() + "/test.mp3";

        toolbar = findViewById(R.id.PlayingRecordToolBar);
        currentPlayingRecordTime = findViewById((R.id.currentPlayingRecordTime));
        repeatButton = findViewById(R.id.repeatButton);
        prevSecondButton = findViewById(R.id.prevSecondButton);
        nextSecondButton = findViewById(R.id.nextSecondButton);
        playButton = findViewById(R.id.playButton);
        settingButton = findViewById(R.id.settingButton);
        playingRecordVolumeButton = findViewById(R.id.playingRecordVolumeButton);
        playingRecordName = findViewById(R.id.playingRecordName);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Repeat Button OnClick
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRepeat) {

                    repeatButton.setBackgroundResource(R.drawable.repeat);
                    isRepeat = true;
                } else {
                    repeatButton.setBackgroundResource(R.drawable.unrepeat);
                    isRepeat = false;
                }
            }
        });

        // PrevSecond Button OnClick
        prevSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingRecordScreen.this, "-1s", Toast.LENGTH_SHORT).show();
            }
        });

        // Play Button OnClick
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    playButton.setBackgroundResource(R.drawable.pause);
                    isPlaying = true;

                    playbackManager.setOutputFile(pathStr);
                    playbackManager.startPlayback();
                    currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    currentPlayingRecordTime.start();
                } else {
                    playButton.setBackgroundResource(R.drawable.play);
                    isPlaying = false;
                    currentPlayingRecordTime.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();
                }
            }
        });

        currentPlayingRecordTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase()) / 1000 == TotalPlayingRecordTime) {
                    currentPlayingRecordTime.stop();
                    isPlaying = false;
                    playButton.setBackgroundResource(R.drawable.play);
                    pauseOffset = 0;
                }
            }
        });

        // Next Second Button OnClick
        nextSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingRecordScreen.this, "+1s", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Button OnClick
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewSettingContactDialog();
            }
        });

        // Playing Record Volume Button OnClick
        playingRecordVolumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVolume) {
                    playingRecordVolumeButton.setBackgroundResource(R.drawable.volume_outline);
                    isVolume = false;
                } else {
                    playingRecordVolumeButton.setBackgroundResource(R.drawable.volume_fill);
                    isVolume = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.playing_record_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editItem: {
                Toast.makeText(this, "Edit", Toast.LENGTH_SHORT).show();
            }
            return true;
            case R.id.renameItem: {
                createNewRenameContactDialog();
            }
            return true;
            case R.id.detailItem: {
                createNewDetailContactDialog();
            }
            return true;
            case R.id.shareItem: {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
            }
            return true;
            case R.id.convertSpeechToTextItem: {
                Intent i = new Intent(PlayingRecordScreen.this, SpeechToTextScreen.class);
                startActivity(i);
            }
            return true;
            case R.id.mouseToSecureFolderItem: {
                Toast.makeText(this, "Mouse To Secure Folder", Toast.LENGTH_SHORT).show();
            }
            return true;
            case R.id.deleteItem: {
                createNewDeleteContactDialog();
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createNewRenameContactDialog() {

        dialog_Rename = new Dialog(this);
        dialog_Rename.setContentView(R.layout.popup_rename);

        newName = dialog_Rename.findViewById(R.id.newName);
        OKButton_Rename = dialog_Rename.findViewById(R.id.OKButton_Rename);
        CancelButton_Rename = dialog_Rename.findViewById(R.id.CancelButton_Rename);

        OKButton_Rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newName.getText().toString().equals("")) {
                    Toast.makeText(PlayingRecordScreen.this, "isEmpty", Toast.LENGTH_SHORT).show();
                } else {
                    playingRecordName.setText(newName.getText().toString());
                    dialog_Rename.dismiss();
                }
            }
        });

        CancelButton_Rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingRecordScreen.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog_Rename.dismiss();
            }
        });

        dialog_Rename.show();
    }

    public void createNewDetailContactDialog() {

        dialog_Detail = new Dialog(this);
        dialog_Detail.setContentView(R.layout.popup_detail);

        name = dialog_Detail.findViewById(R.id.name);
        size = dialog_Detail.findViewById(R.id.size);
        lastModified = dialog_Detail.findViewById(R.id.lastModified);
        bitRate = dialog_Detail.findViewById(R.id.lastModified);
        path = dialog_Detail.findViewById(R.id.path);
        OKButton_Detail = dialog_Detail.findViewById(R.id.OKButton_Detail);

        OKButton_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_Detail.dismiss();
            }
        });

        dialog_Detail.show();
    }

    public void createNewSettingContactDialog() {

        dialog_Setting = new Dialog(this);
        dialog_Setting.setContentView(R.layout.popup_setting);

        speedSeekBar = dialog_Setting.findViewById(R.id.speedSeekBar);
        toneSeekBar = dialog_Setting.findViewById(R.id.toneSeekBar);
        OKButton_Setting = dialog_Setting.findViewById(R.id.OKButton_Setting);
        CancelButton_Setting = dialog_Setting.findViewById(R.id.CancelButton_Setting);

        speedSeekBar.setProgress(currentSpeed);
        toneSeekBar.setProgress(currentTone);

        OKButton_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSpeed = speedSeekBar.getProgress();
                currentTone = toneSeekBar.getProgress();
                dialog_Setting.dismiss();
            }
        });

        CancelButton_Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingRecordScreen.this, "Cancel", Toast.LENGTH_SHORT).show();
                dialog_Setting.dismiss();
            }
        });

        dialog_Setting.show();
    }

    public void createNewDeleteContactDialog() {
        dialog_Delete = new Dialog(this);
        dialog_Delete.setContentView(R.layout.popup_delete);

        yesButton_Delete = dialog_Delete.findViewById(R.id.yesButton_Delete);
        noButton_Delete = dialog_Delete.findViewById(R.id.noButton_Delete);

        yesButton_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_Delete.dismiss();
            }
        });

        noButton_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_Delete.dismiss();
            }
        });

        dialog_Delete.show();
    }
}