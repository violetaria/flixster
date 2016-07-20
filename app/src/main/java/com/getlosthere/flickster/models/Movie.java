package com.getlosthere.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by violetaria on 7/18/16.
 */
public class Movie {
    private String defaultPosterSize = "w500";
    private String defaultBackdropSize = "w780";

    public String getPosterPath() {
        return getPosterPath(defaultPosterSize);
    }

    public String getPosterPath(String size) {
        return String.format("https://image.tmdb.org/t/p/%s/%s",size,posterPath);
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath(String size) {
        return String.format("https://image.tmdb.org/t/p/%s/%s",size,backdropPath);
    }

    public String getBackdropPath(){
        return getBackdropPath(defaultBackdropSize);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    String posterPath;
    String backdropPath;
    String originalTitle;
    Boolean adult;
    String overview;

    public Movie(JSONObject jsonObject)throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.overview = jsonObject.getString("overview");
        this.adult = jsonObject.getBoolean("adult");
        this.originalTitle = jsonObject.getString("original_title");
    }

    public static ArrayList<Movie> fromJSONArray(JSONArray jsonArray){
        ArrayList<Movie> results = new ArrayList<Movie>();
        for(int i = 0; i < jsonArray.length(); i++){
            try {
                results.add(new Movie(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
