package com.alib.myanimelist.ui.Details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.alib.myanimelist.ui.FavAnime.FavAnimeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;

import io.netty.resolver.DefaultAddressResolverGroup;

public class DetailsActivity extends AppCompatActivity {
    private AnimeDatabaseHelper dbHelper;
    private Anime anime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        TextView titleTextView = findViewById(R.id.detail_anime_title);
        TextView descriptionTextView = findViewById(R.id.animeDescription);
        ImageView bannerImageView = findViewById(R.id.detail_anime_image);
        FloatingActionButton addToFavBtn = findViewById(R.id.addToFav);

        dbHelper = new AnimeDatabaseHelper(getApplicationContext());

        Intent intent = getIntent();
        anime = (Anime) intent.getSerializableExtra("anime");


        Jikan jikan = new Jikan.JikanBuilder()
                .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                .build();

        try {
            Anime animeForDescription = jikan.query().anime().get(anime.getMalId())
                    .execute()
                    .block();

            String description = animeForDescription.getSynopsis();
            descriptionTextView.setText(description);
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }

        String title = anime.getTitle();
        String bannerImageUrl = anime.getImages().getPreferredImageUrl();
        titleTextView.setText(title);
        Picasso.get().load(bannerImageUrl).into(bannerImageView);

        addToFavBtn.setOnClickListener(v -> {
            dbHelper.updateAnime(anime);
            dbHelper.close();

            Intent databaseUpdatedIntent = new Intent(FavAnimeFragment.ACTION_DATABASE_UPDATED);
            getApplicationContext().sendBroadcast(databaseUpdatedIntent);
        });
    }
}

