package com.example.voicerecorder;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PauseRecord extends AppCompatActivity {

    Toolbar toolbar;
    Button tickBtn, discardBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_record);

        tickBtn = findViewById(R.id.tick_button);
        discardBtn = findViewById(R.id.discard_button);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.menu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListReordActivity();
            }
        });

        tickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToListReordActivity();// save
            }
        });

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to MianActivity but unsaved
               backToMainActivity();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Your record is not saved!");
            dlgAlert.setTitle("Warning");
            dlgAlert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Save and back to ListRecord Activity
                   goToListReordActivity();
                }
            });

            dlgAlert.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   backToMainActivity();
                }

            });

            dlgAlert.create().show();

        }

        return false;

    }

    void backToMainActivity()
    {
        Intent intent = new Intent(PauseRecord.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    void goToListReordActivity(){
        Intent intent = new Intent(PauseRecord.this, ListRecord.class);
        startActivity(intent);
        finish();
    }
}