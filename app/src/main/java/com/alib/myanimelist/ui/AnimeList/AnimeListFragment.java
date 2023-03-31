package com.alib.myanimelist.ui.AnimeList;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.R;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanException;
import net.sandrohc.jikan.model.anime.Anime;

import java.util.ArrayList;
import java.util.List;


import io.netty.resolver.DefaultAddressResolverGroup;

public class AnimeListFragment extends Fragment {

    private AnimeAdapter animeAdapter;
    private boolean isLoading = false;
    private List<Anime> animeList = new ArrayList<>();

    private final int preloadOffset = 20;

    private final Jikan jikan = new Jikan.JikanBuilder()
            .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
            .build();

    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        animeAdapter = new AnimeAdapter(getContext(), animeList);
        recyclerView.setAdapter(animeAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !isLoading) {
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    if (totalItemCount - lastVisibleItemPosition <= preloadOffset) {
                        loadMoreData();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();

                int visibleItemCount = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                boolean isScrolling = (visibleItemCount + firstVisibleItem) >= totalItemCount - preloadOffset;

                if (!isLoading && isScrolling) {
                    loadMoreData();
                }
            }
        });

        loadMoreData();

        return view;
    }

    private void loadMoreData() {
        if (!isLoading) {
            isLoading = true;
            new LoadMoreDataTask().execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<Anime>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected List<Anime> doInBackground(Void... voids) {
            try {
                return jikan.query().anime().top()
                        .page(currentPage)
                        .limit(50)
                        .execute()
                        .collectList().block();
            } catch (JikanException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Anime> results) {
            super.onPostExecute(results);
            isLoading = false;
            if (results != null && !results.isEmpty()) {
                int startPosition = animeList.size();
                animeList.addAll(results);
                animeAdapter.notifyItemRangeInserted(startPosition, results.size());
                currentPage++;
            } else {
                Toast.makeText(getContext(), "No more data to load", Toast.LENGTH_SHORT).show();
            }
        }

    }
}

