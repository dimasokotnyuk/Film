package com.example.film.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.film.Model.FavoriteMovie;
import com.example.film.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.FavouriteMovieViewHolder> {

    private OnPosterClickListener onPosterClickListener;

    private static final String BASE_URL_POSTER = "https://image.tmdb.org/t/p/w342";

    List<FavoriteMovie> favoriteMovies;

    public interface OnPosterClickListener{
        void onPosterClick(int position);
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    public List<FavoriteMovie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void setFavoriteMovies(List<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteMovieViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_movie_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieViewHolder holder, int position) {
        FavoriteMovie favoriteMovie = favoriteMovies.get(position);
        Picasso.get().load(BASE_URL_POSTER+favoriteMovie.getPosterPath()).into(holder.ivFavouritePoster);
    }

    @Override
    public int getItemCount() {
        return favoriteMovies == null ? 0 : favoriteMovies.size();
    }

    class FavouriteMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFavouritePoster;

        public FavouriteMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFavouritePoster = itemView.findViewById(R.id.imageViewFavouritePoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onPosterClickListener!=null){
                        onPosterClickListener.onPosterClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
