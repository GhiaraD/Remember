package com.ghiarad.dragos.myapplication.Logarea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.MainMenu;
import com.ghiarad.dragos.myapplication.R;
import com.ghiarad.dragos.myapplication.Tutore.TutoreMain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Register extends AppCompatActivity {

    EditText emailRegister, passRegister, passConfirm;
    EditText nume,prenume,dataN,tlf,oras,strada,nr,bloc,scara,ap,numeUrg,tlfUrg;
    TextView CreateAccount;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseHelper mDatabaseHelper;
    boolean ok=true;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDatabaseHelper = new DatabaseHelper(this, "me_table");
        nume= (EditText) findViewById(R.id.nume);
        prenume= (EditText) findViewById(R.id.prenume);
        dataN= (EditText) findViewById(R.id.dataN);
        //tlf= (EditText) findViewById(R.id.tlf);

        /*oras= (EditText) findViewById(R.id.oras);
        strada= (EditText) findViewById(R.id.strada);
        nr=(EditText) findViewById(R.id.nr);
        bloc= (EditText) findViewById(R.id.bloc);
        scara= (EditText) findViewById(R.id.scara);
        ap= (EditText) findViewById(R.id.ap);*/

        numeUrg= (EditText) findViewById(R.id.numeUrg);
        tlfUrg= (EditText) findViewById(R.id.tlfUrg);

        emailRegister = (EditText) findViewById(R.id.emailRegister);
        passRegister = (EditText) findViewById(R.id.passRegister);
        passConfirm = (EditText) findViewById(R.id.passRegisterConfirm);
        CreateAccount = (TextView) findViewById(R.id.createAccount);

        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Cursor data = mDatabaseHelper.getStare();
                    data.moveToFirst();

                    if(data.getString(0).equals("pacient")) {
                        Intent moveToHome = new Intent(Register.this, MainMenu.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }
                    else
                    {
                        Intent moveToHome = new Intent(Register.this, TutoreMain.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Se creeaza contul");
                mProgressDialog.setMessage("Va rugam asteptati...");
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                try {
                    createUserAccount();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void createUserAccount() throws NoSuchAlgorithmException {

        final String emailUser, passUser, passConf;
        emailUser = emailRegister.getText().toString().trim();
        passUser = passRegister.getText().toString().trim();
        passConf = passConfirm.getText().toString().trim();
        final String num = nume.getText().toString();
        final String pren = prenume.getText().toString();
        final String dat = dataN.getText().toString();
        //final String tl = tlf.getText().toString();
        /*final String ora = oras.getText().toString();
        final String str = strada.getText().toString();
        final String n = nr.getText().toString();
        final String bl = bloc.getText().toString();
        final String sc = scara.getText().toString();
        final String a = ap.getText().toString();*/
        final String nrur = numeUrg.getText().toString();
        final String tlur = tlfUrg.getText().toString();

        if( !TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(passUser) && passConf.compareTo(passUser)==0 && passConf.length()>=6)
        {
            mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if( task.isSuccessful())
                    {

                        if (num.length() == 0 || pren.length()==0 || dat.length() == 0 || nrur.length() == 0 || tlur.length() == 0 ) {
                            toastMessage("Trebuie completate toate campurile!");
                        } else {

                            String mUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            databaseReference.child(mUserId).child("Data").child("Nume").setValue(num);
                            databaseReference.child(mUserId).child("Data").child("Prenume").setValue(pren);
                            databaseReference.child(mUserId).child("Data").child("DataNasterii").setValue(dat);
                            //databaseReference.child(mUserId).child("Data").child("NumarTelefon").setValue(tl);
                            databaseReference.child(mUserId).child("Data").child("NumeTutore").setValue(nrur);
                            databaseReference.child(mUserId).child("Data").child("NumarTutore").setValue(tlur);
                            databaseReference.child(mUserId).child("Tutore").child("UrmaresteProst").setValue("0");
                            databaseReference.child(mUserId).child("Tutore").child("DistMax").setValue("500");

                        }

                        Toast.makeText(Register.this, "Contul a fost creat cu succes", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();

                        Random rand = new Random();
                        int rand1 = rand.nextInt(9);
                        int rand2 = rand.nextInt(9);
                        int rand3 = rand.nextInt(9);
                        int rand4 = rand.nextInt(9);
                        int rand5 = rand.nextInt(9);

                        String codd = "" + rand1 + rand2 + rand3 + rand4 + rand5;

                        SmsManager.getDefault().sendTextMessage(tlur, null,
                                "Acesta este codul tau de tutore: " + codd, null, null);

                        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Tutore").child("CodTutore").setValue(codd);

                        mDatabaseHelper.updateStare("pacient");

                        startActivity(new Intent(Register.this, MainMenu.class));

                    }
                    else
                    {
                        Toast.makeText(Register.this, "Email invalid", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }
        else
        {
            if(TextUtils.isEmpty(emailUser)||TextUtils.isEmpty(passUser)||TextUtils.isEmpty(passConf))
            {
                Toast.makeText(Register.this, "Completeaza toate spatiile", Toast.LENGTH_SHORT).show();
            }
            else if(passConf.compareTo(passUser)!=0)
            {
                Toast.makeText(Register.this, "Cele doua parole sunt diferite", Toast.LENGTH_SHORT).show();
            }
            else if(passConf.compareTo(passUser)==0 && passConf.length()<6)
            {
                Toast.makeText(Register.this, "Parola trebuie sa contina cel putin 6 caractere", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Register.this, "Contul nu a putut fi creat", Toast.LENGTH_SHORT).show();
            }
            mProgressDialog.dismiss();
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(Register.this, Login.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}