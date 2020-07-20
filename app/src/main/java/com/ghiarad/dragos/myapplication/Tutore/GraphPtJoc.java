package com.ghiarad.dragos.myapplication.Tutore;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ghiarad.dragos.myapplication.MyMarkerView;
import com.ghiarad.dragos.myapplication.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class GraphPtJoc extends AppCompatActivity {

    private LineChart chart;
    private DatabaseReference dbRef;
    boolean mProcess;
    final ArrayList<Entry> values = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_graph_pt_joc);
        setTitle("Graficul progresului pacientului");

        dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chart = findViewById(R.id.chart1);

        mProcess = true;
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcess) {

                    mProcess=false;

                    int val = 0;
                    int i = 0;

                    for (DataSnapshot snap : dataSnapshot.child("Grafic").getChildren()) {

                        String valoare = snap.getValue().toString();
                        String[] secunde = valoare.split(":");

                        if (secunde[0].equals("00")) {
                            val = Integer.parseInt(secunde[1]);
                        } else {
                            val = Integer.parseInt(secunde[1]) + Integer.parseInt(secunde[0]) * 60;
                        }

                        Log.d("val", val+"");

                        values.add(new Entry(i, val));
                        i++;

                    }

                    Collections.sort(values, new EntryXComparator());
                    LineDataSet set1 = new LineDataSet(values, "DataSet 1");
                    // set1.setFillAlpha(110);
                    // set1.setFillColor(Color.RED);

                    set1.setLineWidth(1.75f);
                    set1.setCircleRadius(5f);
                    set1.setCircleHoleRadius(2.5f);
                    set1.setColor(Color.WHITE);
                    set1.setCircleColor(Color.WHITE);
                    set1.setHighLightColor(Color.WHITE);
                    set1.setDrawValues(false);

                    // create a data object with the data sets
                    LineData data = new LineData(set1);
                    setupChart(chart, data, colors[1 % colors.length]);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private final int[] colors = new int[] {
            Color.rgb(128, 185, 239)
    };

    private void setupChart(LineChart chart, LineData data, int color) {

        ((LineDataSet) data.getDataSetByIndex(0)).setCircleHoleColor(color);

        // no description text
        chart.getDescription().setEnabled(false);

        // chart.setDrawHorizontalGrid(false);
        //
        // enable / disable grid background
        chart.setDrawGridBackground(false);
//        chart.getRenderer().getGridPaint().setGridColor(Color.WHITE & 0x70FFFFFF);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        chart.setBackgroundColor(color);

        // set custom chart offsets (automatic offset calculation is hereby disabled)
        chart.setViewPortOffsets(10, 0, 10, 0);

        // add data
        Log.d("numar datasets", data.getDataSetCount()+"");
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();
        l.setEnabled(false);

        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
        chart.setMarker(mv); // Set the marker to the chart

        chart.getAxisLeft().setEnabled(true);
        chart.getAxisLeft().setSpaceTop(10);
        chart.getAxisLeft().setSpaceBottom(10);
        chart.getAxisRight().setEnabled(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setDrawGridLines(true);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getXAxis().setEnabled(true);

        // animate calls invalidate()...
        chart.animateX(2000);
    }


    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_info)
        {
            startActivity(new Intent(GraphPtJoc.this, SetariTutore.class));
        }
        return super.onOptionsItemSelected(item);
    }*/
}
