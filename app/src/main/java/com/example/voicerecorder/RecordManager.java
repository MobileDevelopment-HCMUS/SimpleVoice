package com.example.voicerecorder;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

public class RecordManager {

    private static MediaRecorder mMediaRecorder;
    private static File file;

    /**
     * Tạo file output ghi âm
     *
     * @param path Đường dẫn tuyệt đối
     * @return true nếu tạo file mới, false nếu file đã tồn tại
     * @throws IOException
     */
    public boolean setOutputFile(String path) throws IOException {
        boolean result;
        file = new File(path);
        result = file.createNewFile();
        return result;
    }

    public void startRecord() {
        if (mMediaRecorder != null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                } catch (IOException e) {
                    e.printStackTrace();
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
    }

    private void stop() {
        mMediaRecorder.stop();
        mMediaRecorder.release();
        mMediaRecorder = null;
    }

}
