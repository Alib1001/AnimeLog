package com.alib.myanimelist.SearchFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.alib.myanimelist.ui.Details.DetailsActivity;
import com.squareup.picasso.Picasso;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.enums.SortOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.netty.resolver.DefaultAddressResolverGroup;

    public class SearchFragment extends Fragment {

        private static MyAdapter mAdapter;



        private final static Jikan jikan = new Jikan.JikanBuilder()
                .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                .build();

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            RecyclerView mRecyclerView = rootView.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new MyAdapter();
            mRecyclerView.setAdapter(mAdapter);


            return rootView;
        }



        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
        public static void getDataFromDB(Context context,String title) {
            List<AnimeData> dataList = new ArrayList<>();
            AnimeDatabaseHelper dbHelper = new AnimeDatabaseHelper(context);
            Cursor cursor = dbHelper.searchData(title);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String animeTitle = cursor.getString(cursor.getColumnIndex(AnimeDatabaseHelper.COLUMN_TITLE));
                    @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(AnimeDatabaseHelper.COLUMN_IMAGE_URI));
                    @SuppressLint("Range") int malId = cursor.getInt(cursor.getColumnIndex(AnimeDatabaseHelper.COLUMN_MAL_ID));
                    dataList.add(new AnimeData(animeTitle, imageUrl,malId));
                } while (cursor.moveToNext());
            }
            if (cursor != null) {
                cursor.close();
            }
            dbHelper.close();
            mAdapter.setData(dataList);
        }


        public static void getAnimeFromApi(String animeTitle)  {
            Collection<Anime> searchResult = null;
            try {
                searchResult = jikan.query().anime().search().limit(5)
                        .query(animeTitle)
                        .orderBy(AnimeOrderBy.POPULARITY, SortOrder.ASCENDING)
                        .execute()
                        .collectList()
                        .block();
            } catch (JikanQueryException e) {
                throw new RuntimeException(e);
            }

            List<AnimeData> dataList = new ArrayList<>();

            for (Anime anime :searchResult) {
                String title = anime.getTitle();
                String imageUrl = anime.images.getPreferredImageUrl();
                int malId = anime.getMalId();
                dataList.add(new AnimeData(title, imageUrl,malId));
            }

            mAdapter.setData(dataList);
        }





        private static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
            private List<AnimeData> mData = new ArrayList<>();

            public void setData(List<AnimeData> data) {
                mData.clear();
                mData.addAll(data);
                notifyDataSetChanged();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_search_anime, parent, false);
                return new ViewHolder(itemView);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                AnimeData item = mData.get(position);
                String title = item.getTitle();
                String imageUrl = item.getImageUrl();
                int malId = item.getMalID();

                Context context = holder.item.getContext();

                holder.animeTextView.setText(title);
                Picasso.get().load(imageUrl).into(holder.animeImageView);

                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra("malId", malId);
                        context.startActivity(intent);
                    }
                });
            }


            @Override
            public int getItemCount() {
                return mData.size();
            }

            static class ViewHolder extends RecyclerView.ViewHolder {
                TextView animeTextView;
                ImageView animeImageView;
                LinearLayout item;

                ViewHolder(View itemView) {
                    super(itemView);
                    animeTextView = itemView.findViewById(R.id.title_text_view);
                    animeImageView = itemView.findViewById(R.id.image_view);
                    item = itemView.findViewById(R.id.item_anime_linear);

                }
            }
        }
    }
