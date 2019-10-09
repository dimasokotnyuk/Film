package com.example.film.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.film.model.FavoriteMovie;
import com.example.film.model.Movie;

@Database(entities = {Movie.class, FavoriteMovie.class}, version = 21 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "movies.db";
    private static AppDatabase appDatabase;

    private static final Object LOCK = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context,AppDatabase.class,DB_NAME).fallbackToDestructiveMigration().build();
            }
            return appDatabase;
        }
    }

    public abstract MovieDao movieDao();
}
