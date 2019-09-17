package com.example.film.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.film.R;
import com.example.film.adapters.MovieAdapter;
import com.example.film.Model.Movie;
import com.example.film.data.MovieViewModel;
import com.example.film.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMovies;
    private MovieAdapter adapter;

    private ImageView imageViewPopularity;
    private ImageView imageViewRating;

    private boolean valueOfSort = true;

    private LinearLayoutManager linearLayoutManager;


    private static final String API_KEY = "06ddbe408bf942d2050148711d1572c2";
    //private static final String LANGUAGE_VALUE = "ru-RU";
    private static final String LANGUAGE_VALUE = "en-US";
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static int page = 1;

    private MovieViewModel viewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intentToMain = new Intent(this, MainActivity.class);
                startActivity(intentToMain);
                break;
            case R.id.itemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewRating=findViewById(R.id.imageViewRating);
        imageViewPopularity=findViewById(R.id.imageViewPopulairty);

        initRecyclerView();
        addPagination();
        checkSort();
        onClickSort();
        onPosterClick();
    }

    private void initRecyclerView() {
        recyclerViewMovies = findViewById(R.id.recyclerViewMovie);

        adapter = new MovieAdapter(this);
        adapter.setMovies(new ArrayList<Movie>());
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewMovies.setLayoutManager(linearLayoutManager);
        recyclerViewMovies.setAdapter(adapter);

    }

    private void onPosterClick(){
        adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
            Movie movie = adapter.getMovies().get(position);
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                intent.putExtra("id",movie.getId());
                intent.putExtra("MainActivity","MainActivity");
                startActivity(intent);
            }
        });
    }

    private void addPagination() {
        recyclerViewMovies.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                page++;
                if (valueOfSort) {
                    viewModel.loadMore(API_KEY, LANGUAGE_VALUE, SORT_BY_POPULARITY, page);
                }else {
                    viewModel.loadMore(API_KEY, LANGUAGE_VALUE, SORT_BY_TOP_RATED, page);
                }
            }
        });
    }

    private void loadData(Boolean ofSort) {
        viewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                adapter.setMovies(movies);
            }
        });
        if (valueOfSort) {
            viewModel.loadData(API_KEY, LANGUAGE_VALUE, SORT_BY_POPULARITY, page);
        } else {
            viewModel.loadData(API_KEY, LANGUAGE_VALUE, SORT_BY_TOP_RATED, page);
        }
    }

    private void onClickSort(){
        imageViewPopularity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueOfSort=true;
                loadData(valueOfSort);
                checkSort();
            }
        });
        imageViewRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueOfSort=false;
                loadData(valueOfSort);
                checkSort();
            }
        });
    }

    private void checkSort(){
        if(valueOfSort){
            imageViewRating.setImageResource(R.drawable.rating_grey);
            imageViewPopularity.setImageResource(R.drawable.popularity_yelow);
        }else {
            imageViewRating.setImageResource(R.drawable.rating_yelow);
            imageViewPopularity.setImageResource(R.drawable.popularity_grey);
        }
        loadData(valueOfSort);
    }
}

