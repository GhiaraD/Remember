package com.ghiarad.dragos.myapplication.Alarme.Model;

import java.util.Comparator;


public class PillComparator implements Comparator<Pill> {

    @Override
    public int compare(Pill pill1, Pill pill2){

        String firstName = pill1.getPillName();
        String secondName = pill2.getPillName();
        return firstName.compareTo(secondName);
    }
}
