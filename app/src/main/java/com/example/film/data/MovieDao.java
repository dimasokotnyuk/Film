package com.example.film.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.film.Model.Movie;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE id==:moveId")
    Single<Movie> getMovieById(int moveId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

}
