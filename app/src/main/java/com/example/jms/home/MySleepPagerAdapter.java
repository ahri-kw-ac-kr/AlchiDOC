package com.example.jms.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MySleepPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;

    public MySleepPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                DaySleep daySleep = new DaySleep();
                return daySleep;
            case 1:
                WeekSleep weekSleep = new WeekSleep();
                return weekSleep;
            case 2:
                MonthSleep monthSleep = new MonthSleep();
                return monthSleep;
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
