package com.example.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SpeechToTextActivity extends AppCompatActivity {
    private ListView listView;
    private List<String> list;
    private TextView textView;
    private ArrayAdapter adapter;
    private static final int RECORD_Request_CODE = 101;
    private SpeechAPI speechAPI;
    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {

        @Override
        public void onVoiceStart() {
            if (speechAPI != null) {
                speechAPI.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (speechAPI != null) {
                speechAPI.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (speechAPI != null) {
                speechAPI.finishRecognizing();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text);
        textView = findViewById(R.id.textview);
        listView = findViewById(R.id.listview);
        list = new ArrayList<>();
        list.add("Google Cloud Platform");
        list.add("Speech To Text Api");
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setVerticalScrollBarEnabled(false);

        speechAPI = new SpeechAPI(this);
        ButterKnife.bind(this);
    }

    private int GrantedPermission(String permission)
    {
        return ContextCompat.checkSelfPermission(this, permission);
    }

    private void makeRequest(String permission)
    {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_Request_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RECORD_Request_CODE) {
            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            } else {
                //startVoiceRecorder();
            }
        }
    }

    private final SpeechAPI.Listener listener = new SpeechAPI.Listener() {
        @Override
        public void onSpeechRecognized(final String text, final boolean isFinal) {
            if(isFinal)
            {
                mVoiceRecorder.dismiss();
            }
            if(textView != null && !TextUtils.isEmpty(text))
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(isFinal)
                        {
                            textView.setText(null);
                            list.add(0, text);
                            adapter.notifyDataSetChanged();
                            listView.smoothScrollToPosition(0);
                            textView.setVisibility(View.GONE);
                        }
                        else {
                            textView.setText(text);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        }
    };

    private void startVoiceRecorder()
    {
        if(mVoiceRecorder != null)
        {
            mVoiceRecorder.stop();
        }
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startVoiceRecorder();
        } else {
            makeRequest(Manifest.permission.RECORD_AUDIO);
        }
        speechAPI.addListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        stopVoiceRecorder();

        // Stop Cloud Speech API
        speechAPI.removeListener(listener);
        speechAPI.destroy();
        speechAPI = null;

        super.onStop();
    }

    private int isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }
}