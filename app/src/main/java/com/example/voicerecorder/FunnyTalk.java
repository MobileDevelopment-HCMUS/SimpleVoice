package com.example.voicerecorder;

import android.media.AudioTrack;
import android.widget.Toast;

import com.example.voicerecorder.VoiceRecorder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.stream.Stream;

public class FunnyTalk {

    private ByteArrayOutputStream byteArrayOutputStream;

    private VoiceRecorder mVoiceRecorder;

    private int silentBytes;
    private int sampleRate;

    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        DataOutputStream dataOutputStream;

        @Override
        public void onVoiceStart() {

            byteArrayOutputStream=new ByteArrayOutputStream();
            dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        }

        @Override
        public void onVoice(byte[] data, int size) {
            try {
                dataOutputStream.write(data, 0, size);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onVoiceEnd() {
            if (mVoiceRecorder != null) {
                mVoiceRecorder.pause();
            }

            try {
                dataOutputStream.close();
                byteArrayOutputStream.close();
                playRecord();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public synchronized void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
        sampleRate = mVoiceRecorder.getSampleRate();
        silentBytes = (int) (0.5 * 2 * sampleRate);
    }

    public void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    private void playRecord() {

        byte[] audioData=byteArrayOutputStream.toByteArray();
        int bufferSizeInBytes=audioData.length;
        int voiceBufferSizeInBytes = bufferSizeInBytes - silentBytes;

        if (voiceBufferSizeInBytes <= 0) {
            return;
        }

        AudioTrack audioTrack = new AudioTrack(3, sampleRate+12000, 2, 2, voiceBufferSizeInBytes, 1);
        audioTrack.setNotificationMarkerPosition(voiceBufferSizeInBytes / 2);
        audioTrack.setPlaybackPositionUpdateListener(new AudioTrack.OnPlaybackPositionUpdateListener() {
            @Override
            public void onMarkerReached(AudioTrack track) {
                if (mVoiceRecorder != null) {
                    mVoiceRecorder.resume();
                }
            }

            @Override
            public void onPeriodicNotification(AudioTrack track) {

            }
        });

        audioTrack.play();
        audioTrack.write(audioData, 0, voiceBufferSizeInBytes);

    }
}
