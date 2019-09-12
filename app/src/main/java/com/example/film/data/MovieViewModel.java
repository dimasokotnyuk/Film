package com.example.film.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.film.Model.Movie;
import com.example.film.Model.MovieResponse;
import com.example.film.api.ApiFactory;
import com.example.film.api.ApiService;

import java.util.List;
import java.util.Observable;

import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import rx.Completable;

public class MovieViewModel extends AndroidViewModel {

    private static AppDatabase db;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Throwable> errors;
    private MutableLiveData<Movie> movieMutableLiveData;

    private ApiFactory apiFactory = ApiFactory.getInstance();
    private ApiService apiService = apiFactory.getApiService();

    public MovieViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        movies = db.movieDao().getAllMovies();
        errors = new MutableLiveData<>();
        movieMutableLiveData = new MutableLiveData<>();
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
        io.reactivex.Completable.fromAction(() -> db.movieDao().insertMovies(movies))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void deleteAllMovie() {
        Completable.fromAction(() -> db.movieDao().deleteAllMovies())
                .subscribeOn(rx.schedulers.Schedulers.io())
                .subscribe();

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
