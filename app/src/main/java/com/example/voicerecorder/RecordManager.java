package com.example.voicerecorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;
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

    public void startRecord() {
        if (mMediaRecorder != null) {
            return;
        }
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
        mMediaRecorder.setLocation(latitude, longitude);
    }

}
