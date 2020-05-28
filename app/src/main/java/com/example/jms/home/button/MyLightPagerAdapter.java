package com.example.jms.home.button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jms.home.statistic.DayLight;
import com.example.jms.home.statistic.MonthLight;
import com.example.jms.home.statistic.WeekLight;

public class MyLightPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public MyLightPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                DayLight dayLight = new DayLight();
                return dayLight;
            case 1:
                WeekLight weekLight = new WeekLight();
                return weekLight;
            case 2:
                MonthLight monthLight = new MonthLight();
                return monthLight;
            default:
                return null;
        }

        //return null;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
