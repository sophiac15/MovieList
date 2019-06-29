package com.example.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.movielist.models.Movie;
import com.example.movielist.models.MovieTrailerActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    // the view objects
    TextView tvTitle;
    TextView tvOverview;
    String keyName;
    RatingBar rbVoteAverage;



    // base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // parameter name for API key
    public final static String API_KEY_PARAM = "api_key";
    // initializing client
    AsyncHttpClient client = new AsyncHttpClient();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // unwrap the movie and assign field
        movie = Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));


        // create URL
        String url = API_BASE_URL + "/movie/"+ movie.getId() +"/videos";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key
        // execute a GET request
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load results into movie list
                try {
                    JSONArray results = response.getJSONArray("results");
                    JSONObject trailer = results.getJSONObject(0);
                    keyName = trailer.getString("key");




                    Log.i("details activity", String.format("Loaded videos", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now_playing movies.", e, true);
                }
            }




            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from videos endpoint.", throwable, true);
            }
        });






        setContentView(R.layout.activity_movie_details);

        // view objects
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.rbVoteAverage);


        //movie id get id

        // set tv title & overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // get vote average
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);


    }


    public void gotoTrailerActivity(View v){
        Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
        i.putExtra("movieId", keyName);
        v.getContext().startActivity(i);
    }



    // handle errors
    private void logError(String message, Throwable error, boolean alertUser) {
        // logging error
        Log.e("details activity", message, error);
        // alert the user
        if (alertUser) {
            // show toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

    }



}
