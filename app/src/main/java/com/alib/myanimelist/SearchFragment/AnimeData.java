package com.alib.myanimelist.SearchFragment;

public class AnimeData {
    private String title;
    private String imageUrl;
    private int malID;

    private Integer eps;


    public AnimeData(String title, String imageUrl, int malID, Integer eps) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.malID = malID;
        this.eps = eps;
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

    public int getEps() {
        return eps;
    }

    public void setEps(int eps) {
        this.eps = eps;
    }
}
