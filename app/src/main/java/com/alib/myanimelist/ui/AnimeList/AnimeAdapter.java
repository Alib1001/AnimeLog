package com.alib.myanimelist.ui.AnimeList;



import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.alib.myanimelist.ui.Details.DetailsActivity;
import com.alib.myanimelist.ui.FavAnime.FavAnimeFragment;
import com.squareup.picasso.Picasso;

import net.sandrohc.jikan.model.anime.Anime;

import java.io.IOException;
import java.util.List;
public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {


    private final List<net.sandrohc.jikan.model.anime.Anime> animeList;

    private final LayoutInflater inflater;
    private final AnimeDatabaseHelper dbHelper;
    Context mContext;

    public AnimeAdapter(Context context, List<net.sandrohc.jikan.model.anime.Anime> animeList) {
        this.animeList = animeList;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.dbHelper = new AnimeDatabaseHelper(context);
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        net.sandrohc.jikan.model.anime.Anime anime = animeList.get(position);
        Drawable fav_filled = ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_filled);
        Drawable fav_unfilled = ContextCompat.getDrawable(mContext, R.drawable.ic_fav_unfilled);
        Drawable fav_settings = ContextCompat.getDrawable(mContext,R.drawable.ic_settings);

        if (dbHelper.checkIfFav(anime.getMalId())) {
            holder.addToFavBtn.setBackground(fav_filled);
        }
        else {
            holder.addToFavBtn.setBackground(fav_unfilled);
        }


        String imageUrl = anime.images.getPreferredImageUrl();
        holder.titleTextView.setText(anime.getTitle());
        Picasso.get().load(imageUrl).into(holder.bannerImageView);
        holder.genreTextView.setText(String.valueOf("EP: " + anime.getEpisodes()));
        holder.ratingTextView.setText(String.valueOf(anime.getScore()));


        holder.item_anime_linear.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("malId",anime.getMalId());
            mContext.startActivity(intent);

            DetailsActivity.showConfigBtn = false;
        });
        holder.addToFavBtn.setOnClickListener(v -> {

           Anime anime1 = animeList.get(holder.getAdapterPosition());
           dbHelper.updateAnime(anime1);


           dbHelper.exportDataToTxt();
           dbHelper.exportDataToJSON();
           dbHelper.close();

            if (dbHelper.checkIfFav(anime1.getMalId())) {
                holder.addToFavBtn.setBackground(fav_filled);
            }
            else {
                holder.addToFavBtn.setBackground(fav_unfilled);
            }
            Intent databaseUpdatedIntent = new Intent(FavAnimeFragment.ACTION_DATABASE_UPDATED);
            mContext.sendBroadcast(databaseUpdatedIntent);

        });
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        public ImageView bannerImageView;
        public TextView titleTextView;
        public TextView genreTextView;
        public TextView ratingTextView;
        public ImageButton addToFavBtn;
        public LinearLayout item_anime_linear;

        public AnimeViewHolder(View itemView) {
            super(itemView);

            addToFavBtn = itemView.findViewById(R.id.add_to_favorites_button);
            bannerImageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            genreTextView = itemView.findViewById(R.id.genre_text_view);
            ratingTextView = itemView.findViewById(R.id.rating_text_view);
            item_anime_linear = itemView.findViewById(R.id.item_anime_linear);
        }
    }


}

