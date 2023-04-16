package com.alib.myanimelist.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AnimeDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "anime.db";
    private static final int DATABASE_VERSION = 5;
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

    public void exportDataToTxt() {
        Cursor cursor = readAllData();

        if (cursor != null) {
            StringBuilder data = new StringBuilder();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String imageUri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI));
                @SuppressLint("Range") int malId = cursor.getInt(cursor.getColumnIndex(COLUMN_MAL_ID));
                @SuppressLint("Range") int episodes = cursor.getInt(cursor.getColumnIndex(COLUMN_EPISODES));

                data.append("ID: ").append(id).append("\n")
                        .append("Title: ").append(title).append("\n")
                        .append("Image URI: ").append(imageUri).append("\n")
                        .append("MAL ID: ").append(malId).append("\n")
                        .append("Episodes: ").append(episodes).append("\n\n");
            }

            cursor.close();

            try {
                File dir = new File(context.getExternalFilesDir(null), "my_anime_data.txt");
                String filePath = dir.getAbsolutePath()+ "/my_anime_data.txt";

                if (!dir.exists()) {
                    dir.mkdirs();
                }

                FileWriter writer = new FileWriter(filePath);
                writer.append(data.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Failed to export data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("Range")
    public void exportDataToJSON() {
        JSONArray jsonArray = new JSONArray();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ANIME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
                    jsonObject.put(COLUMN_IMAGE_URI, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI)));
                    jsonObject.put(COLUMN_MAL_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_MAL_ID)));
                    jsonObject.put(COLUMN_EPISODES, cursor.getInt(cursor.getColumnIndex(COLUMN_EPISODES)));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();

        try {
            File dir = new File(context.getExternalFilesDir(null), "anime_data_json");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = dir.getAbsolutePath()+ "/anime_data.json";
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importDataFromJson() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            File dir = new File(context.getExternalFilesDir(null), "anime_data_json");
            if (!dir.exists()) {
                dir.mkdirs();
                Toast.makeText(context, "Directory does not exists !",Toast.LENGTH_SHORT).show();
            }
            String filePath = dir.getAbsolutePath()+ "/anime_data.json";
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(context, "Json File does not exists !",Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            String jsonData = sb.toString();

            JSONArray jsonArray = new JSONArray(jsonData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String title = jsonObject.getString("title");
                String imageUri = jsonObject.getString("imageuri");
                int malId = jsonObject.getInt("malID");
                int episodes = jsonObject.getInt("episodes");

                ContentValues cv = new ContentValues();
                cv.put(COLUMN_TITLE, title);
                cv.put(COLUMN_IMAGE_URI, imageUri);
                cv.put(COLUMN_MAL_ID, malId);
                cv.put(COLUMN_EPISODES, episodes);
                long result = db.insert(TABLE_ANIME, null, cv);

                if (result == -1) {
                    Log.e("AnimeDatabaseHelper", "Error inserting data from JSON");
                } else {
                    Log.i("AnimeDatabaseHelper", "Data inserted from JSON: " + title);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int  animeCount(){
        Cursor cursor = readAllData();
        int count = 0;

        if (cursor !=null){
            while (cursor.moveToNext()){
                count++;
            }
        }

        return count;
    }

}