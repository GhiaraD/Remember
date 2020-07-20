package com.ghiarad.dragos.myapplication.Galeria;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.OnBoarding.MainActivity;
import com.ghiarad.dragos.myapplication.R;

import java.io.File;

public class SinglePhoto extends AppCompatActivity {

    String path,title;
    ImageView imageView, editPhoto, deletePhoto;
    FrameLayout FLPoza;
    TextView details;
    int k=0;
    TextView[] mTV;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_single_photo);

        mTV = new TextView[101];
        imageView = (ImageView) findViewById(R.id.imagine);
        FLPoza = (FrameLayout) findViewById(R.id.layout_poza);
        details = (TextView)findViewById(R.id.detalii);
        editPhoto = (ImageView) findViewById(R.id.editPhoto);
        deletePhoto = (ImageView) findViewById(R.id.delete);

        mDatabaseHelper = new DatabaseHelper(this, "photos_table");

        path = getIntent().getExtras().getString("path");
        title = getIntent().getExtras().getString("title");

        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(myBitmap);

        Cursor data = mDatabaseHelper.getRowPhoto(title);

        data.moveToFirst();

        String detalii = data.getString(2);
        String coordDB = data.getString(3);
        String text = data.getString(4);
        details.setText(detalii);

        if(coordDB!=null && text!=null) {
            String[] coord = coordDB.split(";");
            String[] nume = text.split(";");
            String[] da = new String[101];

            for (int i = 0; i < coord.length; ++i) {
                for (int j = 0; j < coord[i].length(); ++j) {
                    da = coord[i].split(",");
                }
                MyCoolScreen screen = new MyCoolScreen(SinglePhoto.this);
                mTV[k] = screen.initTextView(Integer.parseInt(da[0].trim()), Integer.parseInt(da[1].trim()) + 20, nume[i]);
                k++;
                FLPoza.addView(screen);
            }
        }

        deletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file = new File(path);
                boolean deleted = file.delete();

                if (deleted)
                    mDatabaseHelper.deletePhoto(title);

                Intent a = new Intent(SinglePhoto.this, MainActivity.class);
                a.putExtra("path",path);
                a.putExtra("title",title);
                startActivity(a);

            }
        });

        editPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent a = new Intent(SinglePhoto.this, customizePhoto.class);
                a.putExtra("path",path);
                a.putExtra("title",title);
                startActivity(a);

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(details.getVisibility()==View.VISIBLE)
                {
                    details.setVisibility(View.INVISIBLE);
                    for(int i=0;i<k;++i)
                    {
                        mTV[i].setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    details.setVisibility(View.VISIBLE);
                    for(int i=0;i<k;++i)
                    {
                        mTV[i].setVisibility(View.VISIBLE);
                    }
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent returnHome = new Intent(getBaseContext(), MainGallery.class);
        startActivity(returnHome);
    }
}