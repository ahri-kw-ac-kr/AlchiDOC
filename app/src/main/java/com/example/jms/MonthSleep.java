package com.example.jms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

public class MonthSleep extends AppCompatActivity {

    int[] colorArray = new int[] {Color.parseColor("#A991D8"), Color.parseColor("#C5AEEF"), Color.parseColor("#E6CEFF")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.month_sleep);

        Toolbar toolbar = findViewById(R.id.toolbar3);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        PieChart pieChart = findViewById(R.id.piechart3);
        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new Entry(4, 0));
        NoOfEmp.add(new Entry(15, 1));
        NoOfEmp.add(new Entry(11, 2));

        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        ArrayList name = new ArrayList();
        name.add("과다");
        name.add("충분");
        name.add("부족");

        PieData data = new PieData(name, dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // false로 바꾸면 데이터가 백분율이 아닌 원래 값으로 그려짐
        pieChart.setCenterText("수면량");
        pieChart.setCenterTextSize(15);
        pieChart.setHoleRadius(25);
        pieChart.setDescription(null);
        pieChart.setDrawSliceText(false); // true로 바꾸면 차트에 '과다, 충분, 부족'도 같이 나타남
        pieChart.setTransparentCircleAlpha(100); // 차트 안에 작은 원 투명도 조절(0~255): 255이 제일 투명
        pieChart.setTransparentCircleRadius(35);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false); // true로 바꾸면 범례 생김

        data.setValueTextSize(18); // 원 안에 퍼센트값 크기 조정
        data.setValueTextColor(Color.parseColor("#3B2760")); // 퍼센트값 색상

        dataSet.setColors(colorArray);
        pieChart.animateXY(5000, 5000);

        /* 데이터 바뀔 때 쓰는 코드(지금은 임의의 값 집어넣은 것)
        참고 사이트:
        https://github.com/PhilJay/MPAndroidChart/wiki/Dynamic-&-Realtime-Data

        data.notifyDataSetChanged(); // 차트에서 기본 데이터가 변경되었음을 확인하고 필요한 모든 재계산
        pieChart.notifyDataSetChanged(); // 차트에 데이터가 바뀌었다고 알림
        pieChart.invalidate(); // 차트 다시 그려! 명령하는 코드
        */
    }
}
