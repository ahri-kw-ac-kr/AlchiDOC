package com.example.jms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragWeek extends Fragment {
    private View view;


    public static FragWeek newInstance(){
        FragWeek fragWeek = new FragWeek();
        return fragWeek;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_week, container, false);
        return view;
    }
}
