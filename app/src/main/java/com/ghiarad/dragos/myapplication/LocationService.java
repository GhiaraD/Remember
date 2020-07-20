package com.ghiarad.dragos.myapplication;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Objects;

import static com.ghiarad.dragos.myapplication.App.CHANNEL_ID;

public class LocationService extends Service {

    Handler handler;
    private Context context = this;
    private String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private String callNumber;
    private DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
    private  double homeLatitude=100,homeLongitude=100;
    private float homeRange = 0;
    private boolean mProcessHome = false;
    private boolean OutOfRange = false;
    public boolean working = true;

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this,MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle("Location Service")
                .setContentText("Location Service running")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mProcessHome = true;

        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessHome)
                {

                    mProcessHome = false;

                    if(dataSnapshot.child("Casa").child("Lat").getValue()!=null)homeLatitude = Double.parseDouble(dataSnapshot.child("Casa").child("Lat").getValue().toString());
                    if(dataSnapshot.child("Casa").child("Long").getValue() !=null)homeLongitude = Double.parseDouble(dataSnapshot.child("Casa").child("Long").getValue().toString());
                    homeRange = Float.parseFloat(dataSnapshot.child("Tutore").child("DistMax").getValue().toString());
                    callNumber = dataSnapshot.child("Data").child("NumarTutore").getValue().toString();
                    OutOfRange = dataSnapshot.hasChild("OutOfRange");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                Toast.makeText(LocationService.this, "5 secs has passed", Toast.LENGTH_SHORT).show();
            }

        };



        new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(working)
                {
                    try {
                        Thread.sleep(5000); // de fapt asta e timpul la care se face request-ul idk

                            Log.d("service", "0n");

                            com.ghiarad.dragos.myapplication.MyLocation.LocationResult locationResult = new com.ghiarad.dragos.myapplication.MyLocation.LocationResult() {
                                @Override
                                public void gotLocation(Location location) {

                                    Log.d("latpoz", "" + location.getLatitude());
                                    Log.d("longpoz", "" + location.getLongitude());
                                    Log.d("range", "" + homeRange);
                                    Log.d("lathome", "" + homeLatitude);
                                    Log.d("longhome", "" + homeLongitude);

                                    if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                                        double currentLatitude = location.getLatitude(), currentLongitude = location.getLongitude();

                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Lat").setValue(currentLatitude);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Long").setValue(currentLongitude);

                                        if(homeLatitude!=100 && homeLongitude!=100) {

                                            float[] results = new float[1];
                                            Location.distanceBetween(location.getLatitude(), location.getLongitude(), homeLatitude, homeLongitude, results);

                                            Log.d("distance1", "" + results[0]);

                                            //double distance = SphericalUtil.computeDistanceBetween(new LatLng(currentLatitude,currentLatitude), new LatLng(homeLatitude,homeLongitude));
                                            //Log.d("distance2",""+distance);

                                            if (results[0] > homeRange && !OutOfRange) {

                                                OutOfRange = true;
                                                mDatabaseUser.child("OutOfRange").setValue("RandomValue");

                                                String uri3 = "google.navigation:q=" + homeLatitude + "," + homeRange + "&mode=w";

                                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri3));
                                                intent.setPackage("com.google.android.apps.maps");

                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                callIntent.setData(Uri.parse("tel:" + callNumber));

                                                TaskStackBuilder.create(LocationService.this)
                                                        .addNextIntent(intent)
                                                        .addNextIntentWithParentStack(callIntent)
                                                        .startActivities();

                                            }

                                        }
                                    }

                                }
                            };
                            com.ghiarad.dragos.myapplication.MyLocation myLocation = new com.ghiarad.dragos.myapplication.MyLocation(context);
                            myLocation.getLocation(context, locationResult);



                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
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
