package com.example.android.evmap;

import android.app.Application;

public class EVapplication extends Application {
    private int amp = 20;
    public int getAmp() {
        return amp;
    }
    public void setAmp(int amp) {
        this.amp = amp;
    }
    private int v = 110;
    public int getV() {
        return v;
    }
    public void setV(int v) {
        this.v = v;
    }

    private int carIndex = 0;
    public int getCarIndex() {
        return carIndex;
    }
    public void setCarIndex(int carIndex) {
        this.carIndex = carIndex;
    }

    public boolean miles = true;
    public boolean getKmMiles() {
        return miles;
    }
    public void setKmMiles(boolean miles) {
        this.miles = miles;
    }

    private String scheduleStr = "";
    public String getScheduleStr() {
        return scheduleStr;
    }

    public void setScheduleStr(String scheduleStr) {
        this.scheduleStr = scheduleStr;
    }
}
