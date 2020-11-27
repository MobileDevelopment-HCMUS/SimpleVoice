package com.example.voicerecorder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StartRecordFragment extends Fragment {
    MainActivity main;
    Context context;
    String message="";

    LinearLayout start_recorder;
    Button startBtn;

    public static StartRecordFragment newIntance(String strArg){
        StartRecordFragment startRecordFragment = new StartRecordFragment();
        Bundle args = new Bundle();
        args.putString("strArg1",strArg);
        startRecordFragment.setArguments(args);
        return startRecordFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            context = getActivity();
            main = (MainActivity) getActivity();
        }catch (IllegalStateException e){
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        start_recorder = (LinearLayout) inflater.inflate(R.layout.nav_bar_bottom_start,null);

        startBtn = start_recorder.findViewById(R.id.start_recording_button);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.onMsgFromFragmentToMain("READY_RECORD","START");

            }
        });

        return start_recorder;
    }
}
