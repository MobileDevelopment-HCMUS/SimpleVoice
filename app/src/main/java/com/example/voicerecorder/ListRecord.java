package com.example.voicerecorder;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
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

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListRecord extends AppCompatActivity {

    Toolbar toolbar;
    TextView textView;
    ListView listViewRecord;

    ArrayList<Record> listRecord;

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);

        String path = getApplicationContext().getExternalFilesDir("/").getAbsolutePath();
        Log.d("Files", "Path:" +path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        listRecord = new ArrayList<Record>();


        //load data file
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

        for (int i = 0; i < files.length; i++)
        {
            /*String[] temp = files[i].getName().split("\\.");

            String fileName = temp[0];*/
            Date lastModDate = new Date(files[i].lastModified());

            Log.d("Files", "FileName:" + files[i].getName());
            int file_size = Integer.parseInt(String.valueOf(files[i].length()/1024));

            metaRetriever.setDataSource(files[i].getPath());
            String filePath = "";
            filePath = files[i].getPath();

            String time = "";
            String duration = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Log.d("time", duration);
            long dur = Long.parseLong(duration);
            String seconds = String.valueOf((dur%60000)/1000);

            Log.d("Seconds", seconds);
            String minutes = String.valueOf(dur/60000);


            if(seconds.length() == 1) {
                time = minutes + ":0" + seconds;
            } else {
                time = minutes + ":" + seconds;
            }
            Record tmp = new Record(files[i].getName(), time, filePath, lastModDate, file_size);

            String loc = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION);
            if(loc != null) Log.d("RAW", loc);

            if(loc != null) {
                String[] s = loc.split("(?=\\+|-|/)");
                Log.d("LEN", String.valueOf(s.length));
                Log.d("LOC", String.valueOf(s[0]));
                if(s.length >= 2 && s[0] != "" && s[1] != "") {
                    tmp.setLocation(Double.parseDouble(s[1]), Double.parseDouble(s[2]));
                    Log.d("LOC", s[1] + "," + s[2]);
                }
            }

            listRecord.add(tmp);
        }

        //close object
        metaRetriever.release();



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
                bundle.putSerializable("record", (Serializable) listRecord.get(position));
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