package com.alib.myanimelist.ui.FavAnime;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavAnimeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public FavAnimeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is favAnime fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}