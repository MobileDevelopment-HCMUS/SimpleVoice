package com.example.voicerecorder;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SpeechToTextScreen extends AppCompatActivity {

    Toolbar speech_toolbar;

    Button languageButton, convertButton;

    TextView playingRecordName_Speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_to_text_screen);

        speech_toolbar = findViewById(R.id.SpeechToTextToolBar);

        languageButton = findViewById(R.id.languageButton);
        convertButton = findViewById(R.id.convertButton);
        playingRecordName_Speech = findViewById(R.id.playingRecordName_Speech);

        setSupportActionBar(speech_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SpeechToTextScreen.this, "Convert", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.speech_to_text_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.English: {
                languageButton.setText("ENG");
            }
            return true;
            case R.id.Vietnamese: {
                languageButton.setText("VIE");
            }
            return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}