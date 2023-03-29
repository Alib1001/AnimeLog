package com.alib.myanimelist.ui.AnimeList;

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
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;

import java.util.ArrayList;
import java.util.List;

import io.netty.resolver.DefaultAddressResolverGroup;

public class AnimeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
    private boolean isLoading = false;
    private List<Anime> animeList;

    private int visibleItemCount;
    private int firstVisibleItem;
    private int totalItemCount;
    private boolean isLoadingImages = false;
    private boolean isScrolling = false;
    private int preloadOffset = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_anime_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        animeList = getAnimeList();
        animeAdapter = new AnimeAdapter(getContext(), animeList);
        recyclerView.setAdapter(animeAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition() ;

                if (!isLoading && totalItemCount <= (lastVisibleItem + 20)) {
                    new LoadMoreDataTask().execute();
                }
            }
        });

        return view;
    }

    Jikan jikan = new Jikan.JikanBuilder()
            .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
            .build();

    private class LoadMoreDataTask extends AsyncTask<Void, Void, List<Anime>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;

        }

        @Override
        protected List<Anime> doInBackground(Void... voids) {
            int currentPage = animeList.size() / 50 + 1;
            try {
                return jikan.query().anime().top()
                        .page(currentPage)
                        .execute()
                        .collectList()
                        .block();
            } catch (JikanQueryException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Anime> results) {
            super.onPostExecute(results);
            isLoading = false;
            if (results != null && !results.isEmpty()) {
                animeList.addAll(results);
                animeAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "No more data to load", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private List<Anime> getAnimeList() {


        List<Anime> results = new ArrayList<>();

        try {
            results = jikan.query().anime().top()
                    .page(1)
                    .execute()
                    .collectList()
                    .block();
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }

        return results;
    }

}

