package com.example.film.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.film.api.ApiFactory;
import com.example.film.api.ApiService;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends AndroidViewModel {

    private static AppDatabase db;
    private LiveData<List<Movie>> movies;
    private MutableLiveData<Throwable> errors;

    private ApiFactory apiFactory = ApiFactory.getInstance();
    private ApiService apiService = apiFactory.getApiService();

    public MovieViewModel(Application application) {
        super(application);
        db = AppDatabase.getInstance(application);
        movies = db.movieDao().getAllMovies();
        errors = new MutableLiveData<>();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public MutableLiveData<Throwable> getErrors() {
        return errors;
    }

    private void insertMovie(List<Movie> movies) {
        new InsertMovieTask().execute(movies);
    }

    private static class InsertMovieTask extends AsyncTask<List<Movie>, Void, Void> {
        @Override
        protected Void doInBackground(List<Movie>... lists) {
            if (lists != null && lists.length > 0) {
                db.movieDao().insertMovies(lists[0]);
            }
            return null;
        }
    }

    private void deleteAllMovie() {
        new DeleteAllMovieTask().execute();
    }

    private static class DeleteAllMovieTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            db.movieDao().deleteAllMovies();
            return null;
        }
    }

    public void loadData(int page) {
        apiService.getMovie(page)
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

    public void loadMore(int page) {
        apiService.getMovie(page)
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
