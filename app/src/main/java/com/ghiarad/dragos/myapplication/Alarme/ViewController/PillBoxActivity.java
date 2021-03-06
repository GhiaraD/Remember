package com.ghiarad.dragos.myapplication.Alarme.ViewController;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.ghiarad.dragos.myapplication.Alarme.Model.Alarm;
import com.ghiarad.dragos.myapplication.Alarme.Model.Pill;
import com.ghiarad.dragos.myapplication.Alarme.Model.PillBox;
import com.ghiarad.dragos.myapplication.Alarme.Model.PillComparator;
import com.ghiarad.dragos.myapplication.Alarme.ViewController.adapter.ExpandableListAdapter;
import com.ghiarad.dragos.myapplication.R;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class PillBoxActivity extends AppCompatActivity {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<List<List<Long>>> alarmIDData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cutia cu Pastile");
        setContentView(R.layout.activity_pill_box);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        try {
            prepareListData();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                PillBox pillBox = new PillBox();
                pillBox.setTempIds(alarmIDData.get(groupPosition).get(childPosition));

                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pill_box, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent returnHome = new Intent(getBaseContext(), AlarmeActivity.class);
        startActivity(returnHome);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void prepareListData() throws URISyntaxException {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        alarmIDData = new ArrayList<List<List<Long>>>();

        PillBox pillbox = new PillBox();
        List<Pill> pills = pillbox.getPills(this);
        Collections.sort(pills, new PillComparator());

        for (Pill pill: pills){
            String name = pill.getPillName();
            listDataHeader.add(name);
            List<String> times = new ArrayList<String>();
            List<Alarm> alarms = pillbox.getAlarmByPill(this.getBaseContext(), name);
            List<List<Long>> ids = new ArrayList<List<Long>>();

            for (Alarm alarm :alarms){
                String time = alarm.getStringTime() + daysList(alarm);
                times.add(time);
                ids.add(alarm.getIds());
            }
            alarmIDData.add(ids);
            listDataChild.put(name, times);
        }
    }

    private String daysList(Alarm alarm){
        String days = "#";
        for(int i=0; i<7; i++){
            if (alarm.getDayOfWeek()[i])
                days += "1";
            else
                days += "0";
        }
        return days;
    }

    @Override
    public void onBackPressed() {
        Intent returnHome = new Intent(getBaseContext(), AlarmeActivity.class);
        startActivity(returnHome);
        finish();
    }
}