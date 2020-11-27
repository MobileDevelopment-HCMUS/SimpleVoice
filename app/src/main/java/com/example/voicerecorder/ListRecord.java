package com.example.voicerecorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;

public class ListRecord extends AppCompatActivity {

    Toolbar toolbar;
    TextView textView;
    ListView listViewRecord;

    ArrayList<Record> listRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);

        listRecord = new ArrayList<Record>();
        Date date = new Date();
        listRecord.add(new Record("Voice01", 20, date, 500));
        listRecord.add(new Record("Voice02", 30, date, 500));
        listRecord.add(new Record("Voice03", 16, date, 500));
        listRecord.add(new Record("Voice04", 11, date, 500));
        listRecord.add(new Record("Voice05", 19, date, 500));
        listRecord.add(new Record("Voice06", 33, date, 500));
        listRecord.add(new Record("Voice07", 12, date, 500));
        listRecord.add(new Record("Voice08", 20, date, 500));


        listViewRecord = findViewById(R.id.lis_recording);
        textView = findViewById(R.id.titlte_activity);
        textView.setText("Recording List");

        ListRecordAdapter adapter = new ListRecordAdapter(this, R.layout.custom_list_record_item, listRecord);
        listViewRecord.setAdapter(adapter);

        toolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRecord.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        listViewRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path="";
                Bundle bundle =new Bundle();
                bundle.putString("path",path);
                Intent intent = new Intent(ListRecord.this,PlayingRecordScreen.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listrecord_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.list_share_setting:


                break;
            case R.id.list_delete_setting:


                break;
            case R.id.list_setting_setting:


                break;
            case R.id.list_about_setting:


                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(ListRecord.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}