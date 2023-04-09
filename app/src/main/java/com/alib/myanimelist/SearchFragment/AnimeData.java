package com.alib.myanimelist.SearchFragment;

public class AnimeData {
    private String title;
    private String imageUrl;
    private int malID;


    public AnimeData(String title, String imageUrl, int malID) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.malID = malID;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getMalID() {
        return malID;
    }

    public void setMalID(int malID) {
        this.malID = malID;
    }
}
