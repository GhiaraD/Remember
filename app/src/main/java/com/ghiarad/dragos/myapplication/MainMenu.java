package com.ghiarad.dragos.myapplication;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.ghiarad.dragos.myapplication.Alarme.ViewController.AlarmeActivity;
import com.ghiarad.dragos.myapplication.Galeria.MainGallery;
import com.ghiarad.dragos.myapplication.Logarea.Login;
import com.ghiarad.dragos.myapplication.NOTITE.ListDataActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Method;

public class MainMenu extends AppCompatActivity {
    LinearLayout noti,alarm,despre_mine,urgenta,galerie,locatie;
    EditText tel;
    FirebaseAuth mAuth;
    boolean mProcess,mProcessSetHome,dialogExists;
    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
    private boolean mProcessCod = false;
    Intent serviceIntent;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Typeface font = Typeface.createFromAsset( getAssets(), "fontawesome-webfont.ttf" );
        TextView button = (TextView) findViewById( R.id.icon_jurnal );
        button.setTypeface(font);
        TextView button2 = (TextView) findViewById( R.id.icon_joc );
        button2.setTypeface(font);
        TextView button3 = (TextView) findViewById( R.id.icon_galerie);
        button3.setTypeface(font);
        TextView button4 = (TextView) findViewById( R.id.icon_alarme );
        button4.setTypeface(font);
        TextView button5 = (TextView) findViewById( R.id.icon_acasa);
        button5.setTypeface(font);
        TextView button6 = (TextView) findViewById( R.id.icon_urgente );
        button6.setTypeface(font);

        mAuth = FirebaseAuth.getInstance();

        noti= (LinearLayout) findViewById(R.id.note);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent not = new Intent(MainMenu.this, ListDataActivity.class);
                startActivity(not);
            }
        });


        alarm= (LinearLayout) findViewById(R.id.alarme);
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alar = new Intent(MainMenu.this, AlarmeActivity.class);
                startActivity(alar);
            }
        });

        despre_mine =(LinearLayout)  findViewById(R.id.despre_mine);
        despre_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eu= new Intent(MainMenu.this, JocMemorie.class);
                startActivity(eu);
            }
        });

        urgenta =(LinearLayout)  findViewById(R.id.urgenta);

        urgenta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                mProcess = true;

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Data").child("NumarTutore").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcess) {

                            mProcess = false;

                            String nr = dataSnapshot.getValue().toString().trim();

                            Intent da = new Intent(Intent.ACTION_CALL);
                            da.setData(Uri.parse("tel:" + nr));
                            startActivity(da);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        galerie = (LinearLayout)findViewById(R.id.galerie);
        galerie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent da = new Intent(MainMenu.this, MainGallery.class);
                startActivity(da);

            }
        });

        locatie = (LinearLayout)findViewById(R.id.locatie);
        locatie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchLocation();

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

                        Toast.makeText(MainMenu.this, "The application does not have permission for the location", Toast.LENGTH_SHORT).show();
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

        Intent intent = new Intent(this,Location2Service.class);
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.profil)
        {
            startActivity(new Intent(MainMenu.this, SeeProfile.class));
        }
        if (id == R.id.logout)
        {

            stopLocationService();
            mDatabaseUser.child("TrackingNow").removeValue();
            mAuth.signOut();
            startActivity(new Intent(MainMenu.this, Login.class));
        }
        if(id == R.id.setarilocatie)
        {

            launchSettings();

        }
        return super.onOptionsItemSelected(item);
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

            dialogExists = true;

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainMenu.this);

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
            dialogExists = true;
            Log.d("mobile data","off");

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainMenu.this);

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
                    Intent da = new Intent(MainMenu.this, MapsActivity.class);
                    da.putExtra("stare",false);
                    startActivity(da);
        }

    }

    public void stopLocationService( ){

        serviceIntent = new Intent(this,LocationService.class);
        stopService(serviceIntent);

    }

    public void launchSettings()
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

            dialogExists = true;

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainMenu.this);

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
            dialogExists = true;
            Log.d("mobile data","off");

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainMenu.this);

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

            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(MainMenu.this);

            View mView = getLayoutInflater().inflate(R.layout.dialog_enter_code,null);

            final Button check ;
            final EditText codIntrodus;
            final TextView codGresit;

            codGresit = (TextView) mView.findViewById(R.id.codgresit);
            check = (Button) mView.findViewById(R.id.checkcode);
            codIntrodus = (EditText) mView.findViewById(R.id.codintrodus);

            mBuilder.setView(mView);
            final android.app.AlertDialog dialog = mBuilder.create();
            dialog.show();

            check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mProcessCod = true;

                    mDatabaseUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(mProcessCod) {

                                mProcessCod = false;

                                String cod = codIntrodus.getText().toString();

                                if (dataSnapshot.child("Tutore").child("CodTutore").getValue().toString().compareTo(cod) == 0) {

                                    dialog.dismiss();
                                    startActivity(new Intent(MainMenu.this, SetariLocatie.class));

                                } else {

                                    codGresit.setVisibility(View.VISIBLE);

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        finish();
    }

}
