package com.ghiarad.dragos.myapplication.Galeria;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.R;

public class customizePhoto extends AppCompatActivity {

    String path,title;
    Button gata;
    ImageView imageView;
    FrameLayout FLPoza;
    String coordDB;
    EditText detalii;
    EditText[] mET;
    int i=0;
    DatabaseHelper mDatabaseHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_customize_photo);

        mET = new EditText[101];
        gata = (Button)findViewById(R.id.done);
        gata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase();
            }
        });

        mDatabaseHelper = new DatabaseHelper(this, "photos_table");

        imageView = (ImageView) findViewById(R.id.GalleryPreviewImg);
        FLPoza = (FrameLayout) findViewById(R.id.layout_poza);
        detalii = (EditText)findViewById(R.id.editDetalii);

        path = getIntent().getExtras().getString("path");
        title = getIntent().getExtras().getString("title");

        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(myBitmap);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                int x = (int) event.getX()-90;
                int y = (int) event.getY()-90;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;

                    case MotionEvent.ACTION_UP:
                        MyCoolScreen screen1 = new MyCoolScreen(customizePhoto.this);
                        mET[i]=screen1.init(x, y);
                        i++;
                        FLPoza.addView(screen1);
                        return true;
                }
                return false;
            }
        });
    }

    public void updateDatabase()
    {
        String mNume = null;
        for(int j=0;j<i;++j)
        {
            if(!mET[j].getText().toString().trim().equals(""))
            {
                int x = (int) mET[j].getX();
                int y = (int) mET[j].getY();
                if(coordDB!=null)coordDB = coordDB  + x + "," + y + ";";
                else coordDB = x + "," + y + ";";

                if(mNume!=null)mNume = mNume + mET[j].getText().toString().trim() + ";";
                else mNume = mET[j].getText().toString().trim() + ";";
            }
        }

        mDatabaseHelper.updatePhoto(title,detalii.getText().toString().trim(),coordDB,mNume);

        Toast.makeText(this, title + detalii.getText().toString().trim() + coordDB +mNume, Toast.LENGTH_LONG).show();

        Intent a = new Intent(customizePhoto.this, SinglePhoto.class);
        a.putExtra("path",path);
        a.putExtra("title",title);
        startActivity(a);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        /*Intent a = new Intent(getBaseContext(), SinglePhoto.class);
        a.putExtra("path",path);
        a.putExtra("title",title);
        startActivity(a);*/
    }

}