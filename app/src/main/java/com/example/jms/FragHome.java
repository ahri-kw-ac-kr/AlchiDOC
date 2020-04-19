package com.example.jms;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jms.R;

import java.util.Random;

import cn.nightcode.sliderIndicator.SliderIndicator;

public class FragHome extends Fragment {
    private ViewPager mainViewPager;
    private SliderIndicator indicator;
    private com.example.jms.SamplePagerAdapter pagerAdapter;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        mainViewPager = view.findViewById(R.id.main_view_pager);
        pagerAdapter = new com.example.jms.SamplePagerAdapter(container.getContext());
        mainViewPager.setAdapter(pagerAdapter);

        indicator = view.findViewById(R.id.main_slide_indicator);
        pagerAdapter.setCount(5); //나중에 이 부분을 보호자 숫자대로 바꿔야함..
        indicator.setupWithViewPager(mainViewPager);
        return view;


    }


}



