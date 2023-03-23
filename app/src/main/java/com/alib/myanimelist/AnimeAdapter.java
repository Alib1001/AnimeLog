package com.alib.myanimelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private List<net.sandrohc.jikan.model.anime.Anime> animeList;
    private LayoutInflater inflater;

    public AnimeAdapter(Context context, List<net.sandrohc.jikan.model.anime.Anime> animeList) {
        this.animeList = animeList;
        this.inflater = LayoutInflater.from(context);
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
        String imageUrl = anime.images.getPreferredImageUrl();

        holder.titleTextView.setText(anime.getTitle());
        Picasso.get().load(imageUrl).into(holder.bannerImageView);
        holder.ratingTextView.setText(String.valueOf(anime.getRating()));
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
        public AnimeViewHolder(View itemView) {
            super(itemView);
            bannerImageView = itemView.findViewById(R.id.image_view);
            titleTextView = itemView.findViewById(R.id.title_text_view);
            genreTextView = itemView.findViewById(R.id.genre_text_view);
            ratingTextView = itemView.findViewById(R.id.rating_text_view);
        }
    }
}
