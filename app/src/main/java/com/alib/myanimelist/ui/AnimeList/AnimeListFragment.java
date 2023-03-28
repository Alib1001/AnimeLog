package com.alib.myanimelist.ui.AnimeList;

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
    private List<net.sandrohc.jikan.model.anime.Anime> animeList;

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
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                    // Load more data
                    loadMoreData();
                }
            }
        });

        return view;
    }

    Jikan jikan = new Jikan.JikanBuilder()
            .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
            .build();

    private List<Anime> getAnimeList() {
        Jikan jikan = new Jikan.JikanBuilder()
                .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                .build();

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


    @Override
    public void onResume() {
        super.onResume();
        animeAdapter.notifyDataSetChanged();
    }

    private void loadMoreData() {
        isLoading = true;

        int currentPage = animeList.size() / 10 + 1;
        try {
            List<Anime> pageResults = jikan.query().anime().top()
                    .page(currentPage)
                    .execute()
                    .collectList()
                    .block();
            if (pageResults != null && !pageResults.isEmpty()) {
                animeList.addAll(pageResults);
                animeAdapter.notifyDataSetChanged();
            }
        } catch (JikanQueryException e) {
            Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
        }

        isLoading = false;
    }

}
