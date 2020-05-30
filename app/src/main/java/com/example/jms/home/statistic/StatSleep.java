package com.example.jms.home.statistic;

import com.example.jms.connection.model.dto.SleepDTO;
import com.example.jms.connection.sleep_doc.dto.RawdataDTO;

import java.util.List;

public class StatSleep {
    private int deep; //깊은수면
    private int light; // 얕은수면
    private int turn; // 뒤척임 수
    private int wake; // 깨어남 수
    private int turnHour; // 1시간 당 뒤척인 정도가 정상, 주의, 관리 인지 구별... 0:정상, 1:주의, 2:관리필요
    private int total; // 총 수면시간

    public StatSleep(){}
    public StatSleep(int deep, int light, int turn, int wake, int turnHour, int total){
        this.deep = deep;
        this.light = light;
        this.turn = turn;
        this.wake = wake;
        this.turnHour = turnHour;
        this.total = total;
    }

    public int getDeep() { return deep; }
    public void setDeep(int deep) { this.deep = deep; }

    public int getLight() { return light; }
    public void setLight(int light) { this.light = light; }

    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }

    public int getWake() { return wake; }
    public void setWake(int wake) { this.wake = wake; }

    public int getTurnHour() { return turnHour; }
    public void setTurnHour(int turnHour) { this.turnHour = turnHour; }

    public int getTotal() { return total; }
    public void setTotal(int total) { this.total = total; }

    public static SleepDTO analyze(List<RawdataDTO> data){
        SleepDTO sleepDTO = new SleepDTO();

        int deepS = 0;
        int lightS = 0;
        int turnS = 0;
        int wakeS = 0;
        int totalS = 0;
        int deepFlag = 0;

        sleepDTO.setTurnHour(0); //0은 정상, 1은 주의, 2는 관리필요

        for(int i=0; i<data.size()-5; i++){
            for(int j=0; j<6; j++) {
                int x = data.get(i+j).getVectorX();
                int y = data.get(i+j).getVectorY();
                int z = data.get(i+j).getVectorZ();
                if(x==0 && y==0 && z==0){ deepFlag += 1; }
                else if(x!=0 || y!=0 || z!=0){ break; }
            }
            if(deepFlag == 6){
                deepS += 10;
                deepFlag = 0;
            }
            else{
                lightS += 10;
                deepFlag = 0;
            }
            int isTurn = data.get(i).getVectorX()+data.get(i).getVectorY()+data.get(i).getVectorZ();
            int isWake = data.get(i).getSteps();
            if(isTurn>=2){
                turnS = turnS + (isTurn/2);
                if((isTurn/2)>10 && (isTurn/2) <16){ sleepDTO.setTurnHour(1); }
                else if((isTurn/2)>15){ sleepDTO.setTurnHour(2); }
            }
            if(isWake>=30){ wakeS += 1; } //깨어남의 기준... 스텝이 30넘으면...?? 잘 모르겠다
            totalS += 10;
        }

        sleepDTO.setDeep(deepS);
        sleepDTO.setLight(lightS);
        sleepDTO.setTurn(turnS);
        sleepDTO.setWake(wakeS);
        sleepDTO.setTotal(totalS);

        return sleepDTO;
    }
}
