package com.example.userapplication.Classes;

public class Reward {
    private String reward;
    private String id_menu;
    private int stamp;

    public Reward(String reward, String id_menu, int stamp) {
        this.reward = reward;
        this.id_menu = id_menu;
        this.stamp = stamp;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getId_menu() {
        return id_menu;
    }

    public void setId_menu(String id_menu) {
        this.id_menu = id_menu;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }
}
