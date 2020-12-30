package com.example.voicerecorder;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.widget.ProgressBar;

import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;

import java.io.File;
import java.io.IOException;

public class PlaybackManager {

    private static MediaPlayer mMediaPlayer;
    private static File file;
    private static Float pitch;
    private static Float speed;
    CircleLineVisualizer circleLineVisualizer;
    ProgressBar progressBar;

    private int duration = 0;

    public void setCircleLineVisualizer(CircleLineVisualizer circleLineVisualizer) {
        this.circleLineVisualizer = circleLineVisualizer;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }



    public static abstract class Callback {

        /**
         * Sau khi phát xong file
         */
        public void onCompletion() {
        }
    }

    private Callback mCallback;

    /**
     * Cài đặt file input phát âm
     *
     * @param path Đường dẫn tuyệt đối
     * @return true nếu file có tồn tại, false nếu file ko tồn tại
     */
    public boolean setOutputFile(String path) {
        boolean result;
        file = new File(path);
        result = file.exists();
        return result;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void startPlayback(Callback... callbacks) {
        if (mMediaPlayer != null) {
            return;
        }
        mCallback = (callbacks.length > 0) ? callbacks[0] : null;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    start();
                    progressBar.setMax(duration);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                while (getPosition() < progressBar.getMax()) {
                                    progressBar.setProgress(getPosition());
                                }
                            }catch (IllegalStateException e){
                                throw e;
                            }
                        }
                    }).start();


                    int audiosessionid = mMediaPlayer.getAudioSessionId();
                    if (audiosessionid != -1) {
                        circleLineVisualizer.setAudioSessionId(audiosessionid);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private synchronized void start() throws IOException {
        mMediaPlayer = new MediaPlayer();
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.setDataSource(file.getAbsolutePath());

        PlaybackParams playbackParams = new PlaybackParams();
        if (pitch != null) {
            playbackParams.setPitch(pitch);
        }

        if (speed != null) {
            playbackParams.setSpeed(speed);
        }
        mMediaPlayer.setPlaybackParams(playbackParams);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (mCallback != null) {
                    mCallback.onCompletion();
                }
                mMediaPlayer.release();
                mMediaPlayer = null;
            }

        });

        mMediaPlayer.prepare();
        mMediaPlayer.start();
        duration = mMediaPlayer.getDuration();
    }

    public void stopPlayback() {
        if (mCallback == null) {
            return;
        }
        mMediaPlayer.release();
        mMediaPlayer = null;
        duration = 0;
    }

    public void pausePlayback() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.pause();
    }

    public void resumePlayback() {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.start();
    }

    public int getDuration() {
        return duration;
    }

    public int getPosition() {
        if (mMediaPlayer != null) {
            try {
                return mMediaPlayer.getCurrentPosition();
            }catch (IllegalStateException e){
               return 0;
            }
        } else {
            return 0;
        }
    }

    private int secondsToSeek = 1;

    public void forwardSeek() {
        if (mMediaPlayer == null) {
            return;
        }
        int newPosition = mMediaPlayer.getCurrentPosition() + secondsToSeek * 1000;
        mMediaPlayer.seekTo(newPosition < duration ? newPosition : 0);
    }

    public void backwardSeek() {
        if (mMediaPlayer == null) {
            return;
        }
        int newPosition = mMediaPlayer.getCurrentPosition() - secondsToSeek * 1000;
        mMediaPlayer.seekTo(newPosition > 0 ? newPosition : 0);
    }

    public void setPosition(int position) {
        if (mMediaPlayer == null) {
            return;
        }
        mMediaPlayer.seekTo(position);
    }

}
