package com.example.essam.movieapp.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.essam.movieapp.MainActivity;
import com.example.essam.movieapp.Movie;
import com.example.essam.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by essam on 11/22/2016.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MovieViewHolder> {
    private List<Movie> mMovieList;
    private LayoutInflater mInflater;
    private Context mContext;
    boolean fav;

    public ImageAdapter(Context context, List<Movie> mMovieList) {
        this.mContext = context;
        this.mMovieList =  mMovieList;
        this.fav = fav;

    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_grid, parent, false);
        final MovieViewHolder viewHolder = new MovieViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                Movie movie = mMovieList.get(position);
                ((MainActivity) mContext).onItemSelected(movie);
            }
        });
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

//        Log.i("movie", movie.getImage());


        Picasso.with(mContext)
                .load("http://image.tmdb.org/t/p/w500" + movie.getImage())
                .placeholder(R.drawable.progress_image)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        Log.i("text", "" + (mMovieList == null ? 0 : mMovieList.size()));

        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    public void setData(List<Movie> movieList) {
        this.mMovieList = new ArrayList<>();
        this.mMovieList.addAll(movieList);
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.grid_item_image);
        }
    }
}
