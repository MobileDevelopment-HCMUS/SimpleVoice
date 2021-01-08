package com.example.voicerecorder;


import android.app.Dialog;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.File;
import java.io.IOException;

public class PlayingRecordScreen extends AppCompatActivity {

    Toolbar toolbar;
    Chronometer currentPlayingRecordTime, totalTime;
    Button repeatButton, prevSecondButton, nextSecondButton, playButton, settingButton, playingRecordVolumeButton;
    TextView playingRecordName;
    TextView newName;
    Button OKButton_Rename, CancelButton_Rename;
    TextView name, size, lastModified, bitRate, path;
    Button OKButton_Detail;
    SeekBar toneSeekBar, speedSeekBar;
    ProgressBar progressBar;
    Button OKButton_Setting, CancelButton_Setting;
    Button yesButton_Delete, noButton_Delete;

    CircleLineVisualizer circleLineVisualizer;

    boolean isInterupted = false;
    long pauseOffset;
    int TotalPlayingRecordTime = 5;
    public int currentSpeed = 2;
    public int currentTone = 3;

    boolean isVolume = true;
    boolean isRepeat = false;
    boolean isPlaying = false;
    String pathStr;

    Dialog dialog_Rename;
    Dialog dialog_Detail;
    Dialog dialog_Setting;
    Dialog dialog_Delete;

    PlaybackManager playbackManager;
    PlaybackManager.Callback callback = new PlaybackManager.Callback() {
        @Override
        public void onCompletion() {
            super.onCompletion();
            TotalPlayingRecordTime = playbackManager.getDuration();
            totalTime.setBase(SystemClock.elapsedRealtime() - TotalPlayingRecordTime);
            pauseOffset = 0;
            isPlaying = false;
            currentPlayingRecordTime.stop();
            playButton.setBackgroundResource(R.drawable.play);
            currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);

            if (isRepeat) {
                try {
                    isPlaying = true;
                    playbackManager.preparePlayback();
                    playbackManager.startPlayback(callback);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                settingButton.setEnabled(false);
                currentPlayingRecordTime.start();
            } else {
                settingButton.setEnabled(true);
            }

            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if (isInterupted) {
            if (isPlaying) {
                playbackManager.resumePlayback();
                currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                currentPlayingRecordTime.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isPlaying) {
            playbackManager.pausePlayback();
            currentPlayingRecordTime.stop();
            pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();

        }
        isInterupted = true;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_record_screen);


        playbackManager = new PlaybackManager();

        toolbar = findViewById(R.id.PlayingRecordToolBar);
        currentPlayingRecordTime = findViewById((R.id.currentPlayingRecordTime));
        totalTime = findViewById(R.id.totalPlayingRecordTime_Speech);
        repeatButton = findViewById(R.id.repeatButton);
        prevSecondButton = findViewById(R.id.prevSecondButton);
        nextSecondButton = findViewById(R.id.nextSecondButton);
        playButton = findViewById(R.id.playButton);
        settingButton = findViewById(R.id.settingButton);
        playingRecordVolumeButton = findViewById(R.id.playingRecordVolumeButton);
        playingRecordName = findViewById(R.id.playingRecordName);
        circleLineVisualizer = findViewById(R.id.blobVisualizer);
        progressBar = findViewById(R.id.progressBar);
        playbackManager.setProgressBar(progressBar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);

        pathStr = getApplicationContext().getFilesDir().getPath() + "/test.mp3";
        playbackManager.setOutputFile(pathStr);
        try {
            playbackManager.preparePlayback();
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        TotalPlayingRecordTime = playbackManager.getDuration();
        totalTime.setBase(SystemClock.elapsedRealtime() - TotalPlayingRecordTime);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayingRecordScreen.this, ListRecord.class);
                startActivity(intent);
                finish();
            }
        });

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
                playbackManager.backwardSeek();
            }
        });

        // Play Button OnClick
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying) {
                    if (pauseOffset == 0) {
                        playbackManager.stopPlayback();
                        playbackManager = new PlaybackManager();
                        playbackManager.setProgressBar(progressBar);

                        playbackManager.setOutputFile(pathStr);
                        try {
                            playbackManager.preparePlayback();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    if (pauseOffset != 0) {
                        playbackManager.resumePlayback();
                    }
                    playButton.setBackgroundResource(R.drawable.pause);
                    isPlaying = true;
                    playbackManager.setCircleLineVisualizer(circleLineVisualizer);
                    playbackManager.startPlayback(callback);

                    currentPlayingRecordTime.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    currentPlayingRecordTime.start();


                    progressBar.setProgress(0);
                    progressBar.setVisibility(View.VISIBLE);
                    settingButton.setEnabled(false);

                } else {
                    playButton.setBackgroundResource(R.drawable.play);
                    isPlaying = false;
                    currentPlayingRecordTime.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase();
                    playbackManager.pausePlayback();
                }

            }
        });

//        currentPlayingRecordTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
//            @Override
//            public void onChronometerTick(Chronometer chronometer) {
//                if ((SystemClock.elapsedRealtime() - currentPlayingRecordTime.getBase()) / 1000 == TotalPlayingRecordTime / 1000) {
//
//                }
//            }
//        });

        // Next Second Button OnClick
        nextSecondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingRecordScreen.this, "+1s", Toast.LENGTH_SHORT).show();
                playbackManager.forwardSeek();
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
                Log.d("Path: ", pathStr);
                Sharing.shareFile(PlayingRecordScreen.this, new File(pathStr));
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
                playbackManager.setPitch(1.0f + (currentTone - 3) * 0.2f);
                playbackManager.setSpeed(1.0f + (currentSpeed - 2) * 0.25f);
                try {
                    playbackManager.preparePlayback();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PlayingRecordScreen.this, ListRecord.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (circleLineVisualizer != null)
            circleLineVisualizer.release();
    }
}