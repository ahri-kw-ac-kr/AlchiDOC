package com.example.jms;

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

import org.w3c.dom.Text;

import java.util.Random;

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
        tv.setText(position + " 님"); //user에 따라 수정(현재 0, 1, 2.. 이렇게 보임)
        tv2.setText("인천시 중구 영종해안남로321번길"); //user에 따라 수정
        tv3.setText("2020년 3월 19일 오후 3시 57분"); //user에 따라 수정

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }




}
