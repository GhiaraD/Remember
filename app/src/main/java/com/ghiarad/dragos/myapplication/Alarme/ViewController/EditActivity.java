package com.ghiarad.dragos.myapplication.Alarme.ViewController;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ghiarad.dragos.myapplication.Alarme.Model.Alarm;
import com.ghiarad.dragos.myapplication.Alarme.Model.Pill;
import com.ghiarad.dragos.myapplication.Alarme.Model.PillBox;
import com.ghiarad.dragos.myapplication.R;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


public class EditActivity extends AppCompatActivity {
    private AlarmManager alarmManager;
    private PendingIntent operation;
    private boolean dayOfWeekList[] = new boolean[7];

    int hour, minute;
    TextView timeLabel;
    PillBox pillBox = new PillBox();
    List<Long> tempIds = pillBox.getTempIds();
    String tempPill_name;


    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minuteOfHour) {
            hour = hourOfDay;
            minute = minuteOfHour;
            timeLabel.setText(setTime(hour, minute));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Editeaza o Alarma");

        // Set up the time string on the page
        timeLabel=(TextView)findViewById(R.id.reminder_time);
        Typeface lightFont = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        timeLabel.setTypeface(lightFont);

        // Set up the time string on the page
        // Uses the tempIds in the pill box to get the time string
        try {
            Alarm firstAlarm = pillBox.getAlarmById(getApplicationContext(), tempIds.get(0));
            hour = firstAlarm.getHour();
            minute = firstAlarm.getMinute();
            timeLabel.setText(setTime(hour, minute));
            pillBox.setTempName(firstAlarm.getPillName()) ;
            timeLabel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new TimePickerDialog(EditActivity.this,
                            t,
                            hour,
                            minute,
                            false).show();
                }
            });
            timeLabel.setText(setTime(hour, minute));

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        EditText editText = (EditText) findViewById(R.id.pill_name);
        tempPill_name = pillBox.getTempName();
        editText.setText(tempPill_name);

        for(Long id: tempIds){
            try{
                int day = pillBox.getDayOfWeek(getApplicationContext(), id);
                CheckBox checkBoxMon = (CheckBox) findViewById(R.id.checkbox_monday);
                CheckBox checkBoxTues = (CheckBox) findViewById(R.id.checkbox_tuesday);
                CheckBox checkBoxWed = (CheckBox) findViewById(R.id.checkbox_wednesday);
                CheckBox checkBoxThur = (CheckBox) findViewById(R.id.checkbox_thursday);
                CheckBox checkBoxFri = (CheckBox) findViewById(R.id.checkbox_friday);
                CheckBox checkBoxSat = (CheckBox) findViewById(R.id.checkbox_saturday);
                CheckBox checkBoxSun = (CheckBox) findViewById(R.id.checkbox_sunday);
                if(day == 2) {
                    checkBoxMon.setChecked(true);
                    dayOfWeekList[1] = true;
                } else if(day == 3) {
                    checkBoxTues.setChecked(true);
                    dayOfWeekList[2] = true;
                } else if(day == 4) {
                    checkBoxWed.setChecked(true);
                    dayOfWeekList[3] = true;
                } else if(day == 5) {
                    checkBoxThur.setChecked(true);
                    dayOfWeekList[4] = true;
                } else if(day == 6) {
                    checkBoxFri.setChecked(true);
                    dayOfWeekList[5] = true;
                } else if(day == 7) {
                    checkBoxSat.setChecked(true);
                    dayOfWeekList[6] = true;
                } else if(day == 1) {
                    checkBoxSun.setChecked(true);
                    dayOfWeekList[0] = true;
                }
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        View.OnClickListener setClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int checkBoxCounter = 0;

                EditText editText = (EditText) findViewById(R.id.pill_name);
                String pill_name = editText.getText().toString();

                Alarm alarm = new Alarm();

                if (!pillBox.pillExist(getApplicationContext(), pill_name)) {
                    Pill pill = new Pill();
                    pill.setPillName(pill_name);
                    alarm.setHour(hour);
                    alarm.setMinute(minute);
                    alarm.setPillName(pill_name);
                    alarm.setDayOfWeek(dayOfWeekList);
                    pill.addAlarm(alarm);
                    long pillId = pillBox.addPill(getApplicationContext() ,pill);
                    pill.setPillId(pillId);
                    pillBox.addAlarm(getApplicationContext(), alarm, pill);
                } else { // If Pill already exists
                    Pill pill = pillBox.getPillByName(getApplicationContext(), pill_name);
                    alarm.setHour(hour);
                    alarm.setMinute(minute);
                    alarm.setPillName(pill_name);
                    alarm.setDayOfWeek(dayOfWeekList);
                    pill.addAlarm(alarm);
                    pillBox.addAlarm(getApplicationContext(), alarm, pill);
                }
                List<Long> ids = new LinkedList<Long>();
                try {
                    List<Alarm> alarms = pillBox.getAlarmByPill(getApplicationContext(), pill_name);
                    for(Alarm tempAlarm: alarms) {
                        if(tempAlarm.getHour() == hour && tempAlarm.getMinute() == minute) {
                            ids = tempAlarm.getIds();
                            break;
                        }
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                for(int i=0; i<7; i++) {
                    if (dayOfWeekList[i] && pill_name.length() != 0) {

                        int dayOfWeek = i+1;

                        long _id = ids.get(checkBoxCounter);
                        int id = (int) _id;
                        checkBoxCounter++;

                        Intent intent = new Intent(getBaseContext(), AlertActivity.class);
                        intent.putExtra("pill_name", pill_name);

                        operation = PendingIntent.getActivity(getBaseContext(), id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);

                        Calendar calendar = Calendar.getInstance();

                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);

                        long alarm_time = calendar.getTimeInMillis();

                        if (calendar.before(Calendar.getInstance()))
                            alarm_time += AlarmManager.INTERVAL_DAY * 7;

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarm_time,
                                AlarmManager.INTERVAL_DAY * 7, operation);
                    }
                }
                if(checkBoxCounter == 0 || pill_name.length() == 0)
                    Toast.makeText(getBaseContext(), "Please input a pill name or check at least one day!", Toast.LENGTH_SHORT).show();
                else {
                    for (long alarmID : tempIds) {
                        pillBox.deleteAlarm(getApplicationContext(), alarmID);

                        Intent intent = new Intent(getBaseContext(), AlertActivity.class);
                        PendingIntent operation = PendingIntent.getActivity(getBaseContext(), (int) alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(operation);
                    }

                    try {
                        List<Alarm> tempTracker = pillBox.getAlarmByPill(getBaseContext(), tempPill_name);
                        if(tempTracker.size() == 0)
                            pillBox.deletePill(getBaseContext(), tempPill_name);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(getBaseContext(), "Alarm for " + pill_name + " is set successfully", Toast.LENGTH_SHORT).show();
                    Intent returnHome = new Intent(getBaseContext(), AlarmeActivity.class);
                    startActivity(returnHome);
                    finish();
                }
            }
        };

        View.OnClickListener cancelClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnHome = new Intent(getBaseContext(), PillBoxActivity.class);
                startActivity(returnHome);
                finish();
            }
        };

        Button btnSetAlarm = (Button) findViewById(R.id.btn_set_alarm);
        btnSetAlarm.setOnClickListener(setClickListener);

        Button btnQuitAlarm = (Button) findViewById(R.id.btn_cancel_alarm);
        btnQuitAlarm.setOnClickListener(cancelClickListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox_monday:
                if (checked)
                    dayOfWeekList[1] = true;
                else
                    dayOfWeekList[1] = false;
                break;
            case R.id.checkbox_tuesday:
                if (checked)
                    dayOfWeekList[2] = true;
                else
                    dayOfWeekList[2] = false;
                break;
            case R.id.checkbox_wednesday:
                if (checked)
                    dayOfWeekList[3] = true;
                else
                    dayOfWeekList[3] = false;
                break;
            case R.id.checkbox_thursday:
                if (checked)
                    dayOfWeekList[4] = true;
                else
                    dayOfWeekList[4] = false;
                break;
            case R.id.checkbox_friday:
                if (checked)
                    dayOfWeekList[5] = true;
                else
                    dayOfWeekList[5] = false;
                break;
            case R.id.checkbox_saturday:
                if (checked)
                    dayOfWeekList[6] = true;
                else
                    dayOfWeekList[6] = false;
                break;
            case R.id.checkbox_sunday:
                if (checked)
                    dayOfWeekList[0] = true;
                else
                    dayOfWeekList[0] = false;
                break;
            case R.id.every_day:
                LinearLayout ll = (LinearLayout) findViewById(R.id.checkbox_layout);
                for (int i=0; i < ll.getChildCount(); i++) {
                    View v = ll.getChildAt(i);
                    ((CheckBox) v).setChecked(checked);
                    onCheckboxClicked(v);
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            for (long alarmID : tempIds) {
                pillBox.deleteAlarm(getApplicationContext(), alarmID);

                Intent intent = new Intent(getBaseContext(), AlertActivity.class);
                PendingIntent operation = PendingIntent.getActivity(getBaseContext(), (int) alarmID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(ALARM_SERVICE);
                alarmManager.cancel(operation);
            }

            // Delete the pill if there is no alarm for it
            try {
                List<Alarm> tempTracker = pillBox.getAlarmByPill(getBaseContext(), tempPill_name);
                if(tempTracker.size() == 0)
                    pillBox.deletePill(getBaseContext(), tempPill_name);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            Intent returnPillBox = new Intent(getBaseContext(), PillBoxActivity.class);
            startActivity(returnPillBox);
            finish();

            Toast.makeText(getBaseContext(), "Alarm for " + tempPill_name + " is deleted successfully", Toast.LENGTH_SHORT).show();
            return true;
        }
        Intent returnHome = new Intent(getBaseContext(), PillBoxActivity.class);
        startActivity(returnHome);
        finish();
        return super.onOptionsItemSelected(item);
    }

    public String setTime(int hour, int minute) {
        String am_pm = (hour < 12) ? "am" : "pm";
        int nonMilitaryHour = hour % 12;
        if (nonMilitaryHour == 0)
            nonMilitaryHour = 12;
        String minuteWithZero;
        if (minute < 10)
            minuteWithZero = "0" + minute;
        else
            minuteWithZero = "" + minute;
        return nonMilitaryHour + ":" + minuteWithZero + am_pm;
    }

    @Override
    public void onBackPressed() {
        Intent returnPillBoxActivity = new Intent(getBaseContext(), PillBoxActivity.class);
        startActivity(returnPillBoxActivity);
        finish();
    }
}