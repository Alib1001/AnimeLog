package com.alib.myanimelist.ui.FavAnime;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.R;

import java.util.ArrayList;
import java.util.List;

public class FavAnimeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Integer> mFavoriteAnimeIds;
    private GridLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoriteAnimeIds = new ArrayList<>();
        mFavoriteAnimeIds.add(R.drawable.ic_launcher_foreground);
        mFavoriteAnimeIds.add(R.drawable.ic_launcher_foreground);
        mFavoriteAnimeIds.add(R.drawable.ic_launcher_foreground);
        mFavoriteAnimeIds.add(R.drawable.ic_launcher_foreground);
        mFavoriteAnimeIds.add(R.drawable.ic_launcher_foreground);
        



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_anime, container, false);
        mRecyclerView = view.findViewById(R.id.fav_anime_list);

        int numColumns = getNumColumns();
        mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        FavAnimeAdapter adapter = new FavAnimeAdapter(mFavoriteAnimeIds, numColumns);
        mRecyclerView.setAdapter(adapter);

        return view;

    }

    /**
     * Returns the number of columns in the RecyclerView based on the current device orientation
     * and screen size.
     *
     * @return The number of columns to display in the RecyclerView
     */
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

    /**
     * Adapter class for the RecyclerView
     */
    private class FavAnimeAdapter extends RecyclerView.Adapter<FavAnimeAdapter.ViewHolder> {

        private List<Integer> mAnimeIds;
        private int mNumColumns;

        public FavAnimeAdapter(List<Integer> animeIds, int numColumns) {
            mAnimeIds = animeIds;
            mNumColumns = numColumns;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_anime_item, parent, false);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = parent.getWidth() / mNumColumns; // Set the width of the item view
            view.setLayoutParams(layoutParams);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.mImageView.setImageResource(mAnimeIds.get(position));
        }

        @Override
        public int getItemCount() {
            return mAnimeIds.size();
        }

        /**
         * ViewHolder class for the RecyclerView
         */
        public class ViewHolder extends RecyclerView.ViewHolder {

            public ImageView mImageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.anime_image);
            }
        }
    }
}