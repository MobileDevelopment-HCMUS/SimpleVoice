package com.example.voicetest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.PlaybackParams;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivityTan extends Activity {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int INITIAL_REQUEST = 1401;

    private int silentBytes;
    private int sampleRate;

    Button btnStart, btnStop, btnPlay;
    EditText txtPitch, txtLoudness;

//    File myfile;
//
//    private VoiceRecorder mVoiceRecorder;
//    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {
//
//        DataOutputStream dataOutputStream;
//
//        @Override
//        public void onVoiceStart() {
//            myfile = new File(getApplicationContext().getFilesDir().getPath(), "test.pcm");
//            try {
//                myfile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            OutputStream outputStream = null;
//            try {
//                outputStream = new FileOutputStream(myfile);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
//
//            dataOutputStream = new DataOutputStream(bufferedOutputStream);
//        }
//
//        @Override
//        public void onVoice(byte[] data, int size) {
//            try {
//                dataOutputStream.write(data, 0, size);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @RequiresApi(api = Build.VERSION_CODES.M)
//        @Override
//        public void onVoiceEnd() {
//            if (mVoiceRecorder != null) {
//                mVoiceRecorder.pause();
//            }
//
//            try {
//                dataOutputStream.close();
//                playRecord();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    private RecordManager recordManager;
//    private PlaybackManager playbackManager;

    FunnyTalk funnyTalk;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        txtPitch = (EditText) findViewById(R.id.txtPitch);
        txtLoudness = (EditText) findViewById(R.id.txtLoudness);

//        recordManager = new RecordManager();
//        playbackManager = new PlaybackManager();
        funnyTalk=new FunnyTalk();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startVoiceRecorder();
               funnyTalk.startVoiceRecorder();

            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopVoiceRecorder();
                funnyTalk.stopVoiceRecorder();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                path="a";
            }
        });

    }


//    private void startVoiceRecorder() {
//        if (mVoiceRecorder != null) {
//            mVoiceRecorder.stop();
//        }
//        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
//        mVoiceRecorder.start();
//        sampleRate = mVoiceRecorder.getSampleRate();
//        silentBytes = (int) (0.5 * 2 * sampleRate);
//    }
//
//    private void stopVoiceRecorder() {
//        if (mVoiceRecorder != null) {
//            mVoiceRecorder.stop();
//            mVoiceRecorder = null;
//        }
//    }
//
//
//    private void configMediaRecorder(@NonNull MediaRecorder mr) {
//        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
//        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
//        mr.setOutputFile(getApplicationContext().getFilesDir().getPath() + "/test.mp3");
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    private void playRecord() throws IOException {
//
//        if (!myfile.exists()) {
//            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        int bufferSizeInBytes = (int) myfile.length();
//        byte[] audioData = new byte[bufferSizeInBytes];
//
//        InputStream inputStream = new FileInputStream(myfile);
//        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
//        DataInputStream dataInputStream = new DataInputStream(bufferedInputStream);
//
//        int j = 0;
//        while (dataInputStream.available() > 0) {
//            audioData[j] = dataInputStream.readByte();
//            j++;
//        }
//        dataInputStream.close();
//
//        int voiceBufferSizeInBytes = bufferSizeInBytes - silentBytes;
//
//        if (voiceBufferSizeInBytes <= 0) {
//            return;
//        }
//
//        AudioTrack audioTrack = new AudioTrack(3, sampleRate + 10000, 2, 2, voiceBufferSizeInBytes, 1);
//        audioTrack.setNotificationMarkerPosition(voiceBufferSizeInBytes / 2);
//        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
//            @Override
//            public void onMarkerReached(AudioTrack track) {
//                if (mVoiceRecorder != null) {
//                    mVoiceRecorder.resume();
//                }
//            }
//
//            @Override
//            public void onPeriodicNotification(AudioTrack track) {
//
//            }
//        });
//
//        audioTrack.play();
//        audioTrack.write(audioData, 0, voiceBufferSizeInBytes);
//
//
//    }
}