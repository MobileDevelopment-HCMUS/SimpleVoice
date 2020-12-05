package com.example.voicerecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

public class FunnyTalkLayout extends AppCompatActivity {

    Toolbar toolbar;
    Button funnyTalkButton;
    boolean isPlaying = false;
    FunnyTalk funnyTalk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_talk_layout);

        toolbar = findViewById(R.id.FunnyTalkToolBar);
        funnyTalkButton = findViewById(R.id.funnyTalkButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        funnyTalk= new FunnyTalk();

        funnyTalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying){
                    funnyTalkButton.setBackgroundResource(R.drawable.pause);
                    isPlaying = true;
                    funnyTalk.startVoiceRecorder();
                }
                else
                {
                    funnyTalkButton.setBackgroundResource(R.drawable.play);
                    isPlaying = false;
                    funnyTalk.stopVoiceRecorder();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlaying=false;
        funnyTalk.stopVoiceRecorder();
    }
}