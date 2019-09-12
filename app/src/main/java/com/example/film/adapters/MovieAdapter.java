package com.example.film.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.film.R;
import com.example.film.Model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String BASE_URL_POSTER="https://image.tmdb.org/t/p/w342";

    private List<Movie> movies;

    private Context context;

    private OnPosterClickListener onPosterClickListener;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public interface OnPosterClickListener{
        void onPosterClick(int position);
    }

    public void setOnPosterClickListener(OnPosterClickListener onPosterClickListener) {
        this.onPosterClickListener = onPosterClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item,viewGroup,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Movie movie = movies.get(i);
        Picasso.get().load(BASE_URL_POSTER+movie.getPosterPath()).into(movieViewHolder.imageViewPoster);
        movieViewHolder.textViewTitle.setText(movie.getTitle());
        movieViewHolder.textViewOverview.setText(movie.getOverview());
        //Glide.with(context).load(BASE_URL_POSTER+movie.getPosterPath()).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(movieViewHolder.imageViewPoster);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageViewPoster;
        private TextView textViewTitle;
        private TextView textViewOverview;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPoster=itemView.findViewById(R.id.imageViewBigPoster);
            textViewTitle=itemView.findViewById(R.id.textViewTitle);
            textViewOverview=itemView.findViewById(R.id.textViewOverview);
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
