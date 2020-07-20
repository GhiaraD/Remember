package com.ghiarad.dragos.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfile extends AppCompatActivity {

    EditText nume,pren,dataN,nrTel,tutore,nrUrg;
    TextView editData;

    DatabaseReference mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editData = (TextView)findViewById(R.id.editData);
        nume = (EditText)findViewById(R.id.nume);
        pren = (EditText)findViewById(R.id.prenume);
        dataN = (EditText)findViewById(R.id.dataNast);
        //nrTel = (EditText)findViewById(R.id.nrTel);
        tutore = (EditText)findViewById(R.id.numeTutore);
        nrUrg = (EditText)findViewById(R.id.nrUrg);

        mDB = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Data");

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

                mDB.child("Nume").setValue(nume.getText().toString().trim());
                mDB.child("Prenume").setValue(pren.getText().toString().trim());
                mDB.child("DataNasterii").setValue(dataN.getText().toString().trim());
                //mDB.child("NumarTelefon").setValue(nrTel.getText().toString().trim());
                mDB.child("NumeTutore").setValue(tutore.getText().toString().trim());
                mDB.child("NumarTutore").setValue(nrUrg.getText().toString().trim());

                startActivity(new Intent(EditProfile.this, SeeProfile.class));

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(EditProfile.this, SeeProfile.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}
