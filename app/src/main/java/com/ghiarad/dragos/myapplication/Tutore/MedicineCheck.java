package com.ghiarad.dragos.myapplication.Tutore;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.ghiarad.dragos.myapplication.Medicament;
import com.ghiarad.dragos.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicineCheck extends AppCompatActivity {

    private RecyclerView mCruceaList;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLucrari;
    private DatabaseReference mCompKeyRef;
    private FirebaseAuth mAuth;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_check);

        mCruceaList = (RecyclerView) findViewById(R.id.medicament_list);
        mCruceaList.setHasFixedSize(true);
        mCruceaList.setLayoutManager(new LinearLayoutManager(this));
        mLayoutManager = new LinearLayoutManager(MedicineCheck.this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mCruceaList.setLayoutManager(mLayoutManager);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        mCompKeyRef = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @Override
    protected void onStart()
    {
        super.onStart();

        mCompKeyRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mDatabaseLucrari = mDatabase.child("Medicine");

                FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder> FBRA = new FirebaseRecyclerAdapter<Medicament, MedicamentViewHolder>(
                        Medicament.class,
                        R.layout.medicament_row,
                        MedicamentViewHolder.class,
                        mDatabaseLucrari
                ) {
                    @Override
                    protected void populateViewHolder(MedicamentViewHolder viewHolder, Medicament model, int position) {

                        viewHolder.setnume(getApplicationContext(),model.getnume());
                        viewHolder.setdata(getApplicationContext(),model.getdata());
                        viewHolder.setora(getApplicationContext(),model.getora());

                    }
                };
                mCruceaList.setAdapter(FBRA);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public static class MedicamentViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public MedicamentViewHolder(View itemView)
        {
            super(itemView);
            mView= itemView;
        }

        public void setnume(Context ctx, String nume)
        {
            TextView numePastila = (TextView)mView.findViewById(R.id.numePastila);
            numePastila.setText(nume);
        }
        public void setdata(Context ctx, String data)
        {
            TextView dataPastila = (TextView)mView.findViewById(R.id.dataPastila);
            dataPastila.setText(data);
        }
        public void setora(Context ctx, String ora)
        {
            TextView oraPastila = (TextView)mView.findViewById(R.id.oraPastila);
            oraPastila.setText(ora);
        }
    }
}
