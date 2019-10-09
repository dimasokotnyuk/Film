package com.example.film.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.film.Model.FavoriteMovie;
import com.example.film.Model.Movie;
import com.example.film.Model.MovieResponse;
import com.example.film.api.ApiFactory;
import com.example.film.api.ApiService;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private static AppDatabase db;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Throwable> errors;
    private MutableLiveData<Movie> movieMutableLiveData;
    private LiveData<List<FavoriteMovie>> favouriteMovies;
    private MutableLiveData<FavoriteMovie> favoriteMovieMutableLiveData;

    private ApiFactory apiFactory = ApiFactory.getInstance();
    private ApiService apiService = apiFactory.getApiService();

    public MovieViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        movies = db.movieDao().getAllMovies();
        errors = new MutableLiveData<>();
        movieMutableLiveData = new MutableLiveData<>();
        favoriteMovieMutableLiveData = new MutableLiveData<>();
        favouriteMovies = db.movieDao().getAllFavouriteMovies();
    }

    public LiveData<List<FavoriteMovie>> getFavouriteMovies() {
        return favouriteMovies;
    }

    public LiveData<FavoriteMovie> getFavoriteMovieMutableLiveData() {
        return favoriteMovieMutableLiveData;
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Movie> getMovieMutableLiveData() {
        return movieMutableLiveData;
    }

    public MutableLiveData<Throwable> getErrors() {
        return errors;
    }

    private void insertMovie(List<Movie> movies) {
        Completable.fromAction(() -> db.movieDao().insertMovies(movies))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void insertFavouriteMovie(FavoriteMovie favoriteMovie){
        Completable.fromAction(()-> db.movieDao().insertFavouriteMovies(favoriteMovie))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteAllMovie() {
        Completable.fromAction(() -> db.movieDao().deleteAllMovies())
                .subscribeOn(Schedulers.io())
                .subscribe();

    }

    public void deleteFavouriteMovie(FavoriteMovie favoriteMovie){
        Completable.fromAction(()-> db.movieDao().deleteFavouriteMovie(favoriteMovie))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void getFavouriteMovieById(int favouriteMovieId){
        db.movieDao().getFavouriteMovieById(favouriteMovieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FavoriteMovie>() {
                    @Override
                    public void accept(FavoriteMovie favoriteMovie) throws Exception {
                        favoriteMovieMutableLiveData.setValue(favoriteMovie);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }

    public void getMovieById(int movieId) {
        db.movieDao().getMovieById(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Movie>() {
                    @Override
                    public void accept(Movie movie) throws Exception {
                        movieMutableLiveData.setValue(movie);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                    }
                });
    }

    public void loadData(String apiKey, String language, String sortBy, int page) {
        apiService.getMovie(apiKey, language, sortBy, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        deleteAllMovie();
                        insertMovie(movieResponse.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errors.setValue(throwable);
                    }
                });
    }

    public void loadMore(String apiKey, String language, String sortBy, int page) {
        apiService.getMovie(apiKey, language, sortBy, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieResponse>() {
                    @Override
                    public void accept(MovieResponse movieResponse) throws Exception {
                        insertMovie(movieResponse.getResults());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        errors.setValue(throwable);
                    }
                });
    }
}
