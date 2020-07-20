package com.ghiarad.dragos.myapplication.NOTITE;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.MainMenu;
import com.ghiarad.dragos.myapplication.R;

import java.util.ArrayList;


public class ListDataActivity extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    private Button btnAd;
    DatabaseHelper mDatabaseHelper;

    private ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Notite");
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHelper = new DatabaseHelper(this,"people_table");
        btnAd=(Button) findViewById(R.id.btnAd);
        populateListView();
        btnAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent da=new Intent(ListDataActivity.this, notite.class);
                startActivity(da);
            }
        });
    }

    private void populateListView() {

        Log.d(TAG, "populateListView: Displaying data in the ListView.");

        //obtine datele si le adauga la lista
        Cursor data = mDatabaseHelper.getNote();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            //obtine valoarea din baza de date în coloana 1
            //then add it to the ArrayList
            listData.add(data.getString(1));
        }

        //creeare si configurare adaptor
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        //seteaza onItemClickListener la ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getNotaID(name); //obtine codul asociat cu acel nume
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("Ooops,se pare ca exista o eroare, aceasta notita nu mai exista.");
                }
            }
        });
    }


    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(ListDataActivity.this, MainMenu.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }
}
