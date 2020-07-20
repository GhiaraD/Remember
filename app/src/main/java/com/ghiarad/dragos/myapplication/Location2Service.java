package com.ghiarad.dragos.myapplication;

import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


//observer pattern
//publisher subscribe pattern
//symetric key encription (for messages)

public class Location2Service extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE="edmt.dev.googlelocationbackground.UPDATE_LOCATION";
    private  double homeLatitude=200,homeLongitude=200;
    private float homeRange = 0;
    private boolean mProcessHome = false;
    private boolean OutOfRange = false;
    private String callNumber;
    private String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    private DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
    DatabaseHelper mDatabaseHelper;

    @Override
    public void onReceive(final Context context, Intent intent) {

        if(intent != null)
            {

            final String action = intent.getAction();
            if(ACTION_PROCESS_UPDATE.equals(action))
            {

                LocationResult result = LocationResult.extractResult(intent);
                if(result != null)
                {

                    final Location location = result.getLastLocation();
                    String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();

                    Log.d("Locatie",location_string);

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

                                Log.d("latpoz", "" + location.getLatitude());
                                Log.d("longpoz", "" + location.getLongitude());
                                Log.d("range", "" + homeRange);
                                Log.d("lathome", "" + homeLatitude);
                                Log.d("longhome", "" + homeLongitude);

                                mDatabaseHelper = new DatabaseHelper(context, "me_table");
                                Cursor data = mDatabaseHelper.getStare();
                                data.moveToFirst();

                                if(FirebaseAuth.getInstance().getCurrentUser() != null) {

                                    double currentLatitude = location.getLatitude(), currentLongitude = location.getLongitude();

                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Lat").setValue(currentLatitude);
                                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Long").setValue(currentLongitude);

                                    if(homeLatitude!=200 && homeLongitude!=200) {

                                        float[] results = new float[1];
                                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), homeLatitude, homeLongitude, results);

                                        Log.d("distance1", "" + results[0]);

                                        //double distance = SphericalUtil.computeDistanceBetween(new LatLng(currentLatitude,currentLatitude), new LatLng(homeLatitude,homeLongitude));
                                        //Log.d("distance2",""+distance);

                                        if (results[0] > homeRange && !OutOfRange && data.getString(0).compareTo("pacient")==0) {

                                            OutOfRange = true;
                                            mDatabaseUser.child("OutOfRange").setValue("RandomValue");

                                            String uri3 = "google.navigation:q=" + homeLatitude + "," + homeRange + "&mode=w";

                                            Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(uri3));
                                            intent2.setPackage("com.google.android.apps.maps");

                                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                                            callIntent.setData(Uri.parse("tel:" + callNumber));

                                            TaskStackBuilder.create(context)
                                                    .addNextIntent(intent2)
                                                    .addNextIntentWithParentStack(callIntent)
                                                    .startActivities();

                                        }

                                    }
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

        }

    }

}
