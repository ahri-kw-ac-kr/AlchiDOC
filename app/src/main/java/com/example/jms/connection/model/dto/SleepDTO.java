package com.example.jms.connection.model.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by leegi on 2020-03-31.
 */

public class SleepDTO implements Serializable {

    private Long id;
    private Date createdAt;
    private String sleepTime;
    private String wakeTime;
    private int deep;
    private int light;
    private int turn;
    private int wake;
    private UserDTO user;

    public SleepDTO(){    }

    public SleepDTO(Long id, Date createdAt, String sleepTime, String wakeTime, int deep, int light, int turn, int wake, UserDTO user){
        this.id = id;
        this.createdAt = createdAt;
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.deep = deep;
        this.light = light;
        this.turn = turn;
        this.wake = wake;
        this.user = user;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public Date getCreatedAt(){ return createdAt; }
    public void setCreatedAt(Date createdAt){ this.createdAt = createdAt; }

    public String getSleepTime(){ return sleepTime;}
    public void setSleepTime(String sleepTime){ this.sleepTime = sleepTime; }

    public String getWakeTime(){ return wakeTime;}
    public void setWakeTime(String wakeTime){ this.wakeTime = wakeTime; }

    public int getDeep() { return deep; }
    public void setDeep(int deep) { this.deep = deep; }

    public int getLight() { return light; }
    public void setLight(int light) { this.light = light; }

    public int getTurn() { return turn; }
    public void setTurn(int turn) { this.turn = turn; }

    public int getWake() { return wake; }
    public void setWake(int wake) { this.wake = wake; }

    public UserDTO getUser(){ return user;}
    public void setUser(UserDTO user){ this.user = user; }
}
