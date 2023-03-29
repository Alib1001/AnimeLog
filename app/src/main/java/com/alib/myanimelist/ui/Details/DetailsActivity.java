package com.alib.myanimelist.ui.Details;

import android.os.Bundle;
import com.alib.myanimelist.R;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView bannerImageView;



    public static net.sandrohc.jikan.model.anime.Anime anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titleTextView = findViewById(R.id.detail_anime_title);
        descriptionTextView = findViewById(R.id.animeDescription);
        bannerImageView = findViewById(R.id.detail_anime_image);

        String title = anime.getTitle();
        String description = anime.demographics.toString();
        String bannerImageUrl = anime.images.getPreferredImageUrl();
        titleTextView.setText(title);


        Picasso.get().load(bannerImageUrl).into(bannerImageView);
    }
}