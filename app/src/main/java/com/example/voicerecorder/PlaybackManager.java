package com.example.voicerecorder;

import android.media.MediaPlayer;
import android.media.PlaybackParams;

import java.io.File;
import java.io.IOException;

public class PlaybackManager {

    private static MediaPlayer mMediaPlayer;
    private static File file;
    private static Integer pitch;

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

    public void setPitch(int pitch) {
        this.pitch = pitch;
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
    }

}
