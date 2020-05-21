package com.example.jms.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.connection.viewmodel.APIViewModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SamplePagerAdapter extends PagerAdapter {

    private Context context;
    private int count;

    public SamplePagerAdapter(Context context) {
        this.context = context;
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main_view_pager, container, false);

        TextView tv = view.findViewById(R.id.item_text);
        TextView tv2 = view.findViewById(R.id.item_text2);
        TextView tv3 = view.findViewById(R.id.item_text3);

        if(position ==0 ) {
            tv.setText(RestfulAPI.principalUser.getFullname() + " 님"); //user에 따라 수정(현재 0, 1, 2.. 이렇게 보임)
            tv2.setText("");//"startTick: "+UserDataModel.userDataModels[position].getDataList().get(0).getStartTick()); //user에 따라 수정
            tv3.setText("");//"TotalLux: "+UserDataModel.userDataModels[position].getDataList().get(0).getTotalLux()); //user에 따라 수정
        }
        else{
            tv.setText(RestfulAPI.principalUser.getFriend().get(position-1).getFullname() + " 님"); //user에 따라 수정(현재 0, 1, 2.. 이렇게 보임)
            tv2.setText("");//"startTick: "+UserDataModel.userDataModels[position].getDataList().get(0).getStartTick()); //user에 따라 수정
            tv3.setText("");//"TotalLux: "+UserDataModel.userDataModels[position].getDataList().get(0).getTotalLux()); //user에 따라 수정
        }

        container.addView(view);
        return view;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }



}
