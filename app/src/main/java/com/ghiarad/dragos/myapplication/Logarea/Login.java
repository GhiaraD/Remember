package com.ghiarad.dragos.myapplication.Logarea;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {


    private final static int REQUEST_CODE_ASK_PERMISSIONS = 1;
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS };


    EditText userEmailEdit, userPassEdit, codTutore;
    TextView mainLoginBtn, mainCreateBtn;
    CheckBox tutore;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseHelper mDatabaseHelper;
    ProgressDialog mProgressDialog;
    FirebaseDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
    }


    private void initialize()
    {
        setContentView(R.layout.activity_login);

        userEmailEdit = (EditText) findViewById(R.id.mainEmailET);
        userPassEdit = (EditText) findViewById(R.id.mainPassET);
        codTutore = (EditText) findViewById(R.id.codTutore);

        mainLoginBtn = (TextView) findViewById(R.id.mainLoginBtn);
        mainCreateBtn = (TextView) findViewById(R.id.mainCreateBtn);

        tutore = (CheckBox)findViewById(R.id.checkbox);
        mDatabaseHelper = new DatabaseHelper(this, "me_table");
        mDatabaseHelper.addStare("pacient");

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user!=null )
                {
                    Cursor data = mDatabaseHelper.getStare();
                    data.moveToFirst();

                    if(data.getString(0).equals("pacient")) {
                        mProgressDialog.dismiss();
                        Intent moveToHome = new Intent(Login.this, MainMenu.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }
                    else if(data.getString(0).equals("tutore"))
                    {
                        mProgressDialog.dismiss();
                        Intent moveToHome = new Intent(Login.this, TutoreMain.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);
                    }

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        tutore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(tutore.isChecked())
                {
                    codTutore.setEnabled(true);
                    codTutore.setVisibility(View.VISIBLE);
                    codTutore.requestFocus();

                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(codTutore, InputMethodManager.SHOW_IMPLICIT);

                }

                if(!tutore.isChecked())
                {
                    codTutore.setEnabled(false);
                    codTutore.setVisibility(View.INVISIBLE);
                }

            }
        });

        mainCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });


        mainLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Autentificare în curs");
                mProgressDialog.setMessage("Vă rugăm așteptați...");
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                try {
                    loginUser();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }*/

    public static String hashPassword(String password)throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA");
        md.update(password.getBytes());
        byte[] b = md.digest();
        StringBuffer sb = new StringBuffer();
        for(byte b1 : b){
            sb.append(Integer.toHexString(b1 & 0xff).toString());
        }
        return sb.toString();
    }

    private void loginUser() throws NoSuchAlgorithmException {

        if(!tutore.isChecked()) {

            String userEmail, userPass;
            userEmail = userEmailEdit.getText().toString().trim();
            userPass = userPassEdit.getText().toString().trim();

            if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)) {
                mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            mProgressDialog.dismiss();
                            mDatabaseHelper.updateStare("pacient");
                            startActivity(new Intent(Login.this, MainMenu.class));

                        } else {
                            Toast.makeText(Login.this, "Date de conectare invalide", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else {
                Toast.makeText(Login.this, "Introdu email si parola", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
            }

        }
        else
        {
            final String userEmail, userPass, cod;
            userEmail = userEmailEdit.getText().toString().trim();
            userPass = userPassEdit.getText().toString().trim();
            cod = codTutore.getText().toString().trim();

            if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass) && !TextUtils.isEmpty(cod)) {
                mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child("Tutore").child("CodTutore").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    String dbcode = dataSnapshot.getValue().toString().trim();

                                    if(cod.equals(dbcode))
                                    {
                                        mProgressDialog.dismiss();
                                        mDatabaseHelper.updateStare("tutore");
                                        startActivity(new Intent(Login.this, TutoreMain.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this, "Cod de tutore gresit", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                        mAuth.signOut();
                                        startActivity(new Intent(Login.this, Login.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {
                            Toast.makeText(Login.this, "Date de conectare invalide", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    }
                });
            } else {
                Toast.makeText(Login.this, "Introdu email, parola si codul pentru tutore", Toast.LENGTH_LONG).show();
                mProgressDialog.dismiss();
            }
        }
    }

    protected void checkPermissions() {
        final List<String> missingPermissions = new ArrayList<>();
        // check all required dynamic permissions
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(permission);
            }
        }
        if (!missingPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = missingPermissions
                    .toArray(new String[missingPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS, grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        // exit the app if one permission is not granted
                        Toast.makeText(this, "Required permission '" + permissions[index]
                                + "' not granted, exiting", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                // all permissions were granted
                initialize();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
        finish();
    }
}