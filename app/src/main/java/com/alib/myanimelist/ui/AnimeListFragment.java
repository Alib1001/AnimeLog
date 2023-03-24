package com.alib.myanimelist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.AnimeAdapter;
import com.alib.myanimelist.R;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;

import java.util.ArrayList;
import java.util.List;

import io.netty.resolver.DefaultAddressResolverGroup;

public class AnimeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
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
        return view;
    }

    private List<net.sandrohc.jikan.model.anime.Anime> getAnimeList() {
        List<net.sandrohc.jikan.model.anime.Anime> animeList = new ArrayList<>();

        Jikan jikan = new Jikan.JikanBuilder()
                .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                .build();

        // Fetch the anime with ID 1. Returns a single value, a mono.
        net.sandrohc.jikan.model.anime.Anime anime = null;
        try {
            anime = jikan.query().anime().get(1)
                    .execute().block();
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }

        // Search for 'sword art online'. Returns a list of values, a flux.
        List<net.sandrohc.jikan.model.anime.Anime> results = new ArrayList<>();
        try {
            results = jikan.query().anime().search()
                    .execute()
                    .collectList()
                    .block();
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }




        results.add(anime);

        return results;
    }


}
