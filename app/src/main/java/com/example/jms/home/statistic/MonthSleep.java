package com.example.jms.home.statistic;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.home.UserDataModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthSleep extends Fragment {

    public MonthSleep(){}

    View view;
    int[] colorArray = new int[] {Color.parseColor("#A991D8"), Color.parseColor("#C5AEEF"), Color.parseColor("#E6CEFF")};

    int percent;
    int totalH;
    int totalM;
    int deepH;
    int deepM;
    int lightH;
    int lightM;
    String titleD;

    TextView titleDay;
    TextView titlePercent;
    TextView totalHT;
    TextView totalMT;
    TextView deepHT;
    TextView deepMT;
    TextView lightHT;
    TextView lightMT;
    TextView wakeT;
    TextView turnT;
    TextView level1;
    TextView level2;
    TextView level3;
    TextView monthSleepPlan1, monthSleepPlan2;
    ImageView face1, face2;
    TextView state1, state2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_sleep, container, false);
        PieChart pieChart = (PieChart) view.findViewById(R.id.piechart3);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];
        List<SleepDTO> monthList = user.getStatSleep().getMonthList();


        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String curr = transFormat.format(calendar.getTime());
        int thisMonth = Integer.parseInt(curr.substring(4,6));
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        //000님의 0월 전체 평균
        titleDay = (TextView) view.findViewById(R.id.monthSleepTitle);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + thisMonth + "월 " + "전체"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + thisMonth + "월 " + "전체"; }
        titleDay.setText(titleD);

        ////////변수 초기화
        Integer[] totalSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] deepSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] lightSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] wakeSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] turnSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] turnHourSum = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] percentList = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        Integer[] deepPercentList = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int total = 0;
        int deep = 0;
        int light = 0;
        int wake = 0;
        int turn = 0;
        int turnHour = 0;

        if(monthList==null){
            level1 = (TextView) view.findViewById(R.id.monthSleepLevel1);
            level2 = (TextView) view.findViewById(R.id.monthSleepLevel2);
            level3 = (TextView) view.findViewById(R.id.monthSleepLevel3);

            /////그래프/////
            int perfect = 0;
            int soso = 0;
            int lack = thisDay;

            level1.setText(perfect + "일");
            level2.setText(soso + "일");
            level3.setText(lack + "일");

            ArrayList NoOfEmp = new ArrayList();
            NoOfEmp.add(new Entry(perfect, 0));
            NoOfEmp.add(new Entry(soso, 1));
            NoOfEmp.add(new Entry(lack, 2));
            PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

            ArrayList name = new ArrayList();
            name.add("충분");
            name.add("보통");
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
        }
        else if(monthList!=null) {
            /////////////////////월간통계//////////////////
            for (int i = 0; i < monthList.size(); i++) {
                totalSum[i] = monthList.get(i).getTotal();
                deepSum[i] = monthList.get(i).getDeep();
                lightSum[i] = monthList.get(i).getLight();
                wakeSum[i] = monthList.get(i).getWake();
                turnSum[i] = monthList.get(i).getTurn();
                turnHourSum[i] = monthList.get(i).getTurnHour();
                percentList[i] = (int) (((monthList.get(i).getTotal() - ((monthList.get(i).getWake() * 10.0) + monthList.get(i).getTurn())) / monthList.get(i).getTotal()) * 100);
                try {
                    deepPercentList[i] = (int) ((((double) monthList.get(i).getDeep() / (double) monthList.get(i).getTotal())) * 100);
                } catch (Exception e) {
                    deepPercentList[i] = 0;
                }
            }

            for (int i = 0; i < monthList.size(); i++) {
                total += totalSum[i];
                deep += deepSum[i];
                light += lightSum[i];
                wake += wakeSum[i];
                turn += turnSum[i];
                turnHour += turnHourSum[i];
            }

            int totalA = total / thisDay;
            int deepA = deep / thisDay;
            int lightA = light / thisDay;
            int wakeA = wake / thisDay;
            int turnA = turn / thisDay;
            int turnHourA = turnHour / thisDay;
            percent = (int) (((totalA - ((wakeA * 10.0) + turnA)) / totalA) * 100);
            totalH = totalA / 60; //총수면시간 시
            totalM = totalA % 60; //총수면시간 분
            deepH = deepA / 60;
            deepM = deepA % 60;
            lightH = lightA / 60;
            lightM = lightA % 60;


            ///퍼센트
            titlePercent = (TextView) view.findViewById(R.id.monthSleepPercent);
            String strP = "평균 수면량 " + percent + "%";
            titlePercent.setText(strP);

            //수면량 기록 카드뷰
            totalHT = (TextView) view.findViewById(R.id.monthSleepTotalH);
            totalMT = (TextView) view.findViewById(R.id.monthSleepTotalM);
            deepHT = (TextView) view.findViewById(R.id.monthSleepDeepH);
            deepMT = (TextView) view.findViewById(R.id.monthSleepDeepM);
            lightHT = (TextView) view.findViewById(R.id.monthSleepLightH);
            lightMT = (TextView) view.findViewById(R.id.monthSleepLightM);
            wakeT = (TextView) view.findViewById(R.id.monthSleepWake);
            turnT = (TextView) view.findViewById(R.id.monthSleepTurn);
            level1 = (TextView) view.findViewById(R.id.monthSleepLevel1);
            level2 = (TextView) view.findViewById(R.id.monthSleepLevel2);
            level3 = (TextView) view.findViewById(R.id.monthSleepLevel3);


            totalHT.setText("" + totalH);
            totalMT.setText("" + totalM);
            deepHT.setText("" + deepH);
            deepMT.setText("" + deepM);
            lightHT.setText("" + lightH);
            lightMT.setText("" + lightM);
            wakeT.setText("" + wakeA);
            turnT.setText("" + turnA);


            /////그래프/////
            int perfect = 0;
            int soso = 0;
            int lack = 0;

            for (int i = 0; i < thisDay; i++) {
                if (percentList[i] >= 80 && deepPercentList[i] >= 25) {
                    perfect += 1;
                } else if ((percentList[i] >= 80 && deepPercentList[i] < 25) || (percentList[i] < 80 && deepPercentList[i] >= 25)) {
                    soso += 1;
                } else if (percentList[i] < 80 && deepPercentList[i] < 25) {
                    lack += 1;
                }
            }

            level1.setText(perfect + "일");
            level2.setText(soso + "일");
            level3.setText(lack + "일");

            ArrayList NoOfEmp = new ArrayList();
            NoOfEmp.add(new Entry(perfect, 0));
            NoOfEmp.add(new Entry(soso, 1));
            NoOfEmp.add(new Entry(lack, 2));
            PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

            ArrayList name = new ArrayList();
            name.add("충분");
            name.add("보통");
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

            /////////코멘트///////
            monthSleepPlan1 = (TextView) view.findViewById(R.id.monthSleepPlan1);
            monthSleepPlan2 = (TextView) view.findViewById(R.id.monthSleepPlan2);
            face1 = (ImageView) view.findViewById(R.id.face1);
            face2 = (ImageView) view.findViewById(R.id.face2);
            state1 = (TextView) view.findViewById(R.id.state1);
            state2 = (TextView) view.findViewById(R.id.state2);

            int deepPercent = 0;
            try {
                deepPercent = (int) ((((double) deepA / (double) totalA)) * 100);
            } catch (Exception e) {
            }

            Log.d("MonthSleep", "monthList 길이 : " + user.getStatSleep().getMonthList().size() + ", pos: " + pos);
            //////////////////////////////////총 수면시간 코멘트////////////////////////
            if (user.getStatSleep().getMonthList().size() == 0) {//데이터 없음

            } else {
                if (percent >= 80) {// 수면효율 정상
                    if (deepPercent >= 25) {//깊은잠 정상
                        monthSleepPlan1.setText(R.string.monthSleepComment2);
                        face1.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                        state1.setText(R.string.sleepState1);
                    } else {//깊은잠 부족
                        monthSleepPlan1.setText(R.string.monthSleepComment1);
                        face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state1.setText(R.string.sleepState2);
                    }
                } else {//수면효율 비정상
                    if (deepPercent >= 25) {//깊은잠 정상
                        monthSleepPlan1.setText(R.string.monthSleepComment4);
                        face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state1.setText(R.string.sleepState2);
                    } else {//깊은잠 부족
                        monthSleepPlan1.setText(R.string.monthSleepComment3);
                        face1.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                        state1.setText(R.string.sleepState2);
                    }
                }

                ///////////////////////////////평균뒤척임 코멘트///////////////////////////
                if (Math.round(turnHourA) == 0) {//정상
                    monthSleepPlan2.setText(R.string.monthSleepComment5);
                    face2.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
                    state2.setText(R.string.sleepState1);
                } else if (Math.round(turnHourA) == 1) {//주의
                    monthSleepPlan2.setText(R.string.monthSleepComment6);
                    face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state2.setText(R.string.sleepState2);
                } else if (Math.round(turnHourA) == 2) {//관리필요
                    monthSleepPlan2.setText(R.string.monthSleepComment7);
                    face2.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
                    state2.setText(R.string.sleepState3);
                }
            }

        /* 데이터 바뀔 때 쓰는 코드(지금은 임의의 값 집어넣은 것)
        참고 사이트:
        https://github.com/PhilJay/MPAndroidChart/wiki/Dynamic-&-Realtime-Data

        data.notifyDataSetChanged(); // 차트에서 기본 데이터가 변경되었음을 확인하고 필요한 모든 재계산
        pieChart.notifyDataSetChanged(); // 차트에 데이터가 바뀌었다고 알림
        pieChart.invalidate(); // 차트 다시 그려! 명령하는 코드
        */
        }
        return view;
    }
}
