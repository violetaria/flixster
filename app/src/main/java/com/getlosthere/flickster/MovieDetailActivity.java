package com.getlosthere.flickster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.getlosthere.flickster.clients.MovieRestClient;
import com.getlosthere.flickster.models.Movie;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailActivity extends AppCompatActivity {
    private final int RESULT_OK = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        int extMovieId = getIntent().getIntExtra("external_movie_id",0);
//        int movieId = getIntent().getIntExtra("movie_id",0);
        getMovieInfo(extMovieId);
//        getVideoInfo(extId);

        int code = getIntent().getIntExtra("code",0);
    }

    public void getMovieInfo(int movieId) {
        MovieRestClient.get( Integer.toString(movieId), null, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Movie movie = new Movie(response);

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
}
