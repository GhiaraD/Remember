package com.ghiarad.dragos.myapplication;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Log;

import com.ghiarad.dragos.myapplication.OnBoarding.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.ghiarad.dragos.myapplication.App.CHANNEL_ID2;

public class ReminderService extends Service {

    boolean working = true;
    Context context = this;

    @Override
    public void onCreate() {
        super.onCreate();
        /*Intent notificationIntent = new Intent(this,MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Did you use the application today?")
                .setContentText("Talk to me")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (working){

                    try{
                        Thread.sleep(1000);

                        //compare datas not strings

                        Date currentTime = Calendar.getInstance().getTime();
                        SimpleDateFormat mdFormat = new SimpleDateFormat("HH:mm:ss");
                        String crtTime = mdFormat.format(currentTime);

                        Log.d("reminder",""+crtTime);

                        if(crtTime.compareTo("13:16:00")==0)
                        {
                            Intent notificationIntent = new Intent(context,MainActivity.class);
                            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID2)
                                    .setSmallIcon(R.drawable.logo)
                                    .setContentTitle("Remember")
                                    .setContentText("Ai folosit aplicatia azi?")
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                            notificationManager.notify(2, builder.build());

                        }

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }

            }
        }).start();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        working = false;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
