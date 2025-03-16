package com.Entities;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Movie {
    public enum Genre { ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI, THRILLER, ANIMATION, DOCUMENTARY }
    public enum Language { ENGLISH, SPANISH, FRENCH, GERMAN, MANDARIN, JAPANESE, HINDI, ARABIC }

    private static int counterID = 1;

    private int movieId;
    private String title;
    private String genre;
    private int duration;
    private String language;
    private String releaseDate;
    private String rating;
    private String description;

    private ArrayList<Showtime> showtimes = new ArrayList<>();

    public Movie(String title, String genre, int duration, String language, String releaseDate, String rating, String description) {
        this.movieId = counterID++;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.language = language;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.description = description;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
    public String getLanguage() { return language; }
    public String getReleaseDate() { return releaseDate; }
    public String getRating() { return rating; }
    public String getDescription() { return description; }

    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setLanguage(String language) { this.language = language; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public void setRating(String rating) { this.rating = rating; }
    public void setDescription(String description) { this.description = description; }

    public void addShowtime(Showtime showtime) {
        showtimes.add(showtime);
    }
    public void removeShowtime(Showtime showtime) {
        showtimes.remove(showtime);
    }
    @Override
    public String toString() {
        return "\nMovie ID: " + movieId +
                "\nTitle: " + title +
                "\nGenre: " + genre +
                "\nDuration: " + duration +
                "\nLanguage: " + language +
                "\nRelease Date: " + releaseDate +
                "\nRating: " + rating +
                "\nDescription: " + description;
    }
}
