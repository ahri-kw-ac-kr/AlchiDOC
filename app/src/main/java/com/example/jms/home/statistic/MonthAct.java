package com.example.jms.home.statistic;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MonthAct extends Fragment {

    View view;
    TextView monthActPlan1;
    TextView monthActPlan2;
    TextView titleDay;
    TextView titlePercent;
    TextView avgT;
    TextView avgK;
    TextView manyT;
    TextView propT;
    TextView lackT;
    String titleD;

    public MonthAct(){}

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_act, container, false);
        PieChart pieChart = view.findViewById(R.id.monthActPiechart);
        int pos = UserDataModel.currentP;
        UserDataModel user = UserDataModel.userDataModels[pos];

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String curr = transFormat.format(calendar.getTime());
        int thisMonth = Integer.parseInt(curr.substring(4,6));

        //000님의 0월 전체 평균
        titleDay = (TextView) view.findViewById(R.id.monthActDate);
        if(pos == 0){ titleD = RestfulAPI.principalUser.getFullname() + "님의 " + thisMonth + "월 " + "전체 평균"; }
        else{ titleD = RestfulAPI.principalUser.getFriend().get(pos-1).getFullname() + "님의 " + thisMonth + "월 " + "전체 평균"; }
        titleDay.setText(titleD);

        //활동량 00%
        titlePercent = (TextView) view.findViewById(R.id.monthActPercent);
        int percent = user.getStatAct().getMonthPercent();
        String dayP = "활동량 " + percent + "%";
        titlePercent.setText(dayP);

        //라벨 텍스트
        manyT = (TextView)view.findViewById(R.id.monthActMany);
        propT = (TextView)view.findViewById(R.id.monthActProp);
        lackT = (TextView)view.findViewById(R.id.monthActLack);
        manyT.setText(user.getStatAct().getMonthMany()+"일");
        propT.setText(user.getStatAct().getMonthProper()+"일");
        lackT.setText(user.getStatAct().getMonthLack()+"일");

        //걸음수
        avgT = (TextView) view.findViewById(R.id.monthActAvgS);
        avgT.setText(""+user.getStatAct().getMonthAvg());

        //칼로리
        avgK = (TextView) view.findViewById(R.id.monthActAvgK);
        avgK.setText(""+user.getStatAct().getMonthKal());

        //그래프
        int[] colorArray = new int[] {Color.parseColor("#6EAD22"), Color.parseColor("#8bc34a"), Color.parseColor("#C0E296")};

        ArrayList NoOfEmp = new ArrayList();
        NoOfEmp.add(new Entry(user.getStatAct().getMonthMany(), 0));
        NoOfEmp.add(new Entry(user.getStatAct().getMonthProper(), 1));
        NoOfEmp.add(new Entry(user.getStatAct().getMonthLack(), 2));
        PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

        ArrayList name = new ArrayList();
        name.add("과다");
        name.add("충분");
        name.add("부족");

        PieData data = new PieData(name, dataSet);
        pieChart.setData(data);
        pieChart.setUsePercentValues(true); // false로 바꾸면 데이터가 백분율이 아닌 원래 값으로 그려짐
        pieChart.setCenterText("활동량");
        pieChart.setCenterTextSize(15);
        pieChart.setHoleRadius(25);
        pieChart.setDescription(null);
        pieChart.setDrawSliceText(false); // true로 바꾸면 차트에 '과다, 충분, 부족'도 같이 나타남
        pieChart.setTransparentCircleAlpha(100); // 차트 안에 작은 원 투명도 조절(0~255): 255이 제일 투명
        pieChart.setTransparentCircleRadius(35);

        Legend legend = pieChart.getLegend();
        legend.setEnabled(false); // true로 바꾸면 범례 생김

        data.setValueTextSize(18); // 원 안에 퍼센트값 크기 조정
        data.setValueTextColor(Color.parseColor("#064808")); // 퍼센트값 색상

        dataSet.setColors(colorArray);
        pieChart.animateXY(5000, 5000);

        //////////코멘트///////////
        monthActPlan1 = (TextView) view.findViewById(R.id.monthActPlan1);
        monthActPlan2 = (TextView) view.findViewById(R.id.monthActPlan2);

        int sun = 0;
        int moon = 0;
        for(int i=0; i<Integer.parseInt(curr.substring(6)); i++){
            sun += user.getStatAct().getMonthSumSun()[i];
            moon +=user.getStatAct().getMonthSumMoon()[i];
        }
        //주간코멘트
        if(sun >= 6000){
            //충분
            monthActPlan1.setText(R.string.monthActComment1);
        }
        else{
            //부족
            monthActPlan1.setText(R.string.monthActComment2);
        }

        //야간코멘트
        if(moon <=2000){
            //적정
            monthActPlan2.setText(R.string.monthActComment4);
        }
        else{
            //과다
            monthActPlan2.setText(R.string.monthActComment3);
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
