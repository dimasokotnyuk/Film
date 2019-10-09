package com.example.film.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.film.model.FavoriteMovie;
import com.example.film.model.Movie;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavoriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM favourite_movies WHERE id==:favouriteMovieId")
    Single<FavoriteMovie> getFavouriteMovieById(int favouriteMovieId);

    @Query("SELECT * FROM movies WHERE id==:moveId")
    Single<Movie> getMovieById(int moveId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavouriteMovies(FavoriteMovie favoriteMovie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Delete
    void deleteFavouriteMovie(FavoriteMovie favoriteMovie);

    @Query("DELETE FROM movies")
    void deleteAllMovies();

}
