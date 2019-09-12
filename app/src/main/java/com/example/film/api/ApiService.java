package com.example.film.api;

import com.example.film.Model.MovieResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface ApiService {
    @GET("movie?&include_adult=false&include_video=false")
    Observable<MovieResponse> getMovie(@Query("api_key") String apiKey,@Query("language") String language,@Query("sort_by")String sortBy,@Query("page")int page);
}
