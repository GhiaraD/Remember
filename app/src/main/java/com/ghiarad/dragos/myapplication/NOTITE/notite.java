package com.ghiarad.dragos.myapplication.NOTITE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.R;

public class notite extends AppCompatActivity {

    private static final String TAG = "notite";

    DatabaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notite);
        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        mDatabaseHelper = new DatabaseHelper(this, "people_table");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if (editText.length() != 0) {
                    AddData(newEntry);
                    editText.setText("");
                } else {
                    toastMessage("Trebuie completat campul de notite!");
                }

            }
        });
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addNota(newEntry);

        if (insertData) {
            toastMessage("Notita salvata!");
        } else {
            toastMessage("Ceva nu merge, te rugam incearca din nou");
        }
        Intent dua=new Intent(notite.this, ListDataActivity.class);
        startActivity(dua);
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(notite.this, ListDataActivity.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}
