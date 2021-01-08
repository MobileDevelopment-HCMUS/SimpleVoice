package com.example.voicerecorder;

import android.media.MediaRecorder;
import android.os.Build;
import android.os.VibrationEffect;
import android.util.Log;

import androidx.annotation.RequiresApi;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class RecordManager {

    private static MediaRecorder mMediaRecorder;
    private static File file;
    private List<WaveSample> pointList = new ArrayList<>();
    private long startTime = 0;
    private String outputFilePath;
    private volatile Boolean stop = false;
    private GraphView graphView;
    private Thread mRecordingThread;
    private float latitude = 0;
    private float longitude = 0;

    public static abstract class Callback {

        /**
         * Khi đạt giới hạn thời gian
         */
        public void onReachedMaxDuration() {
        }
    }

    private Callback mCallback;

    public boolean startPlotting(GraphView graphView) {
        if (graphView != null) {
            this.graphView = graphView;
            this.graphView.setMasterList(pointList);
            this.graphView.startPlotting();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the state of recording
     *
     * @return true if currently recording
     */
    public Boolean isRecording() {
        return mRecordingThread != null && mRecordingThread.isAlive();
    }

    /**
     * Returns last recorded audio samples
     *
     * @return {@link WaveSample list}
     */
    public List getSamples() {
        return pointList;
    }

    /**
     * Returns path of the output file
     *
     * @return Output file path
     */
    public String getOutputFilePath() {
        if (outputFilePath != null) {
            return outputFilePath;
        } else {
            return file.getAbsolutePath();
        }
    }

    /**
     * Tạo file output ghi âm
     *
     * @param path Đường dẫn tuyệt đối
     * @return true nếu tạo file mới, false nếu file đã tồn tại
     * @throws IOException
     */
    public boolean setOutputFile(String path) throws IOException {
        boolean result;
        outputFilePath = path;
        file = new File(path);
        result = file.createNewFile();
        return result;
    }

    public void startRecord(Callback... callbacks) {
        if (mMediaRecorder != null) {
            return;
        }
        mCallback = (callbacks.length > 0) ? callbacks[0] : null;
        pointList.clear();
        startTime = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (!stop) {
                    //Add current audio sample amplitude and timestamp
                    pointList.add(new WaveSample(System.currentTimeMillis() - startTime, mMediaRecorder.getMaxAmplitude()));
                    //pointList.removeIf()
                    try {
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();

    }

    private synchronized void start() throws IOException {
        mMediaRecorder = new MediaRecorder();
        if (mMediaRecorder == null) {
            return;
        }
        configMediaRecorder(mMediaRecorder);
        mMediaRecorder.prepare();
        mMediaRecorder.start();
    }

    private void configMediaRecorder(MediaRecorder mr) {
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mr.setOutputFile(file.getAbsolutePath());

        if(!(latitude == 0 && longitude == 0))
            mr.setLocation(latitude, longitude);
        mr.setMaxDuration(10800000); //3 hours = 10800000 ms
        mr.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mr, int what, int extra) {
                if (what==MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    mCallback.onReachedMaxDuration();
                }
            }
        });
        Log.d("LOC", String.valueOf(latitude));
    }

    public void stopRecord() {
        if (mMediaRecorder != null) {
            stop();
        }
        this.stop = true;

        if (graphView != null) {
            graphView.stopPlotting();
        }
    }

    private void stop() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

    public void setLocation(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
