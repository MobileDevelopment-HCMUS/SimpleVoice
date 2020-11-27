package com.example.voicerecorder;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gauravk.audiovisualizer.visualizer.BlastVisualizer;
import com.visualizer.amplitude.AudioRecordView;

public class StandardFragment extends Fragment {

    MainActivity main;
    Context context;
    String message = "";

    LinearLayout standard_layout;
    ImageView imageView;



    public static StandardFragment newIntance(String strArg) {
        StandardFragment standardFragment = new StandardFragment();
        Bundle args = new Bundle();
        args.putString("strArg", strArg);
        standardFragment.setArguments(args);
        return standardFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            context = getActivity();
            main = (MainActivity) getActivity();


        } catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        standard_layout = (LinearLayout) inflater.inflate(R.layout.standard_layout, null);
        AudioRecordView audioRecordView  = standard_layout.findViewById(R.id.audioRecordView_standard);


        return standard_layout;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
