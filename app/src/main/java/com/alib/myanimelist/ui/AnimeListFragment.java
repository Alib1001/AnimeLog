package com.alib.myanimelist.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alib.myanimelist.Anime;
import com.alib.myanimelist.AnimeAdapter;
import com.alib.myanimelist.R;

import java.util.ArrayList;
import java.util.List;

public class AnimeListFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnimeAdapter animeAdapter;
    private List<Anime> animeList;

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

    private List<Anime> getAnimeList() {
        List<Anime> animeList = new ArrayList<>();

        animeList.add(new Anime("Isekai",4, "Naruto", "2002-2007", "Action, Adventure, Comedy, Super Power, Martial Arts, Shounen"));
        animeList.add(new Anime("Isekai",2, "Bleach", "2006-2007", "Mystery, Police, Psychological, Supernatural, Thriller"));
        animeList.add(new Anime("Isekai",10, "Kaguya", "2002-2007", "Action, Adventure, Comedy, Drama, Magic, Fantasy, Shounen"));
        animeList.add(new Anime("Isekai",10, "NHK", "2002-2007", "Action, Drama, Fantasy, Military, Mystery, Shounen, Super Power"));
        animeList.add(new Anime("Isekai",1, "Maid", "1999-", "Action, Adventure, Comedy, Fantasy, Shounen, Super Power"));
        animeList.add(new Anime("Isekai",1, "Kobayashi", "2002-2007", "Action, Adventure, Comedy, Super Power, Supernatural, Shounen"));
        return animeList;
    }
}
