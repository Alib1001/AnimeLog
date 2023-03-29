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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.alib.myanimelist.ui.Details.DetailsActivity;
import com.alib.myanimelist.ui.FavAnime.FavAnimeFragment;
import com.squareup.picasso.Picasso;

import java.util.List;
public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {


    private List<net.sandrohc.jikan.model.anime.Anime> animeList;
    private boolean isLoading = false;

    private LayoutInflater inflater;
    private AnimeDatabaseHelper dbHelper;
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

        if (dbHelper.checkIfFav(anime.getMalId())) {
            holder.addToFavBtn.setBackground(fav_filled);
        }
        else {
            holder.addToFavBtn.setBackground(fav_unfilled);
        }


        String imageUrl = anime.images.getPreferredImageUrl().toString();
        holder.titleTextView.setText(anime.getTitle());
        Picasso.get().load(imageUrl).into(holder.bannerImageView);
        holder.genreTextView.setText(String.valueOf("EP: " + anime.getEpisodes()));
        holder.ratingTextView.setText(String.valueOf(anime.getRating()));



        holder.item_anime_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                net.sandrohc.jikan.model.anime.Anime anime = animeList.get(holder.getAdapterPosition());
                DetailsActivity.anime = anime;
                Intent intent = new Intent(mContext, DetailsActivity.class);
                mContext.startActivity(intent);
            }
        });
        holder.addToFavBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                net.sandrohc.jikan.model.anime.Anime anime = animeList.get(holder.getAdapterPosition());
                dbHelper.updateAnime(anime);
                dbHelper.close();

                if (dbHelper.checkIfFav(anime.getMalId())) {
                    holder.addToFavBtn.setBackground(fav_filled);
                }
                else {
                    holder.addToFavBtn.setBackground(fav_unfilled);
                }

                Intent databaseUpdatedIntent = new Intent(FavAnimeFragment.ACTION_DATABASE_UPDATED);
                mContext.sendBroadcast(databaseUpdatedIntent);


            }
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

