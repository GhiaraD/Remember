package com.ghiarad.dragos.myapplication.Tutore;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ghiarad.dragos.myapplication.AlertService;
import com.ghiarad.dragos.myapplication.Location2Service;
import com.ghiarad.dragos.myapplication.MapsActivity;
import com.ghiarad.dragos.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Method;

public class TutoreMain extends AppCompatActivity {

    FirebaseAuth mAuth;
    LinearLayout medicine,locationCheck,graph,stopAlert;
    Intent serviceIntent;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutore_main);

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        TextView button = (TextView) findViewById( R.id.icon_grafic );
        button.setTypeface(font);
        TextView button4 = (TextView) findViewById( R.id.icon_pastila );
        button4.setTypeface(font);
        TextView button5 = (TextView) findViewById( R.id.icon_locatie);
        button5.setTypeface(font);
        TextView button6 = (TextView) findViewById( R.id.icon_urgente );
        button6.setTypeface(font);

        medicine = (LinearLayout) findViewById(R.id.medicineCheck);
        locationCheck = (LinearLayout) findViewById(R.id.locationCheck);
        graph = (LinearLayout) findViewById(R.id.progressCheck);
        stopAlert = (LinearLayout) findViewById(R.id.stopalert);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("OutOfRange") )
                    stopAlert.setVisibility(View.VISIBLE);
                else
                    stopAlert.setVisibility((View.INVISIBLE));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TutoreMain.this, MedicineCheck.class));

            }
        });

        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutoreMain.this, GraphPtJoc.class));
            }
        });

        locationCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchLocation();

            }
        });

        stopAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Snackbar.make(v,"Alerta a fost oprită.",Snackbar.LENGTH_LONG).setAction("Repornește", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startService();

                    }
                }).show();
                stopService();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tutore").child("AlertaPacient").removeValue();
                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("OutOfRange").removeValue();

            }
        });

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        updateLocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        Toast.makeText(TutoreMain.this, "Aplicatia nu are permisiune pentru locatie", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                }).check();

    }

    private void updateLocation() {
        buildLocationRequest();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {

        Intent intent = new Intent(this, Location2Service.class);
        intent.setAction(Location2Service.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void buildLocationRequest(){

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);

    }


    public void stopService( ){

        serviceIntent = new Intent(this, AlertService.class);
        stopService(serviceIntent);

    }

    public void startService( ){

        serviceIntent = new Intent(this, AlertService.class);
        ContextCompat.startForegroundService(this,serviceIntent);

    }

    public void launchLocation()
    {

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }


        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled)
        {

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(TutoreMain.this);

            View mView = getLayoutInflater().inflate(R.layout.dialog_turnon2,null);

            final Button go ;

            go = (Button) mView.findViewById(R.id.butonpornitilocatie);

            mBuilder.setView(mView);
            final android.app.AlertDialog dialog = mBuilder.create();
            dialog.show();


            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                }
            });

        }

        else if(!mobileDataEnabled)
        {
            Log.d("mobile data","off");

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(TutoreMain.this);

            View mView = getLayoutInflater().inflate(R.layout.dialog_turnon,null);

            final Button go ;

            go = (Button) mView.findViewById(R.id.butonpornitidatele);

            mBuilder.setView(mView);
            final android.app.AlertDialog dialog = mBuilder.create();
            dialog.show();



            go.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    dialog.dismiss();

                    Intent intent = new Intent();
                    intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                    startActivity(intent);


                }
            });

        }

        else
        {


            Intent da = new Intent(TutoreMain.this, MapsActivity.class);
            da.putExtra("stare",true);
            startActivity(da);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tutore, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.setari)
        {
            startActivity(new Intent(TutoreMain.this, SetariTutore.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        finish();
    }
}
