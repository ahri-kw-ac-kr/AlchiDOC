package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jms.R;
import com.example.jms.connection.model.RestfulAPI;
import com.example.jms.home.UserDataModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MonthLight extends Fragment {

    View view;

    TextView titleDay;
    TextView titlePercent;
    String titleD;

    TextView manyT;
    TextView propT;
    TextView lackT;

    TextView AvgLux;
    TextView AvgNightLux;
    TextView AvgNightTemp;

    TextView monthLightPlan1,monthLightPlan2;
    ImageView face1,face2;
    TextView state1,state2;

    public MonthLight() {}


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_light, container, false);
        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart2);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String curr = transFormat.format(calendar.getTime());
        int thisMonth = Integer.parseInt(curr.substring(4,6));

        //000님의 0월 전체 평균
        titleDay = (TextView) view.findViewById(R.id.monthlightdate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + thisMonth + "월 " + "전체 평균"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + thisMonth + "월 " + "전체 평균"; }
        titleDay.setText(titleD);

        //조도량 00%
        titlePercent = (TextView) view.findViewById(R.id.monthlightpercent);
        int percent = user.getStatLight().getMonthPercent();
        String dayP = "조도량 " + percent + "%";
        titlePercent.setText(dayP);

        //라벨 텍스트
        manyT = (TextView)view.findViewById(R.id.monthlightmany);
        propT = (TextView)view.findViewById(R.id.monthlightprop);
        lackT = (TextView)view.findViewById(R.id.monthlightlack);
        manyT.setText(user.getStatLight().getMonthMany()+"일");
        propT.setText(user.getStatLight().getMonthProper()+"일");
        lackT.setText(user.getStatLight().getMonthLack()+"일");

        //주간 조도량
        AvgLux = view.findViewById(R.id.monthlux1);
        AvgLux.setText(user.getStatLight().getMonthSunAvg()+"");
        //야간 조도량
        AvgNightLux = view.findViewById(R.id.monthlux3);
        AvgNightLux.setText(user.getStatLight().getMonthMoonLuxAvg()+"");
        //야간 색온도도
        AvgNightTemp = view.findViewById(R.id.monthK);
        AvgNightTemp.setText(user.getStatLight().getMonthMoonTempAvg()+"");

        //그래프
       int[] colorArray = new int[] {Color.parseColor("#F8683C"), Color.parseColor("#F99678"), Color.parseColor("#FFB59F")};

        ArrayList NoOfEmp = new ArrayList();

        NoOfEmp.add(new Entry(user.getStatLight().getMonthMany(), 0));
        NoOfEmp.add(new Entry(user.getStatLight().getMonthProper(), 1));
        NoOfEmp.add(new Entry(user.getStatLight().getMonthLack(), 2));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        ArrayList name = new ArrayList();
        name.add("과다");
        name.add("충분");
        name.add("부족");

        PieData data = new PieData(name, dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // false로 바꾸면 데이터가 백분율이 아닌 원래 값으로 그려짐
        pieChart.setCenterText("조도량");
        pieChart.setCenterTextSize(15);
        pieChart.setHoleRadius(25);
        pieChart.setDescription(null);
        pieChart.setDrawSliceText(false); // true로 바꾸면 차트에 '과다, 충분, 부족'도 같이 나타남
        pieChart.setTransparentCircleAlpha(100); // 차트 안에 작은 원 투명도 조절(0~255): 255이 제일 투명
        pieChart.setTransparentCircleRadius(35);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false); // true로 바꾸면 범례 생김

        data.setValueTextSize(18); // 원 안에 퍼센트값 크기 조정
        data.setValueTextColor(Color.parseColor("#982B0A")); // 퍼센트값 색상

        dataSet.setColors(colorArray);
        pieChart.animateXY(5000, 5000);


        //코멘트

        monthLightPlan1 = (TextView) view.findViewById(R.id.monthLightPlan1);
        monthLightPlan2 = (TextView) view.findViewById(R.id.monthLightPlan2);
        face1 = (ImageView) view.findViewById(R.id.monthLightFace1);
        face2 = (ImageView) view.findViewById(R.id.monthLightFace2);
        state1 = (TextView) view.findViewById(R.id.monthLightState1);
        state2 = (TextView) view.findViewById(R.id.monthLightState1);

        int sun = 0;
        int moonLux = 0;
        int moonTemp = 0;

        for(int i=0; i<Integer.parseInt(curr.substring(6)); i++){
            sun += user.getStatLight().getMonthSumSun()[i];
            moonLux +=user.getStatLight().getMonthSumMoonLux()[i];
            moonTemp +=user.getStatLight().getMonthSumMoonTemp()[i];
        }
        //주간코멘트
        if(sun >= 60000){
            //충분
            monthLightPlan1.setText(R.string.monthLightComment1);
            face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
            state1.setText(R.string.good);
        }
        else{
            //부족
            monthLightPlan1.setText(R.string.monthLightComment2);
            face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            state1.setText(R.string.bad);
        }

        //야간코멘트
        if(moonLux <=400 ||moonTemp <= 2500){
            //적정
            monthLightPlan2.setText(R.string.monthLightComment4);
            face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
            state2.setText(R.string.good);
        }
        else{
            //과다
            monthLightPlan2.setText(R.string.monthLightComment3);
            face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            state2.setText(R.string.exceed);
        }

        /* 데이터 바뀔 때 쓰는 코드(지금은 임의의 값 집어넣은 것)
        참고 사이트:
        https://github.com/PhilJay/MPAndroidChart/wiki/Dynamic-&-Realtime-Data

        data.notifyDataSetChanged(); // 차트에서 기본 데이터가 변경되었음을 확인하고 필요한 모든 재계산
        pieChart.notifyDataSetChanged(); // 차트에 데이터가 바뀌었다고 알림
        pieChart.invalidate(); // 차트 다시 그려! 명령하는 코드
        */
        return view;
    }
}
