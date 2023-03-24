package com.alib.myanimelist.ui.FavAnime;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FavAnimeFragment extends Fragment {
    String a ;
    private AnimeDatabaseHelper dbHelper;
    private RecyclerView mRecyclerView;
    private List<Integer> mFavoriteAnimeIds;
    private GridLayoutManager mLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav_anime, container, false);
        dbHelper = new AnimeDatabaseHelper(getActivity());
        mRecyclerView = view.findViewById(R.id.fav_anime_list);

        int numColumns = getNumColumns();
        mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Cursor cursor = dbHelper.readAllData();
        List<Integer> animeIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            animeIds.add(R.drawable.ic_launcher_foreground);
        }
        FavAnimeAdapter adapter = new FavAnimeAdapter(animeIds, numColumns, cursor);
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
        private Cursor mCursor;

        public FavAnimeAdapter(List<Integer> animeIds, int numColumns,Cursor cursor) {
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




            while (mCursor.moveToNext()){
                Toast.makeText(getContext(),mCursor.getString(1),Toast.LENGTH_SHORT).show();

            }



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

    }
}