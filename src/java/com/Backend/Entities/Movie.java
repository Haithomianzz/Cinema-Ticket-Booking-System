package com.Backend.Entities;
import java.util.ArrayList;
import com.Frontend.Date;
public class Movie {


    public enum Genre { ACTION, COMEDY, DRAMA, HORROR, ROMANCE, SCI_FI, THRILLER, ANIMATION, DOCUMENTARY }
    public enum Language { ENGLISH, SPANISH, FRENCH, GERMAN, MANDARIN, JAPANESE, HINDI, ARABIC }

    private static int counterID = 0;

    private final int movieId;
    private String title;
    private String description;


    private Language language;

    private Float rating;
    private int duration;
    private Date releaseDate;
    private ArrayList<Genre> genres;
    private byte[] imageData; // Changed from String Image to byte[] imageData
    private ArrayList<Showtime> showtimes = new ArrayList<>();

    public Movie(String title, String description, Float rating, String language, int duration, String releaseDate, ArrayList<Genre> genres, byte[] imageData) {
        this.movieId = counterID++;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.language = Language.valueOf(language.toUpperCase());
        this.duration = duration;
        this.releaseDate = new Date(releaseDate); // Ensure Date class handles this format
        this.genres = genres;
        this.imageData = imageData; // Assign byte array
    }
    public Movie(int movieId, String title, String description, Float rating, String language, int duration, String releaseDate, ArrayList<Genre> genres, byte[] imageData) {
        this.movieId = movieId;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.language = Language.valueOf(language.toUpperCase());
        this.duration = duration;
        this.releaseDate = new Date(releaseDate); // Ensure Date class handles this format
        this.genres = genres;
        this.imageData = imageData; // Assign byte array
    }
    public void editMovie(String title, String description, Float rating, String language, int duration, String releaseDate, ArrayList<Genre> genres /*, byte[] newImageData */) {
        this.title = (title != null && !title.equals(this.title)) ? title : this.title;
        this.description = (description != null && !description.equals(this.description)) ? description : this.description;
        this.rating = (rating != null && !rating.equals(this.rating)) ? rating : this.rating;
        this.language = (language != null && !language.equals(this.language.toString())) ? Language.valueOf(language.toUpperCase()) : this.language;
        this.duration = (duration != 0 && duration != this.duration) ? duration : this.duration;
        this.releaseDate = (releaseDate != null && !releaseDate.equals(this.releaseDate.toString())) ? new Date(releaseDate) : this.releaseDate;
        this.genres = (genres != null && !genres.equals(this.genres)) ? genres : this.genres;
        // if (newImageData != null) { this.imageData = newImageData; }
    }
    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public int getDuration() { return duration; }
    public Language getLanguage() { return language; }
    public Date getReleaseDate() { return releaseDate; }
    public Float getRating() { return rating; }
    public String getDescription() { return description; }
    public byte[] getImageData() { return imageData; }
    public ArrayList<Genre> getGenres() { return genres; }
    public ArrayList<Showtime> getShowtimes() { return showtimes; }


    public static void setMovieIdCounter(int counterID) { Movie.counterID = counterID; }

    public void setTitle(String title) { this.title = title; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setLanguage(String language) { this.language = Language.valueOf(language); }
    public void setReleaseDate(String releaseDate) { this.releaseDate = new Date(releaseDate); }
    public void setRating(Float rating) { this.rating = rating; }
    public void setDescription(String description) { this.description = description; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

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
                "\nDuration: " + duration +
                "\nLanguage: " + language +
                "\nRelease Date: " + releaseDate +
                "\nRating: " + rating +
                "\nDescription: " + description +
                "\nGenres: " + genres;
    }
}
