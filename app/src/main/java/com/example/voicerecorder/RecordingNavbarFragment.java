package com.example.voicerecorder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecordingNavbarFragment extends Fragment {

    MainActivity main;
    Context context;


    LinearLayout recording_navbar;
    Button playBtn,stopBtn,pauseBtn;

    Animation bottomAnim;

    public static RecordingNavbarFragment newIntance(String strArg){
        RecordingNavbarFragment recordingNavbarFragment = new RecordingNavbarFragment();
        Bundle args = new Bundle();
        args.putString("strArg2",strArg);
        recordingNavbarFragment.setArguments(args);
        return recordingNavbarFragment;
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

        recording_navbar = (LinearLayout) inflater.inflate(R.layout.nav_bar_bottom_recording,null);

        //Animation
        bottomAnim = AnimationUtils.loadAnimation(main,R.anim.bottom_animation);


        playBtn = recording_navbar.findViewById(R.id.play_navbutton);
        stopBtn = recording_navbar.findViewById(R.id.stop_navbutton);
        pauseBtn = recording_navbar.findViewById(R.id.pause_navbutton);

       bottomAnim.setDuration(100);

        playBtn.setAnimation(bottomAnim);
        stopBtn.setAnimation(bottomAnim);
        pauseBtn.setAnimation(bottomAnim);

        playBtn.setEnabled(false);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.onMsgFromFragmentToMain("RECORDING","PLAY");
            }
        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.onMsgFromFragmentToMain("RECORDING","STOP");

            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                main.onMsgFromFragmentToMain("RECORDING","PAUSE");

            }
        });

        return recording_navbar;
    }

}
