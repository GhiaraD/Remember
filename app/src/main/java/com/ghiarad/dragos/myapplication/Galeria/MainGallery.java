package com.ghiarad.dragos.myapplication.Galeria;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ghiarad.dragos.myapplication.DatabaseHelper;
import com.ghiarad.dragos.myapplication.MainMenu;
import com.ghiarad.dragos.myapplication.R;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGallery extends AppCompatActivity {

    private static final int SELECT_FILE = 2;
    List<Cell> allFilesPaths;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gallery);

        mDatabaseHelper = new DatabaseHelper(this, "photos_table");

        showImages();
    }

    private void showImages() {
        String path = getFilesDir()+"";
        allFilesPaths = new ArrayList<>();
        allFilesPaths = listAllFiles(path);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Cell> cells = prepareData();
        Adapter adapter = new Adapter(getApplicationContext(), cells);
        recyclerView.setAdapter(adapter);
    }

    //prepare images for the list
    private ArrayList<Cell> prepareData(){
        ArrayList<Cell> allImages = new ArrayList<>();
        for(Cell c : allFilesPaths){
            String nume = c.getTitle();
            int poz = nume.lastIndexOf('.');
            if(poz<0)poz=0;
            String suffix = nume.substring(poz);
            if(suffix.compareTo(".jpg")==0 || suffix.compareTo(".jpeg")==0 || suffix.compareTo(".png")==0 || suffix.compareTo(".jpe")==0
                    || suffix.compareTo(".tiff")==0 || suffix.compareTo(".webp")==0 || suffix.compareTo(".tif")==0 || suffix.compareTo(".jpg")==0)
            {
                Cell cell = new Cell();
                cell.setTitle(c.getTitle());
                cell.setPath(c.getPath());
                allImages.add(cell);
            }
        }

        return allImages;
    }

    //load files from folder
    private List<Cell> listAllFiles(String pathName){
        List<Cell> allFiles  = new ArrayList<>();
        File file = new File(pathName);
        File[] files = file.listFiles();
        if(files != null)
        {
            for(File f : files)
            {
                Cell cell = new Cell();
                cell.setTitle(f.getName());
                cell.setPath(f.getAbsolutePath());
                allFiles.add(cell);
            }
        }
        return allFiles;
    }

    ///////////functii pentru adaugarea de poze
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_galerie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.addIcon) {

            galleryIntent();

        }
        return super.onOptionsItemSelected(item);
    }

    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();

            String path = getPath(this,uri);
            String name = getName(this,uri);

            if(!mDatabaseHelper.photoAlreadyExsists(name)) {
                try {
                    insertInPrivateStorage(name, path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Poza deja există", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static String getPath( Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }

        return result;
    }

    public String getName( Context context, Uri uri ) {
        String fileName = null;
        if (uri.getScheme().equals("file")) {
            fileName = uri.getLastPathSegment();
        } else {
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, new String[]{
                        MediaStore.Images.ImageColumns.DISPLAY_NAME
                }, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                }
            } finally {

                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return fileName;
    }


    private void insertInPrivateStorage(String name, String path) throws IOException {

        FileOutputStream fos  = openFileOutput(name,MODE_APPEND);

        File file = new File(path);
        byte[] bytes = FileUtils.readFileToByteArray(file);

        fos.write(bytes);
        fos.close();

        mDatabaseHelper.addPhoto(name);

        //Toast.makeText(getApplicationContext(),"File saved in :"+ getFilesDir() + "/"+name, Toast.LENGTH_SHORT).show();
        showImages();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent no = new Intent(this, MainMenu.class);
        no.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(no);
    }

}
