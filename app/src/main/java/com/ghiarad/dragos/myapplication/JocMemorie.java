package com.ghiarad.dragos.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Collections;

public class JocMemorie extends AppCompatActivity {

    ImageView i11,i12,i13,i21,i22,i23,i31,i32,i33,i41,i42,i43;

    Integer[] cardsArray={101,102,103,104,105,106,201,202,203,204,205,206};

    int i101,i102,i103,i104,i105,i106,i201,i202,i203,i204,i205,i206;

    int firstCard,secondCard,cardNumber=1,clickedFirst,clickedSecond;

    DatabaseReference db;
    boolean mProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joc_memorie);

        db = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        i11=(ImageView) findViewById(R.id.card11);
        i12=(ImageView) findViewById(R.id.card12);
        i13=(ImageView) findViewById(R.id.card13);
        i21=(ImageView) findViewById(R.id.card21);
        i22=(ImageView) findViewById(R.id.card22);
        i23=(ImageView) findViewById(R.id.card23);
        i31=(ImageView) findViewById(R.id.card31);
        i32=(ImageView) findViewById(R.id.card32);
        i33=(ImageView) findViewById(R.id.card33);
        i41=(ImageView) findViewById(R.id.card41);
        i42=(ImageView) findViewById(R.id.card42);
        i43=(ImageView) findViewById(R.id.card43);

        i11.setTag("0");
        i12.setTag("1");
        i13.setTag("2");
        i21.setTag("3");
        i22.setTag("4");
        i23.setTag("5");
        i31.setTag("6");
        i32.setTag("7");
        i33.setTag("8");
        i41.setTag("9");
        i42.setTag("10");
        i43.setTag("11");

        frontOfCardsResources();

        Collections.shuffle(Arrays.asList(cardsArray));

        Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        simpleChronometer.start();

        i11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());

                doStuff(i11, theCard);
            }
        });
        i12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i12, theCard);
            }
        });
        i13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i13, theCard);
            }
        });
        i21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i21, theCard);
            }
        });
        i22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i22, theCard);
            }
        });
        i23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i23, theCard);
            }
        });
        i31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i31, theCard);
            }
        });
        i32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i32, theCard);
            }
        });
        i33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i33, theCard);
            }
        });
        i41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i41, theCard);
            }
        });
        i42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i42, theCard);
            }
        });
        i43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard=Integer.parseInt((String) v.getTag());
                doStuff(i43, theCard);
            }
        });
    }

    private void doStuff(ImageView i, int card){
        if(cardsArray[card]==101)
            i.setImageDrawable(getDrawable(R.drawable.icons8corgi100));
        if(cardsArray[card]==102)
            i.setImageDrawable(getDrawable(R.drawable.icons8doge100));
        if(cardsArray[card]==103)
            i.setImageDrawable(getDrawable(R.drawable.icons8elephant100));
        if(cardsArray[card]==104)
            i.setImageDrawable(getDrawable(R.drawable.icons8flamingo100));
        if(cardsArray[card]==105)
            i.setImageDrawable(getDrawable(R.drawable.icons8pandafilled100));
        if(cardsArray[card]==106)
            i.setImageDrawable(getDrawable(R.drawable.icons8penguin100));
        if(cardsArray[card]==201)
            i.setImageDrawable(getDrawable(R.drawable.icons8corgi100));
        if(cardsArray[card]==202)
            i.setImageDrawable(getDrawable(R.drawable.icons8doge100));
        if(cardsArray[card]==203)
            i.setImageDrawable(getDrawable(R.drawable.icons8elephant100));
        if(cardsArray[card]==204)
            i.setImageDrawable(getDrawable(R.drawable.icons8flamingo100));
        if(cardsArray[card]==205)
            i.setImageDrawable(getDrawable(R.drawable.icons8pandafilled100));
        if(cardsArray[card]==206)
            i.setImageDrawable(getDrawable(R.drawable.icons8penguin100));

        if(cardNumber==1){
            firstCard=cardsArray[card];
            if(firstCard>200){
                firstCard=firstCard-100;
            }
            cardNumber=2;
            clickedFirst=card;
            i.setEnabled(false);
        } else if(cardNumber==2){
            secondCard=cardsArray[card];
            if(secondCard>200){
                secondCard=secondCard-100;
            }
            cardNumber=1;
            clickedSecond=card;

            i11.setEnabled(false);
            i12.setEnabled(false);
            i13.setEnabled(false);
            i21.setEnabled(false);
            i22.setEnabled(false);
            i23.setEnabled(false);
            i31.setEnabled(false);
            i32.setEnabled(false);
            i33.setEnabled(false);
            i41.setEnabled(false);
            i42.setEnabled(false);
            i43.setEnabled(false);

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    calculate();
                }
            }, 500);
        }
    }

    private void calculate(){
        if(firstCard==secondCard){

            if(clickedFirst==0){
                i11.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==1){
                i12.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==2){
                i13.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==3){
                i21.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==4){
                i22.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==5){
                i23.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==6){
                i31.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==7){
                i32.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==8){
                i33.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==9){
                i41.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==10){
                i42.setVisibility(View.INVISIBLE);
            } else if(clickedFirst==11){
                i43.setVisibility(View.INVISIBLE);
            }

            if(clickedSecond==0){
                i11.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==1){
                i12.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==2){
                i13.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==3){
                i21.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==4){
                i22.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==5){
                i23.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==6){
                i31.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==7){
                i32.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==8){
                i33.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==9){
                i41.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==10){
                i42.setVisibility(View.INVISIBLE);
            } else if(clickedSecond==11){
                i43.setVisibility(View.INVISIBLE);
            }
        } else{
            if(clickedFirst==0){
                i11.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==1){
                i12.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==2){
                i13.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==3){
                i21.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==4){
                i22.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==5){
                i23.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==6){
                i31.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==7){
                i32.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==8){
                i33.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==9){
                i41.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==10){
                i42.setImageResource(R.drawable.question_mark);
            } else if(clickedFirst==11){
                i43.setImageResource(R.drawable.question_mark);
            }

            if(clickedSecond==0){
                i11.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==1){
                i12.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==2){
                i13.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==3){
                i21.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==4){
                i22.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==5){
                i23.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==6){
                i31.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==7){
                i32.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==8){
                i33.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==9){
                i41.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==10){
                i42.setImageResource(R.drawable.question_mark);
            } else if(clickedSecond==11){
                i43.setImageResource(R.drawable.question_mark);
            }
        }

        i11.setEnabled(true);
        i12.setEnabled(true);
        i13.setEnabled(true);
        i21.setEnabled(true);
        i22.setEnabled(true);
        i23.setEnabled(true);
        i31.setEnabled(true);
        i32.setEnabled(true);
        i33.setEnabled(true);
        i41.setEnabled(true);
        i42.setEnabled(true);
        i43.setEnabled(true);

        checkEnd();
    }

    private void checkEnd(){
        if(i11.getVisibility()==View.INVISIBLE&&
                i12.getVisibility()==View.INVISIBLE&&
                i13.getVisibility()==View.INVISIBLE&&
                i21.getVisibility()==View.INVISIBLE&&
                i22.getVisibility()==View.INVISIBLE&&
                i23.getVisibility()==View.INVISIBLE&&
                i31.getVisibility()==View.INVISIBLE&&
                i32.getVisibility()==View.INVISIBLE&&
                i33.getVisibility()==View.INVISIBLE&&
                i41.getVisibility()==View.INVISIBLE&&
                i42.getVisibility()==View.INVISIBLE&&
                i43.getVisibility()==View.INVISIBLE) {

            final Chronometer simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
            final String formatType=simpleChronometer.getText().toString();
            simpleChronometer.stop();

            mProcess = true;

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcess) {

                        mProcess = false;

                        if(dataSnapshot.hasChild("Grafic")) {
                            db.child("Grafic").child(dataSnapshot.child("Grafic").getChildrenCount() + "")
                                    .setValue(formatType);

                        }
                        else
                        {
                            db.child("Grafic").child("0").setValue(formatType);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(JocMemorie.this);
            alertDialogBuilder
                    .setMessage("Felicitari!")
                    //.setMessage(formatType)
                    .setCancelable(false)
                    .setPositiveButton("Inca un joc", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            Intent intent=new Intent(getApplicationContext(), JocMemorie.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Intoarce-te", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(getApplicationContext(), MainMenu.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void frontOfCardsResources() {
        i101=R.drawable.icons8corgi100;
        i102=R.drawable.icons8doge100;
        i103=R.drawable.icons8elephant100;
        i104=R.drawable.icons8flamingo100;
        i105=R.drawable.icons8pandafilled100;
        i106=R.drawable.icons8penguin100;
        i201=R.drawable.icons8corgi100;
        i202=R.drawable.icons8doge100;
        i203=R.drawable.icons8elephant100;
        i204=R.drawable.icons8flamingo100;
        i205=R.drawable.icons8pandafilled100;
        i206=R.drawable.icons8penguin100;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(JocMemorie.this, MainMenu.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}
