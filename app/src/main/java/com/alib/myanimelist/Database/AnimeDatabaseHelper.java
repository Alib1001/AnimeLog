package com.alib.myanimelist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AnimeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "anime.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_ANIME = "anime";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URI = "imageuri";
    private static final String COLUMN__URL = "url";
    private static final String COLUMN_RATING = "rating";

    private static final String CREATE_TABLE_ANIME = "CREATE TABLE " + TABLE_ANIME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_IMAGE_URI + " TEXT,"
            + COLUMN_RATING + " TEXT"
            + ")";







    public AnimeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ANIME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANIME);
        onCreate(db);
    }

    public void addAnime(String title,String imageUri){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_IMAGE_URI,imageUri);
        long result = db.insert(TABLE_ANIME,null, cv);



    }




    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_ANIME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_ANIME);
    }

}