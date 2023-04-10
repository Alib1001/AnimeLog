package com.alib.myanimelist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class AnimeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "anime.db";
    private static final int DATABASE_VERSION = 3;
    public static final String TABLE_ANIME = "anime";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_IMAGE_URI = "imageuri";
    private static final String COLUMN__URL = "url";
    public static final String COLUMN_MAL_ID = "malID";
    public static final String COLUMN_EPISODES = "episodes";
    private final Context context;

    private static final String CREATE_TABLE_ANIME = "CREATE TABLE " + TABLE_ANIME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT,"
            + COLUMN_IMAGE_URI + " TEXT,"
            + COLUMN_MAL_ID + " INTEGER,"
            + COLUMN_EPISODES + " INTEGER"
            + ")";


    public AnimeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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

    public void addAnime(net.sandrohc.jikan.model.anime.Anime anime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String title = anime.getTitle();
        String imageUri = anime.images.getPreferredImageUrl();
        int malId = anime.getMalId();
        int episodes = anime.getEpisodes();

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_IMAGE_URI,imageUri);
        cv.put(COLUMN_MAL_ID,malId);
        cv.put(COLUMN_EPISODES,episodes);
        long result = db.insert(TABLE_ANIME,null, cv);

        if(result == -1){
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, String.valueOf(anime.getMalId()), Toast.LENGTH_SHORT).show();
        }

    }

    public void updateAnime(net.sandrohc.jikan.model.anime.Anime anime){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        String title = anime.getTitle();
        String imageUri = anime.images.getPreferredImageUrl();
        int malId = anime.getMalId();
        int episodes = anime.getEpisodes();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ANIME + " WHERE " + COLUMN_MAL_ID + " = ?", new String[] { String.valueOf(malId) });

        if (cursor.getCount() == 0) {
            cv.put(COLUMN_TITLE, title);
            cv.put(COLUMN_IMAGE_URI,imageUri);
            cv.put(COLUMN_MAL_ID,malId);
            cv.put(COLUMN_EPISODES, episodes);
            long result = db.insert(TABLE_ANIME,null, cv);

            if(result == -1){
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        } else {
            db.delete(TABLE_ANIME, COLUMN_MAL_ID + " = ?", new String[] { String.valueOf(malId) });
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }
    public void updateData(net.sandrohc.jikan.model.anime.Anime anime) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ANIME + " WHERE " + COLUMN_MAL_ID + " = ?", new String[]{String.valueOf(anime.getMalId())});
        if (cursor.moveToFirst()) {
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_TITLE, anime.getTitle());
            cv.put(COLUMN_IMAGE_URI, anime.images.getPreferredImageUrl());
            cv.put(COLUMN_EPISODES, anime.getEpisodes());

            int rowsAffected = db.update(TABLE_ANIME, cv, COLUMN_MAL_ID + " = ?", new String[]{String.valueOf(anime.getMalId())});
            if (rowsAffected > 0) {
                Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to update anime", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();
    }
    public void deleteAnime(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ANIME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});

        if(result == 0){
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"Deleted successfully",Toast.LENGTH_SHORT).show();
        }
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

    public boolean checkIfFav(int malId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ANIME,
                new String[] { COLUMN_MAL_ID },
                COLUMN_MAL_ID + " = ?",
                new String[] { String.valueOf(malId) },
                null, null, null);

        boolean exists = (cursor.getCount() > 0);

        cursor.close();
        db.close();

        return exists;
    }

    public Cursor searchData(String title){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ANIME,
                null,
                COLUMN_TITLE + " LIKE ?",
                new String[]{"%" + title + "%"},
                null, null, null);

        return cursor;
    }



}