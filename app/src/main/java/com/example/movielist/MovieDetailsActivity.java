package com.example.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.movielist.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity {

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(Bundle savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // unwrap the movie and assign field
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

    }


}
