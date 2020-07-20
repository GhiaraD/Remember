package com.ghiarad.dragos.myapplication;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SeeProfile extends AppCompatActivity {

    TextView nume,pren,dataN,nrTel,tutore,nrUrg,editData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_profile);

        editData = (TextView)findViewById(R.id.editData);

        nume = (TextView)findViewById(R.id.nume);
        pren = (TextView)findViewById(R.id.prenume);
        dataN = (TextView)findViewById(R.id.dataNast);
        //nrTel = (TextView)findViewById(R.id.nrTel);
        tutore = (TextView)findViewById(R.id.numeTutore);
        nrUrg = (TextView)findViewById(R.id.nrUrg);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nume.setText(dataSnapshot.child("Nume").getValue().toString().trim());
                pren.setText(dataSnapshot.child("Prenume").getValue().toString().trim());
                dataN.setText(dataSnapshot.child("DataNasterii").getValue().toString().trim());
                //nrTel.setText(dataSnapshot.child("NumarTelefon").getValue().toString().trim());
                tutore.setText(dataSnapshot.child("NumeTutore").getValue().toString().trim());
                nrUrg.setText(dataSnapshot.child("NumarTutore").getValue().toString().trim());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SeeProfile.this, EditProfile.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(SeeProfile.this, MainMenu.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}
