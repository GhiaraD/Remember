package com.ghiarad.dragos.myapplication.Alarme.ViewController.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ghiarad.dragos.myapplication.Alarme.ViewController.HistoryFragment;
import com.ghiarad.dragos.myapplication.Alarme.ViewController.TodayFragment;
import com.ghiarad.dragos.myapplication.Alarme.ViewController.TomorrowFragment;

public class TabsAdapter extends FragmentPagerAdapter {

    private int TOTAL_TABS = 3;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new HistoryFragment();
            case 1:
                return new TodayFragment();
            case 2:
                return new TomorrowFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }
}
