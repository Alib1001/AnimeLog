package com.alib.myanimelist.SearchFragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
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

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        searchInDB();
    }

    private void searchAnimeinAPI() {
        try {
            Jikan jikan = new Jikan.JikanBuilder()
                    .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                    .build();

            List<AnimeData> dataList = new ArrayList<>();
            Collection<Anime> searchResult = jikan.query().anime().search()
                    .query("sword art online")
                    .orderBy(AnimeOrderBy.POPULARITY, SortOrder.ASCENDING)
                    .execute()
                    .collectList()
                    .block();

            for (Anime anime : searchResult) {
                String title = anime.getTitle();
                String imageUrl = anime.images.getPreferredImageUrl();
                dataList.add(new AnimeData(title, imageUrl));
            }

            mAdapter.setData(dataList);
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }
    }

    private List<AnimeData> getDataFromDB(String title) {

        AnimeDatabaseHelper dbHelper = new AnimeDatabaseHelper(getContext());

        List<AnimeData> dataList = new ArrayList<>();

        Cursor cursor = dbHelper.searchData(title);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve the values for each column in the current row
                @SuppressLint("Range") String animeTitle = cursor.getString(cursor.getColumnIndex(AnimeDatabaseHelper.COLUMN_TITLE));
                @SuppressLint("Range") String imageUrl = cursor.getString(cursor.getColumnIndex(AnimeDatabaseHelper.COLUMN_IMAGE_URI));

                // Add the retrieved data to the list
                dataList.add(new AnimeData(animeTitle, imageUrl));
            } while (cursor.moveToNext());
        }

        cursor.close();
        dbHelper.close();

        return dataList;
    }

    private void searchInDB() {
        List<AnimeData> dataList = getDataFromDB("Monster");
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
            holder.animeTextView.setText(title);
            Picasso.get().load(imageUrl).into(holder.animeImageView);
        }


        @Override
        public int getItemCount() {
            return mData.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView animeTextView;
            ImageView animeImageView;

            ViewHolder(View itemView) {
                super(itemView);
                animeTextView = itemView.findViewById(R.id.title_text_view);
                animeImageView = itemView.findViewById(R.id.image_view);
            }
        }
    }
}
