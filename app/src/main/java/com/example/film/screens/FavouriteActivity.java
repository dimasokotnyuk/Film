package com.example.film.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.film.model.FavoriteMovie;
import com.example.film.R;
import com.example.film.adapters.FavouriteMovieAdapter;
import com.example.film.data.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    private RecyclerView recyclerViewFavouriteMovies;
    private FavouriteMovieAdapter favouriteMovieAdapter;
    private MovieViewModel movieViewModel;

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
        setContentView(R.layout.activity_favourite);

        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);

        favouriteMovieAdapter = new FavouriteMovieAdapter();
        favouriteMovieAdapter.setFavoriteMovies(new ArrayList<FavoriteMovie>());

        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerViewFavouriteMovies.setAdapter(favouriteMovieAdapter);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getFavouriteMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {
                favouriteMovieAdapter.setFavoriteMovies(favoriteMovies);
            }
        });
        movieViewModel.getFavouriteMovies();
        onPosterClick();
    }

    private void onPosterClick() {
        favouriteMovieAdapter.setOnPosterClickListener(new FavouriteMovieAdapter.OnPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                FavoriteMovie favoriteMovie = favouriteMovieAdapter.getFavoriteMovies().get(position);
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                intent.putExtra("id", favoriteMovie.getId());
                intent.putExtra("FavouriteActivity", "FavouriteActivity");
                startActivity(intent);
            }
        });
    }
}
