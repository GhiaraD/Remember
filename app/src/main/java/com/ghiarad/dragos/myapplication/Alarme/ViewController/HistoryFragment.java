package com.ghiarad.dragos.myapplication.Alarme.ViewController;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ghiarad.dragos.myapplication.Alarme.Model.History;
import com.ghiarad.dragos.myapplication.Alarme.Model.PillBox;
import com.ghiarad.dragos.myapplication.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class HistoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        TableLayout stk = (TableLayout) rootView.findViewById(R.id.table_history);

        TableRow tbrow0 = new TableRow(container.getContext());

        TextView tt1 = new TextView(container.getContext());
        tt1.setText("Numele Pastilei");
        tt1.setTextColor(Color.BLACK);
        tt1.setGravity(Gravity.CENTER);
        tt1.setTypeface(null, Typeface.BOLD);
        tt1.setTextSize(20);
        tt1.setPadding(0,5,0,5);
        tbrow0.addView(tt1);

        TextView tt2 = new TextView(container.getContext());
        tt2.setText("Data");
        tt2.setTextColor(Color.BLACK);
        tt2.setGravity(Gravity.CENTER);
        tt2.setTextSize(23);
        tt2.setTypeface(null, Typeface.BOLD);
        tt2.setPadding(0,5,0,5);
        tbrow0.addView(tt2);

        TextView tt3 = new TextView(container.getContext());
        tt3.setText("Ora");
        tt3.setTextColor(Color.BLACK);
        tt3.setGravity(Gravity.CENTER);
        tt3.setTextSize(23);
        tt3.setTypeface(null, Typeface.BOLD);
        tt3.setPadding(0,5,0,5);
        tbrow0.addView(tt3);

        stk.addView(tbrow0);

        PillBox pillBox = new PillBox();

        for (History history: pillBox.getHistory(container.getContext())){
            TableRow tbrow = new TableRow(container.getContext());

            TextView t1v = new TextView(container.getContext());
            t1v.setText(history.getPillName());
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            t1v.setMaxEms(4);
            t1v.setPadding(0,5,0,5);
            tbrow.addView(t1v);

            TextView t2v = new TextView(container.getContext());
            String date = history.getDateString();
            t2v.setText(date);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            t2v.setPadding(0,5,0,5);
            tbrow.addView(t2v);

            TextView t3v = new TextView(container.getContext());

            int nonMilitaryHour = history.getHourTaken() % 12;
            if (nonMilitaryHour == 0)
                nonMilitaryHour = 12;

            String minute;
            if (history.getMinuteTaken() < 10)
                minute = "0" + history.getMinuteTaken();
            else
                minute = "" + history.getMinuteTaken();

            String time = nonMilitaryHour + ":" + minute + " " + history.getAm_pmTaken();
            t3v.setText(time);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            t3v.setPadding(0,5,0,5);
            tbrow.addView(t3v);

            stk.addView(tbrow);
         }
        return rootView;
    }
}