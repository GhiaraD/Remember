package com.ghiarad.dragos.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetariLocatie extends AppCompatActivity {

    private Switch switchPerimetru;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
    private boolean mProcessInit = false;
    Intent serviceIntent;
    TextView setHome;
    private Context context = this;
    boolean mProcessSetHome=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari_locatie);

        switchPerimetru = (Switch) findViewById(R.id.activeazaPerimetru);
        setHome = (TextView) findViewById(R.id.sethome);

        mProcessInit = true;
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessInit)
                {

                    mProcessInit = false;

                    if(dataSnapshot.hasChild("TrackingNow"))
                    {
                        switchPerimetru.setChecked(true);
                    }
                    else
                        switchPerimetru.setChecked(false);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        switchPerimetru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switchPerimetru.isChecked())
                {

                    startService();
                    mDatabaseUser.child("TrackingNow").setValue("RandomValue");

                }
                else{

                    stopService();
                    mDatabaseUser.child("TrackingNow").removeValue();

                }

            }
        });

        //la chestia asta ar trebui sa verifice daca esti conectat la net
        setHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(SetariLocatie.this)
                        .setTitle("Set your home here")
                        .setMessage("You are about to set your home here. Are you sure you want to do this?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mProcessSetHome = true;

                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(mProcessSetHome)
                                        {
                                            mProcessSetHome = false;
                                            mDatabaseUser.child("Casa").child("Lat").setValue(dataSnapshot.child("Lat").getValue().toString());
                                            mDatabaseUser.child("Casa").child("Long").setValue(dataSnapshot.child("Long").getValue().toString());

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();

            }
        });
    }

    public void startService(){

        serviceIntent = new Intent(this,LocationService.class);
        ContextCompat.startForegroundService(this,serviceIntent);

    }

    public void stopService( ){

        serviceIntent = new Intent(this,LocationService.class);
        stopService(serviceIntent);

    }

}
