package com.example.jms.home.statistic;

public class StatLight {
    private Integer[] daySumHour; //시간마다 조도량 합산
    private int daySum; // 지금까지 전체 조도량 합산

    private int dayPercent; // 맨 위 하루 전체 조도량
    private int dayDaySumLux; // 낮 조도량
    private int dayEvenSumLux; // 저녁 조도량
    private int dayEvenSumTemp; // 저녁 색온도
    private int dayNightSumLux; // 야간 조도량
    private int dayNightSumTemp; //야간 색온도

    private Integer[] weekSumDay; //주 전체 조도량 합산
    private Integer[] weekSumSun; //주간 낮 조도량 (오전 9시 ~ 오후 6시)
    private Integer[] weekSumMoon; //주간 밤 조도량 (오후 6시 ~ 오후 9시)
    private int weekAvg;
    private int weekPercent;

    private Integer[] monthSumDay;




    public StatLight(){}
    public StatLight(Integer[] daySumHour, int daySum, int dayPercent,
                     int dayDaySumLux, int dayEvenSumLux, int dayEvenSumTemp,
                     int dayNightSumLux, int dayNightSumTemp){
    this.daySumHour = daySumHour;
    this.daySum = daySum;
    this.dayPercent = dayPercent;
    this.dayDaySumLux = dayDaySumLux;
    this.dayEvenSumLux = dayEvenSumLux;
    this.dayEvenSumTemp = dayEvenSumTemp;
    this.dayNightSumLux = dayNightSumLux;
    this.dayNightSumTemp = dayNightSumTemp;
    }

    public Integer[] getDaySumHour() {return daySumHour;}
    public int getDaySum() {return daySum;}
    public int getDayPercent() {return dayPercent;}
    public int getDayDaySumLux() {return dayDaySumLux;}
}


