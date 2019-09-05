package com.example.film.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.film.R;
import com.example.film.adapters.MovieAdapter;
import com.example.film.data.Movie;
import com.example.film.data.MovieViewModel;
import com.example.film.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private MovieAdapter adapter;

    private int page = 1;

    private MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewMovies=findViewById(R.id.recyclerViewMovie);

        adapter=new MovieAdapter(this);
        adapter.setMovies(new ArrayList<Movie>());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
        recyclerViewMovies.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
        viewModel.loadData(page);
        recyclerViewMovies.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                viewModel.loadMore(page);
            }
        });
        Log.d("myResult","create");
    }


}
