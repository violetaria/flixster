package com.getlosthere.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.getlosthere.flickster.R;
import com.getlosthere.flickster.helpers.DeviceDimensionsHelper;
import com.getlosthere.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by violetaria on 7/18/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private static class ViewHolder {
        TextView overview;
        TextView title;
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolder viewHolder;

        // Get orientation of device
        int orientation = getContext().getResources().getConfiguration().orientation;

        String imagePath;
        int imageWidth;
        int imageHeight;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflator = LayoutInflater.from(getContext());
            convertView = inflator.inflate(R.layout.item_movie, parent, false);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.image.setImageResource(0);
        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());

        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());
        int screenHeight = DeviceDimensionsHelper.getDisplayHeight(getContext());

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageHeight = 0;
            imageWidth = 2 * screenWidth / 3;

            imagePath = movie.getBackdropPath();

//            viewHolder.image.getLayoutParams().height = 2 * screenHeight / 3;
//            viewHolder.image.getLayoutParams().width = 3 * screenWidth / 4;
        } else {
            // Get height of screen if portrait
            imageHeight = 0;
            imageWidth = screenWidth / 2;

            imagePath = movie.getPosterPath();

//            viewHolder.image.getLayoutParams().height = screenHeight;
//            viewHolder.image.getLayoutParams().width = screenWidth / 2;
        }

        Picasso.with(getContext()).load(imagePath).resize(imageWidth, imageHeight).into(viewHolder.image);
//        Picasso.with(getContext()).load(imagePath).fit().centerInside().into(viewHolder.image);

        return convertView;
    }
}
