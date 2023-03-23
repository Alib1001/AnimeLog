package com.alib.myanimelist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Anime;
import com.alib.myanimelist.AnimeAdapter;
import com.alib.myanimelist.R;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.anime.AnimeStatus;
import net.sandrohc.jikan.model.enums.SortOrder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import io.netty.resolver.DefaultAddressResolverGroup;
import pw.mihou.jaikan.Jaikan;
import pw.mihou.jaikan.endpoints.Endpoints;

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
        List<Anime> animeList = new ArrayList<>();

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

        for (net.sandrohc.jikan.model.anime.Anime anime1 : results) {
            String imageUrl = anime.getUrl();
        }


        results.add(anime);


        animeList.add(new Anime("Isekai",anime.rank, anime.getImages().toString(), "2002-2007", "Action, Adventure, Comedy, Super Power, Martial Arts, Shounen"));
        animeList.add(new Anime("Isekai",4, "Naruto", "2002-2007", "Action, Adventure, Comedy, Super Power, Martial Arts, Shounen"));
        animeList.add(new Anime("Isekai",2, "Bleach", "2006-2007", "Mystery, Police, Psychological, Supernatural, Thriller"));
        animeList.add(new Anime("Isekai",10, "Kaguya", "2002-2007", "Action, Adventure, Comedy, Drama, Magic, Fantasy, Shounen"));
        animeList.add(new Anime("Isekai",10, "NHK", "2002-2007", "Action, Drama, Fantasy, Military, Mystery, Shounen, Super Power"));
        animeList.add(new Anime("Isekai",1, "Maid", "1999-", "Action, Adventure, Comedy, Fantasy, Shounen, Super Power"));
        animeList.add(new Anime("Isekai",1, "Kobayashi", "2002-2007", "Action, Adventure, Comedy, Super Power, Supernatural, Shounen"));
        return results;
    }


}
