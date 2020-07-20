package com.ghiarad.dragos.myapplication.Tutore;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ghiarad.dragos.myapplication.AlertService;
import com.ghiarad.dragos.myapplication.Logarea.Login;
import com.ghiarad.dragos.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetariTutore extends AppCompatActivity {

    Switch da_nu;
    EditText distMaxProst;
    TextView btnSaveDist,logout,cod;
    FirebaseAuth mAuth;
    Intent serviceIntent;
    boolean mProcessInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setari_tutore);

        da_nu = (Switch)findViewById(R.id.activeazaPerimetru);
        distMaxProst = (EditText)findViewById(R.id.etDist);
        btnSaveDist = (TextView)findViewById(R.id.btnSave);
        logout = (TextView)findViewById(R.id.logout);
        cod = (TextView)findViewById(R.id.cod);

        mAuth = FirebaseAuth.getInstance();

        mProcessInit = true;

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tutore")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessInit) {

                    mProcessInit = false;

                    distMaxProst.setText(dataSnapshot.child("DistMax").getValue().toString().trim());
                    if (dataSnapshot.hasChild("AlertaPacient")) da_nu.setChecked(true);
                    else da_nu.setChecked(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSaveDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Tutore").child("DistMax").setValue(distMaxProst.getText().toString());

                Toast.makeText(SetariTutore.this, "Distanta noua slavata", Toast.LENGTH_SHORT).show();

            }
        });

        da_nu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(!isChecked)
                {

                    stopService();

                }
                else{

                    startService();

                }

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tutore")
                .child("CodTutore").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cod.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                if(isServiceRunning(AlertService.class)){
                    stopService();
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("Tutore").child("AlertaPacient").removeValue();
                }
                startActivity(new Intent(SetariTutore.this, Login.class));

            }
        });
    }

    public void stopService( ){

        serviceIntent = new Intent(this, AlertService.class);
        stopService(serviceIntent);

    }

    public void startService()
    {

        serviceIntent = new Intent(this,AlertService.class);
        ContextCompat.startForegroundService(this,serviceIntent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private boolean isServiceRunning(Class<?> serviceClass){

        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){

            if(serviceClass.getName().equals(service.service.getClassName())){
                return true;
            }

        }

        return false;

    }

}
