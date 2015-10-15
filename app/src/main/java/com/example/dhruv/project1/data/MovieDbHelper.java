package com.example.dhruv.project1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Movie;

import com.example.dhruv.project1.data.MovieContract.MovieEntry;

/**
 * Created by dhruv on 10/15/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_WEATHER_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry._ID + "INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_ID + "INTEGER SECONDARY KEY," +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + "TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POPULARITY + "FLOAT, " +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH + "TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + "INTEGER NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + "TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + "FLOAT " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_WEATHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
