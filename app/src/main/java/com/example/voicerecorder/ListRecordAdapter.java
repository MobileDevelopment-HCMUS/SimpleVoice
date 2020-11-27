package com.example.voicerecorder;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListRecordAdapter extends ArrayAdapter<Record> {

    Context context;
    int resource;
    ArrayList<Record> list;

    TextView nameRecord, timeRecord, sizeRecord, dateRecord;

    public ListRecordAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Record> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(R.layout.custom_list_record_item, null,true);

            nameRecord = row.findViewById(R.id.name_record);
            timeRecord = row.findViewById(R.id.time_record);
            dateRecord = row.findViewById(R.id.date_record);
            sizeRecord = row.findViewById(R.id.size_record);

            nameRecord.setText(list.get(position).getName());
            timeRecord.setText(String.valueOf(list.get(position).getTime())+" sec");
            dateRecord.setText(list.get(position).getDate().toString());
            sizeRecord.setText(String.valueOf(list.get(position).getSize())+" KB");

        }

        return row;
    }
}
