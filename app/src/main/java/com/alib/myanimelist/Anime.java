package com.alib.myanimelist;

public class Anime {

    private String genre;
    private int rating;
    private String title;
    private String description;
    private String imageUrl;

    public Anime(String genre, int rating, String title, String description, String imageUrl) {
        this.genre = genre;
        this.rating = rating;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
