package com.example.movielist;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movielist.models.Config;
import com.example.movielist.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    // list of movies
    ArrayList<Movie> movies;

    // config needed for image URLs
    Config config;

    // context for rendering
    Context context;

    // generate list of movies
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // creates and inflates new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // create view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);

        // return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // binds an inflated view to a bew item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder position, int i) {
        // get the movie data at the specified void
        Movie movie = movies.get(i);
        // populate the view with the movie data
        position.tvTitle.setText(movie.getTitle());
        position.tvOverview.setText(movie.getOverview());

        // determine the current orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        // build URL for poster image
        String imageUrl= null;

        // if in portrait mode, load poster image
        if (isPortrait) {
            imageUrl= config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        } else {
            // load backdrop instead
            imageUrl= config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());

        }

        // get the correct placeholder & imageview for orientation
        int placeholderId = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;
        ImageView imageView = isPortrait ? position.ivPosterImage : position.ivBackdropImage;


        // load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);

    }

    // returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // create the view holder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // lookup view objects by id
            ivPosterImage = itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // get item position
            int position = getAdapterPosition();
            // make sure position is valid
            if (position != RecyclerView.NO_POSITION) {
                // get movie at position
                Movie movie = movies.get(position);
                // create intent for movie
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // serialize movie
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // show activity
                context.startActivity(intent);
            }
                Movie movie = movies.get(position);

            }
    }
}
