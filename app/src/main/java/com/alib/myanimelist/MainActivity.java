
package com.alib.myanimelist;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alib.myanimelist.ui.AnimeList.AnimeListFragment;
import com.alib.myanimelist.ui.FavAnime.FavAnimeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;
import net.sandrohc.jikan.model.anime.AnimeOrderBy;
import net.sandrohc.jikan.model.enums.SortOrder;

import java.util.Collection;

import io.netty.resolver.DefaultAddressResolverGroup;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FragmentManager fragmentManager;
    private Fragment activeFragment;

    private Fragment animeListFragment;
    private Fragment favoriteAnimeFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();

        animeListFragment = new AnimeListFragment();
        favoriteAnimeFragment = new FavAnimeFragment();

        fragmentManager.beginTransaction().add(R.id.container, favoriteAnimeFragment, "2").hide(favoriteAnimeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, animeListFragment, "1").commit();
        activeFragment = animeListFragment;


        TextView titleView = findViewById(R.id.actionbar_title);

        SearchView searchView = findViewById(R.id.actionbar_search);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                titleView.setVisibility(View.GONE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setVisibility(View.GONE);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleView.setVisibility(View.VISIBLE);
                return false;
            }
        });


        Jikan jikan = new Jikan.JikanBuilder()
                .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
                .build();


        try {
            Collection<Anime> results = jikan.query().anime().search()
                    .query("sword art online")
                    .orderBy(AnimeOrderBy.POPULARITY, SortOrder.ASCENDING)
                    .execute()
                    .collectList()
                    .block();
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();


        if (itemId == R.id.navigation_anime_list) {
            fragmentManager.beginTransaction().hide(activeFragment).show(animeListFragment).commit();
            activeFragment = animeListFragment;

            return true;
        } else if (itemId == R.id.navigation_favorite_anime) {
            fragmentManager.beginTransaction().hide(activeFragment).show(favoriteAnimeFragment).commit();
            activeFragment = favoriteAnimeFragment;
            return true;
        }

        return false;
    }

}