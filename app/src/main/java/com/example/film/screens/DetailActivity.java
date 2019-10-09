package com.example.film.screens;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.film.Model.FavoriteMovie;
import com.example.film.Model.Movie;
import com.example.film.R;
import com.example.film.data.MovieViewModel;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private static final String BASE_URL_BIG_POSTER = "https://image.tmdb.org/t/p/original";

    private int id;
    private Movie detailMovie;
    private MovieViewModel movieViewModel;

    private FavoriteMovie detailFavoriteMovie;

    private ImageView imageViewBigPoster;
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewRating;
    private TextView textViewReleaseData;
    private TextView textViewOverview;

    private ImageView imageViewFavouriteAdd;

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
        setContentView(R.layout.activity_detail);

        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewLabelOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleaseData = findViewById(R.id.textViewReleaseData);
        textViewOverview = findViewById(R.id.textViewOverview);

        imageViewFavouriteAdd = findViewById(R.id.imageViewAddToFavourite);
        imageViewFavouriteAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detailFavoriteMovie == null) {
                    movieViewModel.insertFavouriteMovie(new FavoriteMovie(detailMovie));
                    Toast.makeText(DetailActivity.this, "Добавлено", Toast.LENGTH_SHORT).show();
                } else {
                    movieViewModel.deleteFavouriteMovie(detailFavoriteMovie);
                    Toast.makeText(DetailActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                }
                setFavourite();
            }
        });


        Intent intent = getIntent();
        if (intent.hasExtra("id") && intent.hasExtra("MainActivity")) {
            id = intent.getIntExtra("id", -1);
            movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            movieViewModel.getMovieById(id);
            observeMovieLiveData(movieViewModel);
        } else if (intent.hasExtra("id") && intent.hasExtra("FavouriteActivity")) {
            id = intent.getIntExtra("id", -1);
            movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
            movieViewModel.getFavouriteMovieById(id);
            observeFavouriteMovie(movieViewModel);
        } else {
            finish();
        }
        setFavourite();

    }

    void observeMovieLiveData(MovieViewModel viewModel) {
        viewModel.getMovieMutableLiveData().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    detailMovie = movie;

                    textViewTitle.setText(detailMovie.getTitle());
                    textViewOriginalTitle.setText(detailMovie.getOriginalTitle());
                    textViewRating.setText(Double.toString(detailMovie.getVoteAverage()));
                    textViewReleaseData.setText(detailMovie.getReleaseDate());
                    textViewOverview.setText(detailMovie.getOverview());

                    Picasso.get().load(BASE_URL_BIG_POSTER + detailMovie.getPosterPath()).into(imageViewBigPoster);
                }
            }
        });
    }

    void observeFavouriteMovie(MovieViewModel viewModel){
        viewModel.getFavoriteMovieMutableLiveData().observe(this, new Observer<FavoriteMovie>() {
            @Override
            public void onChanged(FavoriteMovie favoriteMovie) {
                detailFavoriteMovie = favoriteMovie;

                textViewTitle.setText(detailFavoriteMovie.getTitle());
                textViewOriginalTitle.setText(detailFavoriteMovie.getOriginalTitle());
                textViewRating.setText(Double.toString(detailFavoriteMovie.getVoteAverage()));
                textViewReleaseData.setText(detailFavoriteMovie.getReleaseDate());
                textViewOverview.setText(detailFavoriteMovie.getOverview());

                Picasso.get().load(BASE_URL_BIG_POSTER + detailFavoriteMovie.getPosterPath()).into(imageViewBigPoster);
            }
        });
    }

    private void setFavourite() {
       movieViewModel.getFavouriteMovieById(id);
        if (detailFavoriteMovie == null) {
            imageViewFavouriteAdd.setImageResource(R.drawable.favourite_add_to);
        } else {
            imageViewFavouriteAdd.setImageResource(R.drawable.favourite_remove);
        }
    }
}

