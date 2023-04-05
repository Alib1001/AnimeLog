package com.alib.myanimelist.SearchFragment;

public class AnimeData {
    private String title;
    private String imageUrl;

    public AnimeData(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
