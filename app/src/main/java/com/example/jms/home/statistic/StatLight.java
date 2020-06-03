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

public class StatLight {

    /*일간*/
    private Integer[] daySumHourLux; //일 시간마다 조도량 합산
    private Integer[] daySumHourTemp; //일 시간마다 조도량 합산
    private Integer[] daySumHourLuxWgt; //일 시간마다 조도량 합산
    private int daySum; // 지금까지 전체 조도량 합산
    private int dayPercent; // 맨 위 하루 전체 조도량

    private int dayDaySumLux; // 낮 조도량
    private int dayEvenSumLux; // 저녁 조도량
    private int dayEvenSumTemp; // 저녁 색온도
    private int dayNightSumLux; // 야간 조도량
    private int dayNightSumTemp; //야간 색온도

    /*주간*/
    private Integer[] weekSumDay; //주 요일마다 전체 조도량 합산
    private Integer[] weekSumDayTemp;
    private Integer[] weekSumSun; //주간 낮 조도량 (오전 9시 ~ 오후 6시)
    private Integer[] weekSumMoonLux; //주간 밤 조도량 (오후 6시 ~ 오후 9시)
    private Integer[] weekSumMoonTemp; //주간 밤 색온도 (오후 6시 ~ 오후 9시)
    private int weekAvg;
    private int weekPercent;
    private int weekSunAvg;
    private int weekMoonLuxAvg;
    private int weekMoonTempAvg;

    /*월간*/
    private Integer[] monthSumDay; // 월 매일 조도량
    private Integer[] monthSumSun;
    private Integer[] monthSumMoonLux;
    private Integer[] monthSumMoonTemp;
    private int monthPercent;
    private int monthAvg;
    private int monthSunAvg;
    private int monthMoonLuxAvg;
    private int monthMoonTempAvg;
    private int monthProper;
    private int monthMany;
    private int monthLack;

    public StatLight() {
    }

    public StatLight(Integer[] daySumHourLux, Integer[] daySumHourTemp, Integer[] daySumHourLuxWgt,int daySum, int dayPercent, int dayDaySumLux, int dayEvenSumLux, int dayEvenSumTemp, int dayNightSumLux, int dayNightSumTemp,
                     Integer[] weekSumDay, Integer[] weekSumSun, Integer[] weekSumMoonLux, Integer[] weekSumMoonTemp, int weekAvg,int weekPercent, int weekSunAvg, int weekMoonLuxAvg, int weekMoonTempAvg,
                     Integer[] monthSumDay, Integer[] monthSumSun, Integer[] monthSumMoonLux, Integer[] monthSumMoonTemp,int monthPercent, int monthAvg, int monthSunAvg, int monthMoonLuxAvg, int monthMoonTempAvg,
                     int monthMany, int monthProper, int monthLack) {

        this.daySumHourLux = daySumHourLux;
        this.daySumHourTemp = daySumHourTemp;
        this.dayNightSumTemp = dayNightSumTemp;
        this.daySum = daySum;
        this.dayPercent = dayPercent;
        this.dayDaySumLux = dayDaySumLux;
        this.dayEvenSumLux = dayEvenSumLux;
        this.dayEvenSumTemp = dayEvenSumTemp;
        this.dayNightSumLux = dayNightSumLux;

        this.weekSumDay = weekSumDay;
        this.weekSumSun = weekSumSun;
        this.weekSumMoonLux = weekSumMoonLux;
        this.weekSumMoonTemp = weekSumMoonTemp;
        this.weekAvg = weekAvg;
        this.weekPercent = weekPercent;
        this.weekSunAvg = weekSunAvg;
        this.weekMoonLuxAvg = weekMoonLuxAvg;

        this.monthSumDay = monthSumDay;
        this.monthSumSun = monthSumSun;
        this.monthSumMoonLux = monthSumMoonLux;
        this.monthSumMoonTemp = monthSumMoonTemp;
        this.monthPercent = monthPercent;
        this.monthAvg = monthAvg;
        this.monthProper = monthProper;
        this.monthLack = monthLack;
    }

    /*getter*/
    public Integer[] getDaySumHourLux() { return daySumHourLux; }
    public Integer[] getDaySumHourTemp() { return daySumHourTemp; }
    public Integer[] getDaySumHourLuxWgt() {return daySumHourLuxWgt;}
    public int getDaySum() { return daySum; }
    public int getDayPercent() { return dayPercent; }
    public int getDayDaySumLux() { return dayDaySumLux; }
    public int getDayEvenSumLux() { return dayEvenSumLux; }
    public int getDayEvenSumTemp() { return dayEvenSumTemp; }
    public int getDayNightSumLux() { return dayNightSumLux; }
    public int getDayNightSumTemp() { return dayNightSumTemp; }

    public Integer[] getWeekSumDay() { return weekSumDay; }
    public Integer[] getWeekSumSun() { return  weekSumSun; }
    public Integer[] getWeekSumMoonLux() { return weekSumMoonLux; }
    public Integer[] getWeekSumMoonTemp() {return weekSumMoonTemp; }
    public int getWeekAvg() { return weekAvg; }
    public int getWeekPercent() { return weekPercent; }
    public int getWeekSunAvg() { return weekSunAvg; }
    public int getWeekMoonLuxAvg() { return weekMoonLuxAvg; }
    public int getWeekMoonTempAvg() { return weekMoonTempAvg; }

    public Integer[] getMonthSumDay() {return monthSumDay; }
    public Integer[] getMonthSumSun() {return monthSumSun;}
    public Integer[] getMonthSumMoonLux() {return monthSumMoonLux;}
    public Integer[] getMonthSumMoonTemp() {return monthSumMoonTemp;}
    public int getMonthAvg() {return monthAvg;}
    public int getMonthPercent() {return monthPercent;}
    public int getMonthSunAvg() {return monthSunAvg;}
    public int getMonthMoonLuxAvg() {return monthMoonLuxAvg;}
    public int getMonthMoonTempAvg() {return monthMoonTempAvg;}
    public int getMonthMany() { return monthMany; }
    public int getMonthProper() { return monthProper; }
    public int getMonthLack() { return monthLack; }

    /*setter*/
    public void setDaySumHourLux(Integer[] daySumHourLux){this.daySumHourLux= daySumHourLux;}
    public void setDaySumHourTemp(Integer[] daySumHourTemp){this.daySumHourTemp=daySumHourTemp;}
    public void setDaySumHourLuxWgt(Integer[] daySumHourLuxWgt){this.daySumHourLuxWgt=daySumHourLuxWgt;}
    public void setDaySum(int daySum){this.daySum=daySum;}
    public void setDayPercent(int dayPercent){this.dayPercent=dayPercent;}
    public void setDayDaySumLux(int dayDaySumLux){this.dayDaySumLux=dayDaySumLux;}
    public void setDayEvenSumLux(int dayEvenSumLux){this.dayEvenSumLux=dayEvenSumLux;}
    public void setDayEvenSumTemp(int dayEvenSumTemp){this.dayEvenSumTemp=dayEvenSumTemp;}
    public void setDayNightSumLux(int dayNightSumLux){this.dayNightSumLux=dayNightSumLux;}
    public void setDayNightSumTemp(int dayNightSumTemp){this.dayNightSumTemp=dayNightSumTemp;}

    public void setWeekSumDay(Integer[] weekSumDay){this.weekSumDay=weekSumDay;}
    public void setWeekSumSun(Integer[] weekSumSun){this.weekSumSun=weekSumSun;}
    public void setWeekSumMoonLux(Integer[] weekSumMoonLux){this.weekSumMoonLux=weekSumMoonLux;}
    public void setWeekSumMoonTemp(Integer[] weekSumMoonTemp){this.weekSumMoonTemp=weekSumMoonTemp;}
    public void setWeekAvg(int weekAvg){this.weekAvg=weekAvg;}
    public void setWeekPercent(int weekPercent){this.weekPercent=weekPercent;}
    public void setWeekSunAvg(int weekSunAvg){this.weekSunAvg=weekSunAvg;}
    public void setWeekMoonLuxAvg(int weekMoonLuxAvg){this.weekMoonLuxAvg=weekMoonLuxAvg;}
    public void setWeekMoonTempAvg(int weekMoonTempAvg){this.weekMoonTempAvg=weekMoonTempAvg;}

    public void setMonthSumDay(Integer[] monthSumDay){this.monthSumDay=monthSumDay;}
    public void setMonthSumSun(Integer[] monthSumSun){this.monthSumSun=monthSumSun;}
    public void setMonthSumMoonLux(Integer[] monthsumMoonLux){this.monthSumMoonLux=monthsumMoonLux;}
    public void setMonthSumMoonTemp(Integer[] monthSumMoonTemp){this.monthSumMoonTemp=monthSumMoonTemp;}
    public void setMonthAvg(int monthPercent){this.monthPercent=monthPercent;}
    public void setMonthPercent(int monthAvg){this.monthAvg=monthAvg;}
    public void setMonthSunAvg(int monthSunAvg){this.monthSunAvg=monthSunAvg;}
    public void setMonthMoonLuxAvg(int monthMoonLuxAvg){this.monthMoonLuxAvg=monthMoonLuxAvg;}
    public void setMonthMoonTempAvg(int monthMoonTempAvg){this.monthMoonTempAvg=monthMoonTempAvg;}
    public void setMonthMany(int monthMany) { this.monthMany = monthMany; }
    public void setMonthProper(int monthPercent) { this.monthProper = monthProper; }
    public void setMonthLack(int monthLack) { this.monthLack = monthLack; }



    private void day(List<List<RawdataDTO>> perHour) {


        Integer[] sumHourLux = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourLux = new Integer[24][];
        Integer[] sumHourTemp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourTemp = new Integer[24][];
        Integer[] sumHourLuxWgt = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[][] hourLuxWgt= new Integer[24][];

        for (int i = 0; i < perHour.size(); i++) {

            //10분마다 저장
            hourLux[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            hourTemp[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};
            hourLuxWgt[i] = new Integer[]{0, 0, 0, 0, 0, 0, 0, 0};

            for (int j = 0; j < perHour.get(i).size(); j++) {

                hourLux[i][j] = (int) perHour.get(i).get(j).getAvgLux();
                hourTemp[i][j] = (int) perHour.get(i).get(j).getAvgTemp();

                // 2000이상이면 가중치 1, 노출시간 10분.
                if (hourLux[i][j]>=2000) {hourLuxWgt[i][j] = hourLux[i][j]*10;}
                else if(1800<=hourLux[i][j]&&hourLux[i][j]<2000){hourLuxWgt[i][j] = hourLux[i][j]*9;} //가중치 0.9, 노출시간 10분
                else if(1600<=hourLux[i][j]&&hourLux[i][j]<1800){hourLuxWgt[i][j]= hourLux[i][j]*7;} //이하 동문.
                else if(1400<=hourLux[i][j]&&hourLux[i][j]<1600){hourLuxWgt[i][j]= hourLux[i][j]*4;}
                else if(1000<=hourLux[i][j]&&hourLux[i][j]<1400){hourLuxWgt[i][j]= hourLux[i][j]*1;}
                else{hourLuxWgt[i][j]= hourLux[i][j]*0*10;}

                Log.d("DayLight-Lux", "i: " + i + ", j: " + j + ", hour: " + hourLux[i][j]);
                Log.d("DayLight-Temp", "i: " + i + ", j: " + j + ", hour: " + hourTemp[i][j]);
                Log.d("DayLight-LuxWgt", "i: " + i + ", j: " + j + ", hour: " + hourLuxWgt[i][j]);

                //SumHour = 1시간동안 합
                sumHourLux[i] += hourLux[i][j];
                sumHourTemp[i] += hourTemp[i][j];
                sumHourLuxWgt[i] += hourLuxWgt[i][j];

                // 10분마다 평균을 더한 값이므로 6으로 나눠야한다
                if(j+1 == perHour.get(i).size()){
                    sumHourTemp[i] = sumHourTemp[i]/(j+1);
                }

            }
            Log.d("DayLight-시간마다 Lux합산", i + ", " + sumHourLux[i]);//합산 잘 되었는지 확인
            Log.d("DayLight-시간마다 Temp합산", i + ", " + sumHourTemp[i]);//합산 잘 되었는지 확인
            Log.d("DayLight-시간마다 LuxWgt합산", i + ", " + sumHourLuxWgt[i]);//합산 잘 되었는지 확인
        }

        daySumHourLux = sumHourLux; //1시간마다 Lux 저장
        daySumHourTemp = sumHourTemp; //1시간마다 Temp 저장
        daySumHourLuxWgt = sumHourLuxWgt; //1시간마다 Lux에 가중치 곱해서 더한거 저장

        //하루 전체 조도량 구하는 방법 가중치 먹여서
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(daySumHourLuxWgt).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        daySum = total.intValue();

        //퍼센트 구하는 곳 하루 목표 조도량 6만으로 나눈다
        dayPercent = (total.intValue()*100) / 60000;

        //일단 가중치 먹인걸로 저장합니다 - Lux는 누적량 / Temp는 평균치
        for (int a = 0;a<9;a++){
            dayNightSumLux += daySumHourLuxWgt[a];
            dayNightSumTemp += daySumHourTemp[a];

        }
        for (int a=8;a<19;a++){
            dayDaySumLux += daySumHourLuxWgt[a];
        }
        for (int a=19;a<23;a++){
            dayEvenSumLux += daySumHourLuxWgt [a];
            dayEvenSumTemp += daySumHourTemp[a];
        }
        for (int a = 22;a<24;a++){
            dayNightSumLux += daySumHourLuxWgt [a];
            dayNightSumTemp += daySumHourTemp[a];
        }

    }

    private void week(List<List<RawdataDTO>> perDay) {

        //하루치 합산 준비
        Integer[] sumDay = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayWgt = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayMor = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDinLux = {0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDinTemp = {0, 0, 0, 0, 0, 0, 0};

        Integer[][] dayLux = new Integer[7][]; //매 시간
        Integer[][] dayTemp = new Integer[7][];
        Integer[][] dayLuxWgt = new Integer[7][];

        for (int i = 0; i < perDay.size(); i++) {

            dayLux[i] = new Integer[150];
            dayTemp[i] = new Integer[150];
            dayLuxWgt[i] = new Integer[150];

            for (int j = 0; j < perDay.get(i).size(); j++) {
                long time = (long) perDay.get(i).get(j).getStartTick()*1000;
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
                String date = transFormat.format(time);
                String hour = date.substring(9,11);
                int hourI = Integer.parseInt(hour);

                dayLux[i][j] = (int) perDay.get(i).get(j).getAvgLux();
                dayTemp[i][j] = (int) perDay.get(i).get(j).getAvgTemp();

                //0~24까지 7이간 parse한다.
                Log.d("WeekLight", "i: " + i + ", j: " + j + ", day: " + dayLux[i][j]);
                Log.d("WeekLight", "i: " + i + ", j: " + j + ", day: " + dayTemp[i][j]);

                if (dayLux[i][j]>=2000) {dayLuxWgt[i][j] = dayLux[i][j]*10;}
                else if(1800<=dayLux[i][j]&&dayLux[i][j]<2000){dayLuxWgt[i][j] = dayLux[i][j]*9;} //가중치 0.9, 노출시간 10분
                else if(1600<=dayLux[i][j]&&dayLux[i][j]<1800){dayLuxWgt[i][j]= dayLux[i][j]*7;} //이하 동문.
                else if(1400<=dayLux[i][j]&&dayLux[i][j]<1600){dayLuxWgt[i][j]= dayLux[i][j]*4;}
                else if(1000<=dayLux[i][j]&&dayLux[i][j]<1400){dayLuxWgt[i][j]= dayLux[i][j]*1;}
                else{dayLuxWgt[i][j]= dayLux[i][j]*0*10;}


                if(hourI>=9 && hourI<18){
                    sumDayMor[i]+=dayLux[i][j]/9; //오전9시~오후6시 Lux 평균
                }
                if(hourI>=18 && hourI<21){
                    sumDayDinLux[i]+=dayLux[i][j]/3; //오후6시~오후9시 Lux 평균
                    sumDayDinTemp[i]+=dayTemp[i][j]/3; //오후6시~오후9시 Temp 평균
                }

                sumDay[i] += dayLux[i][j]/24; //하루 Lux
                sumDayWgt[i] += dayLuxWgt[i][j]/24; //하루 LuxWgt
            }

            Log.d("StatLight-WeekLight-MorningLux", i + ", " + sumDayMor[i]);
            Log.d("StatLight-WeekLight-DinLux", i + ", " + sumDayDinLux[i]);
            Log.d("StatLight-WeekLight-DinTemp", i + ", " + sumDayDinTemp[i]);
            Log.d("StatLight-WeekLight-DayLux", i + ", " + sumDay[i]);//합산 잘 되었는지 확인

            /*이거 되나...?*/
            weekSunAvg += sumDayMor[i];
            weekMoonLuxAvg += sumDayDinLux[i];
            weekMoonTempAvg += sumDayDinTemp[i]/(3*7);
        }

        weekSumDay = sumDay;
        //UserDataModel.userDataModels[pos].getStatLight().setWeekSumDay(sumDay);
        weekSumSun = sumDayMor;
        //UserDataModel.userDataModels[pos].getStatLight().setWeekSumSun(sumDayMor);
        weekSumMoonLux = sumDayDinLux;
        //UserDataModel.userDataModels[pos].getStatLight().setWeekSumMoonLux(sumDayDinLux);
        weekSumMoonTemp = sumDayDinTemp;
        //UserDataModel.userDataModels[pos].getStatLight().setWeekSumMoonTemp(sumDayDinTemp);


        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_WEEK);

        //매일 Lux를 다 더해서 일주일 LuxAvg 합 구하기
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumDayWgt).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        int avg = Math.round(total.intValue()/thisDay);
        weekAvg = avg;
        Log.e("StatLight",weekAvg+"");
        weekPercent = (avg*100) / 60000;
    }

    private void month(List<List<RawdataDTO>> perDay) {

        //합산 준비
        Integer[] sumDay = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayMor = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDinLux = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        Integer[] sumDayDinTemp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        Integer[][] dayLux = new Integer[31][];
        Integer[][] dayTemp = new Integer[31][];
        Integer[][] dayLuxWgt = new Integer[31][];

        for (int i = 0; i < perDay.size(); i++) {

            dayLux[i] = new Integer[150];
            dayTemp[i] = new Integer[150];
            dayLuxWgt[i] = new Integer[150];

            for (int j = 0; j < perDay.get(i).size(); j++) {

                dayLux[i][j] = (int) perDay.get(i).get(j).getAvgLux();
                dayTemp[i][j] = (int) perDay.get(i).get(j).getAvgTemp();

                Log.d("MonthLight-daylux", "i: " + i + ", j: " + j + ", day: " + dayLux[i][j]);
                Log.d("MonthLight-daytemp", "i: " + i + ", j: " + j + ", day: " + dayTemp[i][j]);

                long time = (long) perDay.get(i).get(j).getStartTick()*1000;
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd HH");
                String date = transFormat.format(time);
                String hour = date.substring(9,11);
                int hourI = Integer.parseInt(hour);

                // 2000이상이면 가중치 1, 노출시간 10분.
                if (dayLux[i][j]>=2000) {dayLuxWgt[i][j] = dayLux[i][j]*10;}
                else if(1800<=dayLux[i][j]&&dayLux[i][j]<2000){dayLuxWgt[i][j] = dayLux[i][j]*9;} //가중치 0.9, 노출시간 10분
                else if(1600<=dayLux[i][j]&&dayLux[i][j]<1800){dayLuxWgt[i][j]= dayLux[i][j]*7;} //이하 동문.
                else if(1400<=dayLux[i][j]&&dayLux[i][j]<1600){dayLuxWgt[i][j]= dayLux[i][j]*4;}
                else if(1000<=dayLux[i][j]&&dayLux[i][j]<1400){dayLuxWgt[i][j]= dayLux[i][j]*1;}
                else{dayLuxWgt[i][j]= dayLux[i][j]*0*10;}

                if(hourI>=9 && hourI<18){
                    sumDayMor[i]+=dayLuxWgt[i][j];
                    monthSunAvg += sumDayMor[i];
                }
                if(hourI>=18 && hourI<21){
                    sumDayDinLux[i]+=dayLuxWgt[i][j];
                    sumDayDinTemp[i]+=dayTemp[i][j];
                    monthMoonLuxAvg += sumDayDinLux[i];
                    monthMoonTempAvg+= sumDayDinTemp[i];
                }
                sumDay[i] += dayLux[i][j];



            }
            Log.d("StatLight-MonthLight", i + ", " + sumDay[i]);//합산 잘 되었는지 확인
            Log.d("StatLight-MonthLight", i + ", " + sumDayMor[i]);//합산 잘 되었는지 확인
            Log.d("StatLight-MonthLight", i + ", " + sumDayDinLux[i]);//합산 잘 되었는지 확인
            Log.d("StatLight-MonthLight", i + ", " + sumDayDinTemp[i]);//합산 잘 되었는지 확인
        }

        //현재시간
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyyMMdd");
        String curr = transFormat.format(calendar.getTime());
        int thisDay = calendar.get(Calendar.DAY_OF_MONTH);

        monthSumDay = sumDay;
        monthSumSun = sumDayMor;
        monthSumMoonLux = sumDayDinLux;
        monthSumMoonTemp = sumDayDinTemp;
        monthSunAvg =  monthSunAvg/(thisDay*9);
        monthMoonLuxAvg =  monthMoonLuxAvg/(thisDay*3);
        monthMoonTempAvg = monthMoonTempAvg/(thisDay*3*calendar.getActualMaximum(Calendar.DAY_OF_MONTH));


        //과다,충분,부족 별 날짜 개수
        monthMany=0;
        monthProper=0;
        monthLack=0;

        for(int i=0; i< thisDay; i++){
            Log.e("proper?", i + ", " + sumDayMor[i]);//합산 잘 되었는지 확인
            if(sumDayMor[i]>=60000) { monthProper++; }
            else if(sumDayMor[i]<60000) { monthLack++; }
        }

        //한달 전체 퍼센트를 구하기
        AtomicInteger total = new AtomicInteger();
        Flowable<Integer> flowableS = Flowable.fromArray(sumDay).to(MathFlowable::sumInt);
        flowableS.subscribe(sum -> {
            total.set(sum);
        }, Throwable::printStackTrace);
        int avg = Math.round(total.intValue()/thisDay);
        monthAvg = avg;
        monthPercent = (avg*100) / 60000;

    }


    public void parsing(List<List<RawdataDTO>> perHour, List<List<RawdataDTO>> perDay, List<List<RawdataDTO>> perMonthDay) {
        day(perHour);
        week(perDay);
        month(perMonthDay);
    }

    public void parsing(List<List<RawdataDTO>> perHour){
        day(perHour);
    }
}


