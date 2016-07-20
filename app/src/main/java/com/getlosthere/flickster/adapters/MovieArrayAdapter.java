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

//        backdrop_sizes: [
//        "w300",
//                "w780",
//                "w1280",
//                "original"
//        ],
//        poster_sizes: [
//        "w92",
//                "w154",
//                "w185",
//                "w342",
//                "w500",
//                "w780",
//                "original"
//        ],
        int screenSize = getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        String imageSize;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageHeight = 0;
            imageWidth = 2 * screenWidth / 3;

//            viewHolder.image.getLayoutParams().height = 2 * screenHeight / 3;
//            viewHolder.image.getLayoutParams().width = 3 * screenWidth / 4;

            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    imageSize = "w1280";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    imageSize = "w780";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                    imageSize = "w300";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                default:
                    imageSize = "w300";
                    break;
            }
            imagePath = movie.getBackdropPath(imageSize);

        } else {
            // Get height of screen if portrait
            imageHeight = 0;
            imageWidth = screenWidth / 2;

//            viewHolder.image.getLayoutParams().height = screenHeight;
//            viewHolder.image.getLayoutParams().width = screenWidth / 2;
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                    imageSize = "w780";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    imageSize = "w500";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                default:
                    imageSize = "w185";
                    break;
            }
            imagePath = movie.getPosterPath(imageSize);
        }

        Picasso.with(getContext()).load(imagePath).resize(imageWidth, imageHeight).into(viewHolder.image);
//        Picasso.with(getContext()).load(imagePath).fit().centerInside().into(viewHolder.image);

        return convertView;
    }
}
