package com.example.movielist.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {

    //base URL for loading images
    String imageBaseUrl;

    // poster size
    String posterSize;

    // backdrop size
    String backdropSize;

    public Config (JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // get the image base URL
        imageBaseUrl = images.getString("secure_base_url");
        // get the image size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        // use the option at index 3
        posterSize = posterSizeOptions.optString(3,"w342");
        // parse backdrop sizes
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    // helper method for creating URLS
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl,  size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
