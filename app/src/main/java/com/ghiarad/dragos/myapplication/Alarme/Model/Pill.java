package com.ghiarad.dragos.myapplication.Alarme.Model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class Pill {
    private String pillName;
    private long pillId;
    private List<Alarm> alarms = new LinkedList<Alarm>();

    public String getPillName() { return pillName; }

    public void setPillName(String pillName) { this.pillName = pillName; }

    /**
     *
     * @param alarm
     * allows a new alarm sto be added to a preexisting alarm
     */
    public void addAlarm(Alarm alarm) {
        alarms.add(alarm);
        Collections.sort(alarms);
    }

    public long getPillId() {
        return pillId;
    }

    public void setPillId(long pillID) {
        this.pillId = pillID;
    }
}
