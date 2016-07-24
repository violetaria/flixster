package com.getlosthere.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.getlosthere.flickster.clients.MovieRestClient;
import com.getlosthere.flickster.models.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {
    private final int RESULT_OK = 40;
    private final int REQUEST_CODE = 30;
    Movie movie;
    int movieId;
    String movieVideoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movieId = getIntent().getIntExtra("external_movie_id",0);
        getMovieInfo(movieId);

        int code = getIntent().getIntExtra("code",0);
    }

    public void launchQuickPlayView(){
        MovieRestClient.get(Integer.toString(movieId) + "/videos", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray videoJSONResults = null;
                try {
                    videoJSONResults = response.getJSONArray("results");
                    JSONObject videoObject = videoJSONResults.getJSONObject(0); // always grab the first one for now

                    Intent i = new Intent(MovieDetailActivity.this, QuickPlayActivity.class);

                    i.putExtra("movie_key", videoObject.getString("key"));
                    i.putExtra("code", REQUEST_CODE);

                    startActivity(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
//        getMovieVideo(movieId);
//
//        Intent i = new Intent(MovieDetailActivity.this, QuickPlayActivity.class);
//
//        i.putExtra("movie_key", movieVideoKey);
//        i.putExtra("code", REQUEST_CODE);
//
//        startActivity(i);
    }

    public void getMovieVideo(int movieId) {
        MovieRestClient.get(Integer.toString(movieId) + "/videos", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray videoJSONResults = null;
                try {
                    videoJSONResults = response.getJSONArray("results");
                    JSONObject videoObject = videoJSONResults.getJSONObject(0); // always grab the first one for now
                    movieVideoKey = videoObject.getString("key");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void getMovieInfo(int movieId) {
        MovieRestClient.get(Integer.toString(movieId), null, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    movie = new Movie(response);

                    TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
                    TextView tvSynopsis = (TextView) findViewById(R.id.tvSynopsis);
                    TextView tvReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
                    RatingBar rbRating = (RatingBar) findViewById(R.id.rbRating);
                    ImageView ivMovieImage = (ImageView) findViewById(R.id.ivMovieImage);

                    if (tvTitle != null) {
                        tvTitle.setText(movie.getOriginalTitle());
                    }
                    if (tvSynopsis != null){
                        tvSynopsis.setText(movie.getOverview());
                    }
                     if (tvReleaseDate != null) {
                        tvReleaseDate.setText("Released Date: " + movie.getReleaseDate());
                    }
                    if (rbRating != null) {
                        rbRating.setRating(movie.getRating().floatValue());
                    }
                    if (ivMovieImage != null) {
                        Picasso.with(getApplicationContext()).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(10, 10)).fit().centerInside().placeholder(R.drawable.movie_placeholder_land).into(ivMovieImage);
                        ivMovieImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                launchQuickPlayView();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    // ## TODO on back click instead of submit
    public void onSubmit(View v) {
        Intent data = new Intent();

        data.putExtra("code", RESULT_OK); // ints work too

        setResult(RESULT_OK, data); // set result code and bundle data for response

        this.finish();
    }

    // ## TODO FIX THIS
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
//            String name = data.getExtras().getString("name");
            int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            Toast.makeText(this, "hey now", Toast.LENGTH_SHORT).show();
        }
    }
}
