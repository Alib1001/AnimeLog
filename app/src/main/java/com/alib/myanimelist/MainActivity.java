
package com.alib.myanimelist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.SearchFragment.AnimeData;
import com.alib.myanimelist.SearchFragment.SearchFragment;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.netty.resolver.DefaultAddressResolverGroup;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,SearchView.OnQueryTextListener {

    private FragmentManager fragmentManager;
    private Fragment activeFragment;
    private Fragment previousFragment;

    private Fragment animeListFragment;
    private Fragment favoriteAnimeFragment;
    private Fragment searchFragment;
    private String searchQuery;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        fragmentManager = getSupportFragmentManager();



        animeListFragment = new AnimeListFragment();
        favoriteAnimeFragment = new FavAnimeFragment();
        searchFragment = new SearchFragment();

        fragmentManager.beginTransaction().add(R.id.container, searchFragment, "searchFragment").hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, favoriteAnimeFragment, "favoriteAnimeFragment").hide(favoriteAnimeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, animeListFragment, "animeListFragment").commit();



        activeFragment = animeListFragment;
        previousFragment = activeFragment;


        TextView titleView = findViewById(R.id.actionbar_title);

        SearchView searchView = findViewById(R.id.actionbar_search);

        Intent intent = getIntent();


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
                fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                activeFragment = searchFragment;

                SearchFragment.getAnimeFromApi("Monster");

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleView.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(activeFragment).show(previousFragment).commit();
                activeFragment = previousFragment;



                return false;
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_anime_list) {
            fragmentManager.beginTransaction().hide(activeFragment).show(animeListFragment).commit();
            // Toast.makeText(getApplicationContext(),animeListFragment.getTag(),Toast.LENGTH_SHORT).show();
            activeFragment = animeListFragment;
            previousFragment = activeFragment;


            return true;
        } else if (itemId == R.id.navigation_favorite_anime) {
            fragmentManager.beginTransaction().hide(activeFragment).show(favoriteAnimeFragment).commit();
            activeFragment = favoriteAnimeFragment;
            previousFragment = activeFragment;
            return true;
        }

        return false;
    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

}