package com.example.movielist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.movielist.models.Config;
import com.example.movielist.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    // base URL for API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";

    // parameter name for API key
    public final static String API_KEY_PARAM = "api_key";

    // logging from this activity
    public final static String TAG = "MainActivity";


    //declaring instance fields
    AsyncHttpClient client;

    // list of currently playing movies
    ArrayList<Movie> movies;

    // recycler view
    RecyclerView rvMovies;

    // adapter wired to the recycler view
    MovieAdapter adapter;

    // image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initializing client
        client = new AsyncHttpClient();

        // initializing list of movies
        movies = new ArrayList<>();

        // initialize the adapter
        adapter = new MovieAdapter(movies);

        // resolve recycler view & connect layout manager and adapter
        rvMovies = findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // get configuration
        getConfiguration();

    }

    // get list of currently playing movies
    private void getNowPlaying() {
        // create URL
        String url = API_BASE_URL + "/movie/now_playing";
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
                    // iterate through result set and create Movie objects
                    for (int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify movie added to list
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now_playing movies.", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint.", throwable, true);
            }
        });
    }

    // access configuration
    private void getConfiguration() {
        // create URL
        String url = API_BASE_URL + "/configuration";
        // set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key

        // execute a GET request
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    // pass config to adapter
                    adapter.setConfig(config);
                    // get now_playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration.", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration.", throwable, true);
            }
        });
    }

    // handle errors
    private void logError(String message, Throwable error, boolean alertUser) {
        // logging error
        Log.e(TAG, message, error);
        // alert the user
        if (alertUser) {
            // show toast with error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

    }


}
