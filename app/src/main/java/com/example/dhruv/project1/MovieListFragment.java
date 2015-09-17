package com.example.dhruv.project1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
import java.util.Collections;
import java.util.Comparator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */

public class MovieListFragment extends Fragment {

    public MovieListFragment(){}

    public class MovieListAdapter extends ArrayAdapter<MovieItem>
    {
        Context context;
        int layoutResourceId;
        final ArrayList<MovieItem> movies;

        class MovieItemHolder {
            ImageView image;
        }

        public MovieListAdapter(Context context, int layoutResourceId, ArrayList<MovieItem> movies){
        super(context, layoutResourceId, movies);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.movies = movies;
    }

        @Override
        public View getView ( int position, View convertView, ViewGroup parent){
            View row = convertView;
            MovieItemHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                row = inflater.inflate(layoutResourceId, parent, false);

                holder = new MovieItemHolder();
                holder.image = (ImageView) row.findViewById(R.id.list_item_movie_image_view);
                row.setTag(holder);
            } else {
                holder = (MovieItemHolder) row.getTag();
            }

            MovieItem item = movies.get(position);
            String imageBasePath = "http://image.tmdb.org/t/p/" + "w185";
            Picasso.with(context).load(imageBasePath + item.posterPath).into(holder.image);
            return row;
        }
    }

    ArrayList<MovieItem> movies;
    MovieListAdapter movieItemArrayAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstainceState) {
        View rootView = inflater.inflate(R.layout.fragment_movielist_grid, container, false);
        movies = new ArrayList<MovieItem>();
        movieItemArrayAdapter = new MovieListAdapter(
            getActivity(),
            R.layout.list_item_movie,
            movies
        );
        GridView movieGrid = (GridView) rootView.findViewById(R.id.listview_movies);
        movieGrid.setAdapter(movieItemArrayAdapter);
        movieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MovieItem movieItem = movieItemArrayAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class)
                        .putExtra("title", movieItem.title)
                        .putExtra("posterPath", movieItem.posterPath)
                        .putExtra("overview", movieItem.overview)
                        .putExtra("voteAverage", movieItem.voteAverage)
                        .putExtra("releaseDate", movieItem.releaseDate);
                startActivity(intent);
            }
        });

        class FetchMoviesTask extends AsyncTask<URL, Void, String> {
            protected String doInBackground(URL... urls) {
                // These two need to be declared outside the try/catch
                // so that they can be closed in the finally block.
                HttpURLConnection urlConnection = null;
                BufferedReader reader = null;

                // Will contain the raw JSON response as a string.
                String forecastJsonStr = "";

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are avaiable at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    URL url = urls[0];

                    // Create the request to OpenWeatherMap, and open the connection
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
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
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    forecastJsonStr = buffer.toString();
                } catch (IOException e) {
                    Log.e("PlaceholderFragment", "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    return null;
                } finally{
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e("PlaceholderFragment", "Error closing stream", e);
                        }
                    }
                }
                return forecastJsonStr;
            }

            protected void onPostExecute(String result) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray results = jsonResponse.optJSONArray("results");
                    for (int i=0;i<results.length();i++){
                        //jsonResponse.optJSONArray("list").get(i).toString()
                        JSONObject res = results.getJSONObject(i);
                        MovieItem movie = new MovieItem(
                                res.getString("id"),
                                res.getString("original_title"),
                                res.getString("poster_path"),
                                res.getString("overview"),
                                res.getString("release_date"),
                                res.getDouble("vote_average"),
                                res.getDouble("popularity"));
                        movieItemArrayAdapter.add(movie);
                    }
                }
                catch (JSONException e) {
                    System.out.println("error" + e);
                }
            }
        }

        try {
            URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=d45432122734ded51fb9af81eaea9d22");
            new FetchMoviesTask().execute(url);
        } catch (Exception e) {
            System.out.println("Boohoo" + e);
        }

        return rootView;

    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        class MovieRatingComparator implements Comparator<MovieItem> {
            @Override
            public int compare(MovieItem o1, MovieItem o2) {
                return o2.voteAverage.compareTo(o1.voteAverage);
            }
        }
        class MoviePopularityComparator implements Comparator<MovieItem> {
            @Override
            public int compare(MovieItem o1, MovieItem o2) {
                return o2.popularity.compareTo(o1.popularity);
            }
        }
        if (id == R.id.action_sort_by_highest_rated) {
            Collections.sort(movies, new MovieRatingComparator());
            movieItemArrayAdapter.notifyDataSetChanged();
        } else if (id == R.id.action_sort_by_most_popular) {
            Collections.sort(movies, new MoviePopularityComparator());
            movieItemArrayAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_movies, menu);

    }

}
