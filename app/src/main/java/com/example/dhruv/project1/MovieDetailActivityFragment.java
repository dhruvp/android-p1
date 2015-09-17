package com.example.dhruv.project1;

import android.content.Intent;
import android.media.Image;
import android.provider.DocumentsContract;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        String imageBasePath = "http://image.tmdb.org/t/p/" + "w185";
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        ImageView posterView = (ImageView) rootView.findViewById(R.id.poster);
        Picasso.with(getActivity()).load(imageBasePath + intent.getExtras().get("posterPath")).into(posterView);
        ((TextView) rootView.findViewById(R.id.title))
            .setText(intent.getStringExtra("title"));
        ((TextView) rootView.findViewById(R.id.overview))
                .setText(intent.getStringExtra("overview"));
        ((TextView) rootView.findViewById(R.id.rating))
                .setText("Rating: " + ((Double) intent.getDoubleExtra("voteAverage", 0.0)).toString());
        ((TextView) rootView.findViewById(R.id.release_date))
                .setText("Releases on " + intent.getStringExtra("releaseDate"));
        return rootView;
    }
}
