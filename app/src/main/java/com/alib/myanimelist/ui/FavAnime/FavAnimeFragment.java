package com.alib.myanimelist.ui.FavAnime;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;
import com.alib.myanimelist.ui.Details.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class FavAnimeFragment extends Fragment {
    private AnimeDatabaseHelper dbHelper;
    private RecyclerView mRecyclerView;
    private FavAnimeAdapter mAdapter;
    private Cursor mCursor;
    public static final String ACTION_DATABASE_UPDATED = "com.alib.myanimelist.DATABASE_UPDATED";
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
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), numColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mCursor = dbHelper.readAllData();
        mAdapter = new FavAnimeAdapter(numColumns, mCursor);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }



    private int getNumColumns() {
        int numColumns = 2;
        int orientation = getResources().getConfiguration().orientation;
        int screenWidthDp = getResources().getConfiguration().screenWidthDp;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE || screenWidthDp >= 600) {

            numColumns = 3;
        }

        return numColumns;
    }


    @Override
    public void onResume() {
        super.onResume();
        mRecyclerView.clearOnScrollListeners();
        IntentFilter filter = new IntentFilter(ACTION_DATABASE_UPDATED);
        getActivity().registerReceiver(mDatabaseUpdatedReceiver, filter);
        Cursor newCursor = dbHelper.readAllData();
        if (newCursor.getCount() != mCursor.getCount()) {
            mCursor = newCursor;
            fetchDataAndUpdateAdapter();
        }
        newCursor.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mDatabaseUpdatedReceiver);
    }


    private class FavAnimeAdapter extends RecyclerView.Adapter<FavAnimeAdapter.ViewHolder> {

        private Cursor mCursor;

        public FavAnimeAdapter(int numColumns, Cursor cursor) {

            mCursor = cursor;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fav_anime_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            mCursor.moveToPosition(position);
            String title = mCursor.getString(mCursor.getColumnIndexOrThrow(AnimeDatabaseHelper.COLUMN_TITLE));
            String imageUrl = mCursor.getString(mCursor.getColumnIndexOrThrow(AnimeDatabaseHelper.COLUMN_IMAGE_URI));
            int malId = mCursor.getInt(mCursor.getColumnIndexOrThrow(AnimeDatabaseHelper.COLUMN_MAL_ID));

            holder.mTitleTextView.setText(title);
            Picasso.get().load(imageUrl).into(holder.mImageView);

            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailsActivity.showConfigBtn = true;
                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra("malId", malId);
                    getContext().startActivity(intent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return mCursor.getCount();
        }

        public void updateData(Cursor cursor) {
            mCursor = cursor;
            mAdapter.notifyDataSetChanged();
            notifyDataSetChanged();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageView;
            TextView mTitleTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.anime_image);
                mTitleTextView = itemView.findViewById(R.id.anime_title);

            }
        }
    }

    private void fetchDataAndUpdateAdapter() {
        Cursor newCursor = dbHelper.readAllData();
        List<Integer> animeIds = new ArrayList<>();
        while (newCursor.moveToNext()) {
            animeIds.add(R.drawable.ic_launcher_foreground);
        }
        mAdapter.updateData(newCursor);
    }

    private final BroadcastReceiver mDatabaseUpdatedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction().equals(ACTION_DATABASE_UPDATED)) {
                fetchDataAndUpdateAdapter();
            }
        }
    };

}