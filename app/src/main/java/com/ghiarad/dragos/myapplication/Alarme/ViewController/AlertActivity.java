package com.ghiarad.dragos.myapplication.Alarme.ViewController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import android.widget.Toast;

import com.ghiarad.dragos.myapplication.Alarme.Model.History;
import com.ghiarad.dragos.myapplication.Alarme.Model.Pill;
import com.ghiarad.dragos.myapplication.Alarme.Model.PillBox;
import com.ghiarad.dragos.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlertActivity extends FragmentActivity {

    private AlarmManager alarmManager;
    private PendingIntent operation;
    private DatabaseReference db;
    boolean mProcess;
    private Vibrator v;
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        AlertAlarm alert = new AlertAlarm();
        alert.show(getSupportFragmentManager(), "AlertAlarm");
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        long[] mVibratePattern = new long[]{500, 500, 500, 500};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int[] mVibrateAmplitude = new int[]{0, VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE};
            v.vibrate(VibrationEffect.createWaveform(mVibratePattern, mVibrateAmplitude,0));

        } else {
            v.vibrate(mVibratePattern, 0);
        }

        mPlayer = MediaPlayer.create(AlertActivity.this, R.raw.cuco_sound);
        mPlayer.setLooping(true);
        mPlayer.start();

    }

    // I took it
    public void doPositiveClick(final String pillName){
        PillBox pillBox = new PillBox();
        Pill pill = pillBox.getPillByName(this, pillName);
        History history = new History();

        v.cancel();
        mPlayer.stop();
        mPlayer.release();

        Calendar takeTime = Calendar.getInstance();
        Date date = takeTime.getTime();
        final String dateString = new SimpleDateFormat("MMM d, yyyy").format(date);

        final int hour = takeTime.get(Calendar.HOUR_OF_DAY);
        final int minute = takeTime.get(Calendar.MINUTE);
        String am_pm = (hour < 12) ? "am" : "pm";

        history.setHourTaken(hour);
        history.setMinuteTaken(minute);
        history.setDateString(dateString);
        history.setPillName(pillName);

        mProcess = true;

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcess) {

                    mProcess = false;

                    if(dataSnapshot.hasChild("Medicine")) {
                        db.child("Medicine").child(dataSnapshot.child("Medicine").getChildrenCount() + "")
                                .child("nume").setValue(pillName);

                        db.child("Medicine").child(dataSnapshot.child("Medicine").getChildrenCount() + "")
                                .child("data").setValue(dateString);

                        db.child("Medicine").child(dataSnapshot.child("Medicine").getChildrenCount() + "")
                                .child("ora").setValue(hour + ":" + minute);
                    }
                    else
                    {
                        db.child("Medicine").child("0").child("nume").setValue(pillName);

                        db.child("Medicine").child("0").child("data").setValue(dateString);

                        db.child("Medicine").child("0").child("ora").setValue(hour + ":" + minute);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        pillBox.addToHistory(this, history);

        String stringMinute;
        if (minute < 10)
            stringMinute = "0" + minute;
        else
            stringMinute = "" + minute;

        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;

        Toast.makeText(getBaseContext(),  pillName + " was taken at "+ nonMilitaryHour + ":" + stringMinute + " " + am_pm + ".", Toast.LENGTH_SHORT).show();

        Intent returnHistory = new Intent(getBaseContext(), AlarmeActivity.class);
        startActivity(returnHistory);
        finish();
    }

}