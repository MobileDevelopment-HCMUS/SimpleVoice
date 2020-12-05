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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_funny_talk_layout);

        toolbar = findViewById(R.id.FunnyTalkToolBar);
        funnyTalkButton = findViewById(R.id.funnyTalkButton);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        funnyTalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPlaying){
                    funnyTalkButton.setBackgroundResource(R.drawable.pause);
                    isPlaying = true;
                }
                else
                {
                    funnyTalkButton.setBackgroundResource(R.drawable.play);
                    isPlaying = false;
                }
            }
        });
    }
}