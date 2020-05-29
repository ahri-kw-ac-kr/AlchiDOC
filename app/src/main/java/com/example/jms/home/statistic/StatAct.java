package com.example.jms.home.statistic;

import android.util.Log;

import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import hu.akarnokd.rxjava2.math.MathFlowable;
import io.reactivex.Flowable;

public class StatAct {
    private Integer[] daySumHour;
    private int daySum;
    private int dayKal;
    private int dayPercent;

    private Integer[] weekSumDay;
    private Integer[] weekSumSun;
    private Integer[] weekSumMoon;
    private int weekAvg;
    private int weekKal;
    private int weekPercent;

    private int monthMany;
    private int monthProper;
    private int monthLack;
    private int monthAvg;
    private int monthKal;
    private int monthPercent;


    public StatAct(){}
    public StatAct(Integer[] daySumHour, int daySum, int dayKal, int dayPercent,
                   Integer[] weekSumDay, Integer[] weekSumSun, Integer[] weekSumMoon, int weekAvg, int weekKal, int weekPercent,
                   int monthMany, int monthProper, int monthLack, int monthAvg, int monthKal, int monthPercent){
        this.daySumHour = daySumHour;
        this.daySum = daySum;
        this.dayKal = dayKal;
        this.dayPercent = dayPercent;

        this.weekSumDay = weekSumDay;
        this.weekSumSun = weekSumSun;
        this.weekSumMoon = weekSumMoon;
        this.weekAvg = weekAvg;
        this.weekKal = weekKal;
        this.weekPercent = weekPercent;

        this.monthAvg = monthAvg;
        this.monthProper = monthProper;
        this.monthLack = monthLack;
        this.monthKal = monthKal;
        this.monthPercent = monthPercent;
    }

    public Integer[] getDaySumHour() { return daySumHour; }
    public int getDaySum() { return daySum; }
    public int getDayKal() { return dayKal; }
    public int getDayPercent() { return dayPercent; }
    public Integer[] getWeekSumDay() { return weekSumDay; }
    public Integer[] getWeekSumSun() { return weekSumSun; }
    public Integer[] getWeekSumMoon() { return weekSumMoon; }
    public int getWeekAvg() { return weekAvg; }
    public int getWeekKal() { return weekKal; }
    public int getWeekPercent() { return weekPercent; }
    public int getMonthMany() { return monthMany; }
    public int getMonthProper() { return monthProper; }
    public int getMonthLack() { return monthLack; }
    public int getMonthAvg() { return monthAvg; }
    public int getMonthKal() { return monthKal; }
    public int getMonthPercent() { return monthPercent; }

    public void setDaySumHour(Integer[] daySumHour) { this.daySumHour = daySumHour; }
    public void setDaySum(int daySum) { this.daySum = daySum; }
    public void setDayKal(int dayKal) { this.dayKal = dayKal; }
    public void setDayPercent(int dayPercent) { this.dayPercent = dayPercent; }
    public void setWeekSumDay(Integer[] weekSumDay) { this.weekSumDay = weekSumDay; }
    public void setWeekSumSun(Integer[] weekSumSun) { this.weekSumSun = weekSumSun; }
    public void setWeekSumMoon(Integer[] weekSumMoon) { this.weekSumMoon = weekSumMoon; }
    public void setWeekAvg(int weekAvg) { this.weekAvg = weekAvg; }
    public void setWeekKal(int weekKal) { this.weekKal = weekKal; }
    public void setWeekPercent(int weekPercent) { this.weekPercent = weekPercent; }
    public void setMonthMany(int monthMany) { this.monthMany = monthMany; }
    public void setMonthProper(int monthPercent) { this.monthProper = monthProper; }
    public void setMonthLack(int monthLack) { this.monthLack = monthLack; }
    public void setMonthAvg(int weekAvg) { this.monthAvg = monthAvg; }
    public void setMonthKal(int weekKal) { this.monthKal = monthKal; }
    public void setMonthPercent(int weekPercent) { this.monthPercent = monthPercent; }

    private void day(List<List<RawdataDTO>> perHour){
        //합산 준비
        Integer[] sumHour = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hour = new Integer[24][];
        for (int i = 0; i < perHour.size(); i++) {
            hour[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            for (int j = 0; j < perHour.get(i).size(); j++) {
                hour[i][j] = (int) perHour.get(i).get(j).getSteps();
                Log.d("DayAct", "i: " + i + ", j: " + j + ", hour: " + hour[i][j]);
                sumHour[i] += hour[i][j];
            }
            Log.d("StatAct-DayAct", i + ", " + sumHour[i]);//합산 잘 되었는지 확인
        }
        daySumHour = sumHour;

        //걸음수,칼로리 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumHour).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        daySum = total.intValue();
        dayKal = total.intValue() / 30;

        //퍼센트 구하는 곳
        dayPercent = (total.intValue()*100) / 6000;
    }

    private void week(List<List<RawdataDTO>> perDay){
        //합산 준비
        Integer[] sumDay = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayMor = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDin = {0, 0, 0, 0, 0, 0, 0};
        Integer[][] day = new Integer[7][];
        for (int i = 0; i < perDay.size(); i++) {
            day[i] = new Integer[150];
            for (int j = 0; j < perDay.get(i).size(); j++) {
                day[i][j] = (int) perDay.get(i).get(j).getSteps();
                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + day[i][j]);
                if(j>=9 && j<18){sumDayMor[i]+=day[i][j];}
                if(j>=18 && j<21){sumDayDin[i]+=day[i][j];}
                sumDay[i] += day[i][j];
            }
            Log.d("StatAct-WeekAct", i + ", " + sumDay[i]);//합산 잘 되었는지 확인
        }
        weekSumDay = sumDay;
        weekSumSun = sumDayMor;
        weekSumMoon = sumDayDin;

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);

        //걸음수, 칼로리, 퍼센트 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumDay).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        int avg = Math.round(total.intValue()/thisDay);
        weekAvg = avg;
        weekKal = avg/30;
        weekPercent = (avg*100) / 6000;
    }

    private void month(List<List<RawdataDTO>> perDay){
        //합산 준비
        Integer[] sumDay = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayMor = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDin = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] day = new Integer[31][];
        for (int i = 0; i < perDay.size(); i++) {
            day[i] = new Integer[150];
            for (int j = 0; j < perDay.get(i).size(); j++) {
                day[i][j] = (int) perDay.get(i).get(j).getSteps();
                Log.d("WeekAct", "i: " + i + ", j: " + j + ", day: " + day[i][j]);
                if(j>=9 && j<18){sumDayMor[i]+=day[i][j];}
                if(j>=18 && j<21){sumDayDin[i]+=day[i][j];}
                sumDay[i] += day[i][j];
            }
            Log.d("StatAct-MonthAct", i + ", " + sumDay[i]);//합산 잘 되었는지 확인
        }

        //과다,충분,부족 별 날짜 개수
        monthMany=0;
        monthProper=0;
        monthLack=0;

        for(int i=0; i<31; i++){
            if(sumDayMor[i]>=6000 && sumDayDin[i]<2000) { monthProper++; }
            else if(sumDayMor[i]>=6000 && sumDayDin[i]>=2000) { monthMany++; }
            else if(sumDayMor[i]<6000 && sumDayDin[i]<2000) { monthLack++; }
            else if(sumDayMor[i]<6000 && sumDayDin[i]>=2000) { monthLack++; }
        }

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        //걸음수, 칼로리, 퍼센트 구하는 곳
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumDay).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        int avg = Math.round(total.intValue()/thisDay);
        monthAvg = avg;
        monthKal = avg/30;
        monthPercent = (avg*100) / 6000;
    }

    public void parsing(List<List<RawdataDTO>> perHour, List<List<RawdataDTO>> perDay, List<List<RawdataDTO>> perMonthDay){
        day(perHour);
        week(perDay);
        month(perMonthDay);
    }
}
