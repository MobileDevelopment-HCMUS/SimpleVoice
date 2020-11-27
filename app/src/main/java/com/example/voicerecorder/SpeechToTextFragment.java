package com.example.voicerecorder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SpeechToTextFragment extends Fragment {

    MainActivity main;
    Context context;


    LinearLayout speech2text;


    public static SpeechToTextFragment newIntance(String strArg){
        SpeechToTextFragment speechToTextFragment = new SpeechToTextFragment();
        Bundle args = new Bundle();
        args.putString("strArg3",strArg);
        speechToTextFragment.setArguments(args);
        return speechToTextFragment;
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

        speech2text = (LinearLayout) inflater.inflate(R.layout.speechtotext_layout,null);

        return speech2text;
    }

}
