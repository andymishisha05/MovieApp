package com.example.essam.movieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.essam.movieapp.adapters.ImageAdapter;
import com.example.essam.movieapp.movieDataSQLite.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by essam on 11/26/2016.
 */

public class MoviesFragment extends Fragment {
    RecyclerView mRecycler;
    Cursor Cursor;
    private String mSortBy = POPULARITY_DESC;

    ArrayList<Movie> mArrayList = null;

    private ImageAdapter mMovieAdapter;
    private TextView mTextView;
    int page = 1;
    private static final String SORT_SETTING_KEY = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private static final String FAVORITE = "favorite";
    private static final String MOVIES_KEY = "movies";


   /* private static final String[] MOVIE_COLUMNS = {
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_YOUTUBE1,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_YOUTUBE2,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_RATING,
            MovieContract.MovieEntry.COLUMN_DATE
    };

    public static final int COL_ID = 1;
    public static final int COL_MOVIE_ID = 2;
    public static final int COL_TITLE = 7;
    public static final int COL_IMAGE = 3;
    public static final int COL_IMAGE2 = 4;
    public static final int COL_OVERVIEW = 5;
    public static final int COL_RATING = 6;
    public static final int COL_DATE = 8;*/

    public MoviesFragment() {
    }

    public interface Callback {
        void onItemSelected(Movie movie);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SharedPreferences sh = getContext().getSharedPreferences("sort", Context.MODE_PRIVATE);

        String sortby = sh.getString("sort", "pop");
        Toast.makeText(getActivity(), sortby, Toast.LENGTH_SHORT).show();
        MenuItem sort_by_popular = menu.findItem(R.id.sort_by_popular);
        MenuItem sort_by_rating = menu.findItem(R.id.sort_by_rating);
        MenuItem sort_by_favorite = menu.findItem(R.id.sort_by_favorite);

        if (mSortBy.contentEquals(POPULARITY_DESC)) {
            if (!sort_by_popular.isChecked()) {
                sort_by_popular.setChecked(true);
            }
        } else if (mSortBy.contentEquals(RATING_DESC)) {
            if (!sort_by_rating.isChecked()) {
                sort_by_rating.setChecked(true);
            }
        } else if (mSortBy.contentEquals(FAVORITE)) {
            if (!sort_by_favorite.isChecked()) {
                sort_by_favorite.setChecked(true);
            }

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (getContext() != null) {
            try {
                switch (item.getItemId()) {
                    case R.id.sort_by_popular:
                        if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }
                        mSortBy = POPULARITY_DESC;
                        updateMovies(mSortBy);
                        return true;
                    case R.id.sort_by_rating:
                        if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }
                        mSortBy = RATING_DESC;
                        updateMovies(mSortBy);
                        return true;
                    case R.id.sort_by_favorite:
                        if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }
                        mSortBy = FAVORITE;
                        updateMovies(mSortBy);
                        return true;

                    default:
                        return super.onOptionsItemSelected(item);
                }
            } catch (Exception e) {

            }
        }
        return super.onOptionsItemSelected(item);
    }



  /*  private void getFavoriatMovie() {
        List<Movie> movies = MovieContract.MovieEntry.;

        mMovieGridAdapter = new ImageAdapter(getActivity(), movies, true);
        mGridView.setAdapter(mMovieGridAdapter);


        System.out.println(movies.get(0).getImage());
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerView);
        // mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        // mTextView = (TextView) view.findViewById(R.id.tv_no_movies);
        mMovieAdapter = new ImageAdapter(getActivity(), new ArrayList<Movie>());

        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        mRecycler.setAdapter(mMovieAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SORT_SETTING_KEY)) {
                mSortBy = savedInstanceState.getString(SORT_SETTING_KEY);
            }

            if (savedInstanceState.containsKey(MOVIES_KEY)) {
                mArrayList = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
                mMovieAdapter.setData(mArrayList);
            } else {
                updateMovies(mSortBy);
            }
        } else {
            updateMovies(mSortBy);
        }

        return view;
    }

    private void updateMovies(String sort_by) {
        if (!sort_by.contentEquals(FAVORITE)) {
            new FetchMoviesTask().execute(sort_by);
        } else {
            new FetchFavoriteMoviesTask(getActivity()).execute();
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (!mSortBy.contentEquals(POPULARITY_DESC)) {
            outState.putString(SORT_SETTING_KEY, mSortBy);
        }
        if (mArrayList != null) {
            outState.putParcelableArrayList(MOVIES_KEY, mArrayList);
        }
        super.onSaveInstanceState(outState);
    }


    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        public Collection<Movie> getMoviesDataFromJson(String movieJsonStr) throws JSONException {
            JSONObject json = new JSONObject(movieJsonStr);
            JSONArray movies = json.getJSONArray("results");
            ArrayList result = new ArrayList<>();
            for (int i = 0; i < movies.length(); i++) {
                result.add(Movie.fromJson(movies.getJSONObject(i)));
            }
            return result;
        }


        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length == -1) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_BY_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_BY_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.the_movie_db_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return (List<Movie>) getMoviesDataFromJson(jsonStr);

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                if (mMovieAdapter != null) {
                    mMovieAdapter.setData(movies);
                }
                mArrayList = new ArrayList<>();
                mArrayList.addAll(movies);
            }
        }
    }

    public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Movie> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {

                Movie movie = new Movie(cursor);
                results.add(movie);

                cursor.close();

            }
            return results;

        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMNS,
                    null,
                    null,
                    null
            );
            //   ArrayList<Movie> mArrayList = null;

            /*if (cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor);
                    mArrayList.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }*/
            return  getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            if (movies != null) {
                if (mMovieAdapter != null) {
                    mMovieAdapter.setData(movies);
                }
                mArrayList = new ArrayList<>();
                mArrayList.addAll(movies);

            }
        }
    }


    }





  /*  private void getFavoriatMovie() {
        List<Movie> movies = myDb.viewData();

        mMovieAdapter = new ImageAdapter(getActivity(), movies, true);
        mGridView.setAdapter((ListAdapter) mArrayList);


        System.out.println(movies.get(0).getImage());

        //Toast.makeText(getActivity(), movies.get(0).getPoster(), Toast.LENGTH_LONG).show();


        /*Picasso.with(getActivity())
                .load()
                .placeholder(R.color.colorAccent)
                .into();*/





  /*  public class FetchFavoriteMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

        private Context mContext;

        public FetchFavoriteMoviesTask(Context context) {
            mContext = context;
        }

        private List<Movie> getFavoriteMoviesDataFromCursor(Cursor cursor) {
            List<Movie> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(cursor);
                    results.add(movie);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<Movie> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MOVIE_COLUMNS,
                    null,
                    null,
                    null
            );
            return getFavoriteMoviesDataFromCursor(cursor);
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if (movies != null) {
                if (mMovieAdapter != null) {
                    mMovieAdapter.setData(movies);
                }
                mArrayList = new ArrayList<>();
                mArrayList.addAll(movies);
            }
        }
    }*/

