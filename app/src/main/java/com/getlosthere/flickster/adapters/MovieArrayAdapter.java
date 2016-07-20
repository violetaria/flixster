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

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by violetaria on 7/18/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {
    private static class UnpopularViewHolder {
        TextView overview;
        TextView title;
        ImageView image;
    }

    private static class PopularViewHolder {
        ImageView image;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies){
        super(context, 0, movies);
    }

    @Override
    public int getItemViewType(int position) {

        return getItem(position).popularity.ordinal();
    }

    @Override
    public int getViewTypeCount() {
        return Movie.PopularityValues.values().length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);

        Movie movie = getItem(position);

        // Get orientation of device
        int orientation = getContext().getResources().getConfiguration().orientation;

        // Get screen size of device
        int screenSize = getContext().getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        String imagePath;
        int placeholderImage;
        int imageWidth;
        String imageSize;

        if(viewType == Movie.PopularityValues.POPULAR.ordinal()) {
            PopularViewHolder viewHolder1 = new PopularViewHolder();

            View v = convertView;

            if (v == null) {
                v = LayoutInflater.from(getContext()).inflate(R.layout.item_popular_movie, parent, false);
                viewHolder1.image = (ImageView) v.findViewById(R.id.ivMovieImage);

                v.setTag(viewHolder1);
            } else {
                viewHolder1 = (PopularViewHolder) v.getTag();
            }
            viewHolder1.image.setImageResource(0);

            imageWidth = calculateImageWidth(orientation, viewType);
            imageSize = getImageSizeForOrientation(orientation, screenSize);
            imagePath = movie.getBackdropPath(imageSize);
            placeholderImage = R.drawable.movie_placeholder_land;

            // Picasso.with(getContext()).load(imagePath).resize(imageWidth, 0).placeholder(placeholderImage).into(viewHolder1.image);
            Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).fit().centerInside().placeholder(placeholderImage).into(viewHolder1.image);

            return v;
        } else {
            UnpopularViewHolder viewHolder2 = new UnpopularViewHolder();

            View v = convertView;

            if(v == null){
                v =  LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
                viewHolder2.image = (ImageView) v.findViewById(R.id.ivMovieImage);
                viewHolder2.title = (TextView) v.findViewById(R.id.tvTitle);
                viewHolder2.overview = (TextView) v.findViewById(R.id.tvOverview);

                v.setTag(viewHolder2);
            }
            else {
                viewHolder2 = (UnpopularViewHolder) v.getTag();
            }
            viewHolder2.image.setImageResource(0);
            viewHolder2.title.setText(movie.getOriginalTitle());
            viewHolder2.overview.setText(movie.getOverview());

            imageWidth = calculateImageWidth(orientation,viewType);
            imageSize = getImageSizeForOrientation(orientation, screenSize);

            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imagePath = movie.getBackdropPath(imageSize);
                placeholderImage = R.drawable.movie_placeholder_land;
            } else {
                imagePath = movie.getPosterPath(imageSize);
                placeholderImage = R.drawable.movie_placeholder;
            }

            Picasso.with(getContext()).load(imagePath).transform(new RoundedCornersTransformation(10, 10)).resize(imageWidth, 0).placeholder(placeholderImage).into(viewHolder2.image);

            return v;
        }
    }

    private int calculateImageHeight(int orientation) {
        int screenHeight = DeviceDimensionsHelper.getDisplayHeight(getContext());

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return   2 * screenHeight / 3;
        } else {
            return 2 * screenHeight / 3;
        }
    }

    private int calculateImageWidth(int orientation, int viewType) {
        int screenWidth = DeviceDimensionsHelper.getDisplayWidth(getContext());

        if (orientation == Configuration.ORIENTATION_LANDSCAPE || viewType == Movie.PopularityValues.POPULAR.ordinal()) {
            return  2 * screenWidth / 3;
        } else {
            return screenWidth / 2;
        }
    }

    private String getImageSizeForOrientation(int orientation, int screenSize) {
        String imageSize;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            switch (screenSize) {
                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                    imageSize = "w1280";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_LARGE:
                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                    imageSize = "w780";
                    break;
                case Configuration.SCREENLAYOUT_SIZE_SMALL:
                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
                default:
                    imageSize = "w300";
                    break;
            }
        } else {
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
        }
        return imageSize;
//            imageHeight = 0;
//            imageWidth = 2 * screenWidth / 3;
//
////            bMapScaled = BitmapScaler.scaleToFitWidth(bMap, imageWidth);
//
////            viewHolder.image.getLayoutParams().height = screenHeight;
////            viewHolder.image.getLayoutParams().width = 2 * screenWidth / 3;
//
//            switch (screenSize) {
//                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//                    imageSize = "w1280";
//                    break;
//                case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                    imageSize = "w780";
//                    break;
//                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                case Configuration.SCREENLAYOUT_SIZE_SMALL:
//                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
//                default:
//                    imageSize = "w300";
//                    break;
//            }
//        } else {
//            imageHeight = 0;
//            imageWidth = screenWidth / 2;
//
////            bMapScaled = BitmapScaler.scaleToFitHeight(bMap, imageHeight);
//
////            viewHolder.image.getLayoutParams().height = screenHeight / 2;
////            viewHolder.image.getLayoutParams().width = screenWidth / 2;
//            switch (screenSize) {
//                case Configuration.SCREENLAYOUT_SIZE_XLARGE:
//                case Configuration.SCREENLAYOUT_SIZE_LARGE:
//                    imageSize = "w780";
//                    break;
//                case Configuration.SCREENLAYOUT_SIZE_NORMAL:
//                    imageSize = "w500";
//                    break;
//                case Configuration.SCREENLAYOUT_SIZE_SMALL:
//                case Configuration.SCREENLAYOUT_SIZE_UNDEFINED:
//                default:
//                    imageSize = "w185";
//                    break;
//            }
    }
}
