package com.ghiarad.dragos.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "people_table";
    private static final String noteCOL1 = "ID";
    private static final String noteCOL2 = "name";

    private static final String TABLE_NAME2 = "me_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "stare";

    private static final String Photos_Table = "photos_table";
    private static final String photoCOL1 = "ID";
    private static final String photoCOL2 = "titlu";
    private static final String photoCOL3 = "detalii";
    private static final String photoCOL4 = "coord";
    private static final String photoCOL5 = "nume";

    public DatabaseHelper(Context context, String nume) {
        super(context, nume, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + noteCOL2 + " TEXT)";
        String createTable2 = "CREATE TABLE " + TABLE_NAME2 + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT)";
        String createTable3 = "CREATE TABLE " + Photos_Table +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                photoCOL2 + " TEXT, " +
                photoCOL3 + " TEXT, " +
                photoCOL4 + " TEXT, " +
                photoCOL5 + " TEXT)";
        db.execSQL(createTable);
        db.execSQL(createTable2);
        db.execSQL(createTable3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME2);
        db.execSQL("DROP IF TABLE EXISTS " + Photos_Table);
        onCreate(db);
    }


    public void addPhoto(String titlu){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(photoCOL2, titlu);

        db.insert(Photos_Table, null, contentValues);
    }

    public void updatePhoto(String titlu, String detalii, String coord, String nume){

        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " SELECT " + photoCOL2 + " FROM " + Photos_Table + " WHERE " +  ;
        /*String replace = "REPLACE INTO " + Photos_Table + " (" + photoCOL2 + ", " + photoCOL3 + ", " + photoCOL4 + ", " + photoCOL5 + ")" +
                " VALUES " + "(" + "'" + titlu + "'" + ", " + "'" + detalii + "'" + ", " + "'" + coord + "'" + ", " + "'" + nume + "'" + ")";

        db.execSQL(replace);*/

        ContentValues initialValues = new ContentValues();
        initialValues.put(photoCOL3, detalii);
        initialValues.put(photoCOL4, coord);
        initialValues.put(photoCOL5, nume);

        db.update(Photos_Table, initialValues, photoCOL2 + "=" + "'" + titlu + "'", null);

    }

    public void deletePhoto(String titlu){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Photos_Table, photoCOL2 + "=" + "'" + titlu + "'",null);

    }

    public Cursor getRowPhoto(String title) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT *  FROM " + Photos_Table + " WHERE " + photoCOL2 + " = " + "'" + title + "'";
        return db.rawQuery(query, null);
    }

    public boolean photoAlreadyExsists(String title) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + photoCOL2 + " FROM " + Photos_Table + " WHERE " + photoCOL2 + " = " + "'" + title + "'";
        Cursor c =db.rawQuery(query,null);
        if(c!=null) return false;
        else return true;
    }


    public boolean addNota(String item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(noteCOL2, item);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        return result != -1;
    }

    public Cursor getNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                noteCOL1 + " DESC",
                null);
        return data;
    }

    public Cursor getNotaID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + noteCOL1 + " FROM " + TABLE_NAME +
                " WHERE " + noteCOL2 + " = '" + name + "'";
        return db.rawQuery(query, null);
    }

    public void updateNota(String newName, int id, String oldName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + noteCOL2 +
                " = '" + newName + "' WHERE " + noteCOL1 + " = '" + id + "'" +
                " AND " + noteCOL2 + " = '" + oldName + "'";
        db.execSQL(query);
    }

    public void deleteNota(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + noteCOL1 + " = '" + id + "'" +
                " AND " + noteCOL2 + " = '" + name + "'";
        db.execSQL(query);
    }



    public void addStare(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, name);

        db.insert(TABLE_NAME2, null, contentValues);
    }

    public Cursor getStare() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL2 + " FROM " + TABLE_NAME2 +
                " WHERE " + COL1 + " = '" + "1" + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateStare(String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME2 + " SET " + COL2 +
                " = '" + newName + "' WHERE " + COL1 + " = '" + "1" + "'";
        db.execSQL(query);
    }

    public void deleteStare() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME2 + " WHERE "
                + COL1 + " = '" + 1 + "'";
        db.execSQL(query);
    }

}
