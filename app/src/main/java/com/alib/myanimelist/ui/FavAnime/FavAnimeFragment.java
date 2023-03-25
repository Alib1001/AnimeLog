package com.alib.myanimelist.ui.FavAnime;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.AnimeAdapter;
import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FavAnimeFragment extends Fragment {
    private AnimeDatabaseHelper dbHelper;
    List<net.sandrohc.jikan.model.anime.Anime> animeList;
    private RecyclerView mRecyclerView;
    private List<Integer> mFavoriteAnimeIds;
    private GridLayoutManager mLayoutManager;
    private FavAnimeAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new AnimeDatabaseHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_anime, container, false);
        mRecyclerView = view.findViewById(R.id.fav_anime_list);

        int numColumns = getNumColumns();
        mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Cursor cursor = dbHelper.readAllData();
        List<Integer> animeIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            animeIds.add(R.drawable.ic_launcher_foreground);
        }
        mAdapter = new FavAnimeAdapter(animeIds, numColumns, cursor);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }


    private int getNumColumns() {
        int numColumns = 2; // Default number of columns
        int orientation = getResources().getConfiguration().orientation;
        int screenWidthDp = getResources().getConfiguration().screenWidthDp;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE || screenWidthDp >= 600) {
            // If the device is in landscape orientation or has a screen width of 600dp or more,
            // use 3 columns
            numColumns = 3;
        }

        return numColumns;
    }



    @Override
    public void onResume() {
        super.onResume();
        // Update the dataset and notify the adapter of the changes
        Cursor cursor = dbHelper.readAllData();
        List<Integer> animeIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            animeIds.add(R.drawable.ic_launcher_foreground);
        }
        mAdapter.updateData(animeIds, cursor);
        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
    }


    private class FavAnimeAdapter extends RecyclerView.Adapter<FavAnimeAdapter.ViewHolder> {

        private List<Integer> mAnimeIds;
        private int mNumColumns;
        private Cursor mCursor;

        public FavAnimeAdapter(List<Integer> animeIds, int numColumns, Cursor cursor) {
            mAnimeIds = animeIds;
            mNumColumns = numColumns;
            mCursor = cursor;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_anime_item, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = parent.getWidth() / mNumColumns;
            view.setLayoutParams(layoutParams);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            String title = mCursor.getString(1);
            String imageUrl = mCursor.getString(2);
            holder.titleTextView.setText(title);
            Picasso.get().load(imageUrl).into(holder.mImageView);

        }

        @Override
        public int getItemCount() {
            return mAnimeIds.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView mImageView;
            public TextView titleTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.anime_image);
                titleTextView = itemView.findViewById(R.id.anime_title);
            }
        }


        public void updateData(List<Integer> animeIds, Cursor cursor) {
            mAnimeIds.clear();
            mAnimeIds.addAll(animeIds);
            mCursor = cursor;
            notifyDataSetChanged();
        }

        public void updateAnimeIds(List<Integer> animeIds) {
            mAnimeIds = animeIds;
            notifyDataSetChanged();
        }
    }
}