package com.alib.myanimelist.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import net.sandrohc.jikan.model.anime.AnimeStatus;
import net.sandrohc.jikan.model.anime.AnimeType;
import net.sandrohc.jikan.model.enums.AgeRating;

import java.util.ArrayList;
import java.util.List;

public class AnimeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "anime.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_ANIME = "anime";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_IMAGE_URI = "imageuri";
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
    /**
    public void addAnime(net.sandrohc.jikan.model.anime.Anime anime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, anime.getTitle());
        values.put(COLUMN_TYPE, String.valueOf(anime.getType()));
        values.put(COLUMN_SOURCE, anime.getSource());
        values.put(COLUMN_EPISODES, anime.getEpisodes());
        values.put(COLUMN_STATUS, String.valueOf(anime.getStatus()));
        values.put(COLUMN_AIRING, anime.isAiring() ? 1 : 0);
        values.put(COLUMN_AIR_DATE, String.valueOf(anime.getAired()));
        values.put(COLUMN_DURATION, String.valueOf(anime.getDuration()));
        values.put(COLUMN_RATING, String.valueOf(anime.getRating()));
        values.put(COLUMN_SCORE, anime.getScore());
        values.put(COLUMN_SCORED_BY, anime.getScoredBy());
        values.put(COLUMN_RANK, anime.getRank());
        values.put(COLUMN_POPULARITY, anime.getPopularity());
        values.put(COLUMN_MEMBERS, anime.getMembers());
        values.put(COLUMN_FAVORITES, anime.getFavorites());
        values.put(COLUMN_SYNOPSIS, anime.getSynopsis());
        values.put(COLUMN_BACKGROUND, anime.getBackground());
        values.put(COLUMN_SEASON, String.valueOf(anime.getSeason()));
        values.put(COLUMN_YEAR, anime.getYear());
        values.put(COLUMN_BROADCAST, String.valueOf(anime.getBroadcast()));
        long id = db.insert(TABLE_ANIME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<net.sandrohc.jikan.model.anime.Anime> getAllAnime() {
        List<net.sandrohc.jikan.model.anime.Anime> animeList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ANIME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                net.sandrohc.jikan.model.anime.Anime  anime = new net.sandrohc.jikan.model.anime.Anime();

                anime.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                anime.setType(AnimeType.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))));
                anime.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_SOURCE)));
                anime.setEpisodes(cursor.getInt(cursor.getColumnIndex(COLUMN_EPISODES)));
                anime.setStatus(AnimeStatus.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_STATUS))));
                anime.setAiring(cursor.getInt(cursor.getColumnIndex(COLUMN_AIRING)) == 1);
                anime.setRating(AgeRating.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_RATING))));
                anime.setScore(cursor.getDouble(cursor.getColumnIndex(COLUMN_SCORE)));
                anime.setScoredBy(cursor.getInt(cursor.getColumnIndex(COLUMN_SCORED_BY)));
                anime.setRank(cursor.getInt(cursor.getColumnIndex(COLUMN_RANK)));
                anime.setPopularity(cursor.getInt(cursor.getColumnIndex(COLUMN_POPULARITY)));
                anime.setMembers(cursor.getInt(cursor.getColumnIndex(COLUMN_MEMBERS)));
                anime.setFavorites(cursor.getInt(cursor.getColumnIndex(COLUMN_FAVORITES)));
                anime.setSynopsis(cursor.getString(cursor.getColumnIndex(COLUMN_SYNOPSIS)));
                anime.setBackground(cursor.getString(cursor.getColumnIndex(COLUMN_BACKGROUND)));
                anime.setYear(cursor.getInt(cursor.getColumnIndex(COLUMN_YEAR)));
                animeList.add(anime);
            } while (cursor.moveToNext());
        }
        db.close();
        return animeList;
    }

    **/

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