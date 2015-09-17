package com.example.dhruv.project1;

/**
 * Created by dhruv on 9/3/15.
 */
public class MovieItem {
    String id;
    String title;
    String posterPath;
    String overview;
    String releaseDate;
    Double voteAverage;
    Double popularity;

    public MovieItem(String movieId,  String movieTitle, String moviePosterPath, String movieOverview, String movieReleaseDate, Double movieVoteAverage, Double moviePopularity) {
        id = movieId;
        title = movieTitle;
        posterPath = moviePosterPath;
        overview = movieOverview;
        releaseDate = movieReleaseDate;
        voteAverage = movieVoteAverage;
        popularity = moviePopularity;
    }
}
