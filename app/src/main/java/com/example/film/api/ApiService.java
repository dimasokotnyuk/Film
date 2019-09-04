package com.example.film.api;

import com.example.film.data.MovieResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("movie?api_key=06ddbe408bf942d2050148711d1572c2&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false")
    Observable<MovieResponse> getMovie(@Query("page")int page);
}
