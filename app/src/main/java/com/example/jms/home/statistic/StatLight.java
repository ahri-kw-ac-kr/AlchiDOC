package com.example.jms.home.statistic;

import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.util.List;

public class StatLight {

    /*일간*/
    private Integer[] daySumHour; //일 시간마다 조도량 합산
    private int daySum; // 지금까지 전체 조도량 합산
    private int dayPercent; // 맨 위 하루 전체 조도량

    private int dayDaySumLux; // 낮 조도량
    private int dayEvenSumLux; // 저녁 조도량
    private int dayEvenSumTemp; // 저녁 색온도
    private int dayNightSumLux; // 야간 조도량
    private int dayNightSumTemp; //야간 색온도

    /*주간*/
    private Integer[] weekSumDay; //주 요일마다 전체 조도량 합산
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

    public StatLight() {
    }

    public StatLight(Integer[] daySumHour, int daySum, int dayPercent,
                     int dayDaySumLux, int dayEvenSumLux, int dayEvenSumTemp,
                     int dayNightSumLux, int dayNightSumTemp, Integer[] weekSumDay,
                     Integer[] weekSumSun, Integer[] weekSumMoonLux, Integer[] weekSumMoonTemp, int weekAvg,
                     int weekPercent, Integer[] monthSumDay, Integer[] monthSumSun, Integer[] monthSumMoonLux, Integer[] monthSumMoonTemp,int monthPercent) {

        this.daySumHour = daySumHour;
        this.daySum = daySum;
        this.dayPercent = dayPercent;
        this.dayDaySumLux = dayDaySumLux;
        this.dayEvenSumLux = dayEvenSumLux;
        this.dayEvenSumTemp = dayEvenSumTemp;
        this.dayNightSumLux = dayNightSumLux;
        this.dayNightSumTemp = dayNightSumTemp;

        this.weekSumDay = weekSumDay;
        this.weekSumSun = weekSumSun;
        this.weekSumMoonLux = weekSumMoonLux;
        this.weekSumMoonTemp = weekSumMoonTemp;
        this.weekAvg = weekAvg;
        this.weekPercent = weekPercent;

        this.monthSumDay = monthSumDay;
        this.monthSumSun = monthSumSun;
        this.monthSumMoonLux = monthSumMoonLux;
        this.monthSumMoonTemp = monthSumMoonTemp;
        this.monthPercent = monthPercent;
    }

    /*getter*/
    public Integer[] getDaySumHour() { return daySumHour; }

    public int getDaySum() { return daySum; }

    public int getDayPercent() { return dayPercent; }

    public int getDayDaySumLux() { return dayDaySumLux; }

    public int getDayEvenSumLux() { return dayEvenSumLux; }

    public int getDayEvenSumTemp() { return dayEvenSumTemp; }

    public int getDayNightSumLux() { return dayNightSumLux; }

    public int getDayNightSumTemp() { return dayNightSumTemp; }

    /*setter*/

    private void day(List<List<RawdataDTO>> perHour) {
    }

    private void week(List<List<RawdataDTO>> perDay) {
    }

    private void month(List<List<RawdataDTO>> perDay) {
    }


    public void parsing(List<List<RawdataDTO>> perHour, List<List<RawdataDTO>> perDay, List<List<RawdataDTO>> perMonthDay) {
        day(perHour);
        week(perDay);
        month(perMonthDay);
    }
}


