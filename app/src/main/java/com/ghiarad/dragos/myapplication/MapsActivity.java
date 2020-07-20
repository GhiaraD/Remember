package com.ghiarad.dragos.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.ghiarad.dragos.myapplication.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;


public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    LinearLayout Urgenta;
    Marker pozMarker = null,Locuinta;
    Polyline currentPolyline;
    DatabaseReference dbRef;
    DatabaseHelper da;
    Boolean mProcessAcasa,mProcessInit;
    float maxDistanceAllowed = -1;
    private Context context = this;
    DatabaseHelper mDatabaseHelper;
    Boolean mProcessZoom=false;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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

        /**final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkfunc();
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);*/

        Urgenta = (LinearLayout) findViewById(R.id.sethome);
        mDatabaseHelper = new DatabaseHelper(this, "me_table");
        Cursor data = mDatabaseHelper.getStare();
        data.moveToFirst();
        if(data.getString(0).compareTo("tutore")==0)Urgenta.setVisibility(View.GONE);
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Data");
        da = new DatabaseHelper(this, "me_table");

        Urgenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String uri = "http://maps.google.com/maps?saddr=" + lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude() + "&daddr=" + xCasa + "," + yCasa;

                //String uri2 = "google.navigation:q="+lastKnownLocation.getLatitude()+","+lastKnownLocation.getLongitude()+"&daddr="+xCasa+","+yCasa;

                mProcessAcasa = true;

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Casa").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessAcasa)
                        {

                            mProcessAcasa = false;

                            if(dataSnapshot.hasChild("Lat")&&dataSnapshot.hasChild("Long"))
                            {

                                String uri3 = "google.navigation:q=" + dataSnapshot.child("Lat").getValue().toString() + "," + dataSnapshot.child("Long").getValue().toString() + "&mode=w";

                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri3));
                                intent.setPackage("com.google.android.apps.maps");
                                startActivity(intent);

                            }
                            else
                                Toast.makeText(getApplicationContext(),"You haven't set your home yet",Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Toast.makeText(this,"Se încarcă...",Toast.LENGTH_SHORT).show();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);


        zoomOnStart();



        mProcessInit = true;

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("Casa").hasChild("Lat") && dataSnapshot.child("Casa").hasChild("Long") && mProcessInit)
                {

                    maxDistanceAllowed = Float.parseFloat(dataSnapshot.child("Tutore").child("DistMax").getValue().toString());
                    maxDistanceAllowed = maxDistanceAllowed * 1000;

                    mProcessInit = false;

                    LatLng latLng = new LatLng(Double.parseDouble(dataSnapshot.child("Casa").child("Lat").getValue().toString()),Double.parseDouble(dataSnapshot.child("Casa").child("Long").getValue().toString()));

                    Locuinta = mMap.addMarker(new MarkerOptions().position(latLng).title("HOME"));
                    //Locuinta.setIcon();

                    CircleOptions circleOptions = new CircleOptions();

                    circleOptions.center(latLng);
                    circleOptions.radius(Double.parseDouble(dataSnapshot.child("Tutore").child("DistMax").getValue().toString()));
                    circleOptions.strokeColor(R.color.colorPrimary);
                    circleOptions.strokeWidth(3);
                    circleOptions.fillColor(0x224FC3F7);

                    mMap.addCircle(circleOptions);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(getIntent().hasExtra("stare"))if(getIntent().getExtras().containsKey("stare"))if(getIntent().getExtras().getBoolean("stare") && dataSnapshot.child("Lat").getValue() != null)
                {

                    double lat = Double.parseDouble(dataSnapshot.child("Lat").getValue().toString());
                    double lon = Double.parseDouble(dataSnapshot.child("Long").getValue().toString());

                    if(pozMarker !=null)pozMarker.remove();

                    LatLng pacientLocation = new LatLng(lat,lon);
                    pozMarker = mMap.addMarker(new MarkerOptions().position(pacientLocation).title("Pacient"));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onTaskDone(Object... values) {

        if(currentPolyline!=null)
            currentPolyline.remove();

        currentPolyline = mMap.addPolyline((PolylineOptions)values[0]);

    }

    public void zoomOnStart()
    {

        mProcessZoom = true;

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                LatLng startLocation = new LatLng(Double.parseDouble(dataSnapshot.child("Lat").getValue().toString()),Double.parseDouble(dataSnapshot.child("Long").getValue().toString()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation,12.0f));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public boolean isLocation()
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

        return gps_enabled;

    }
}
