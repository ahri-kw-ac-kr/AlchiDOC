package com.example.jms.home.button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jms.home.statistic.DayAct;
import com.example.jms.home.statistic.MonthAct;
import com.example.jms.home.statistic.WeekAct;

public class MyActPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public MyActPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                DayAct dayAct = new DayAct();
                return dayAct;
            case 1:
                WeekAct weekAct = new WeekAct();
                return weekAct;
            case 2:
                MonthAct monthAct = new MonthAct();
                return monthAct;
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
