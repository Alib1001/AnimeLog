
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
import com.alib.myanimelist.ui.Backup.BackupFragment;
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
    private Fragment backupFragment;

    private SearchView searchView;



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
        backupFragment = new BackupFragment();

        fragmentManager.beginTransaction().add(R.id.container, searchFragment, "searchFragment").hide(searchFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, favoriteAnimeFragment, "favoriteAnimeFragment").hide(favoriteAnimeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, animeListFragment, "animeListFragment").commit();
        fragmentManager.beginTransaction().add(R.id.container, backupFragment, "backupFragment").hide(backupFragment).commit();



        activeFragment = animeListFragment;
        previousFragment = activeFragment;


        TextView titleView = findViewById(R.id.actionbar_title);

        searchView = findViewById(R.id.actionbar_search);



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                titleView.setVisibility(View.GONE);
                if (previousFragment.getTag() == "favoriteAnimeFragment"){
                    SearchFragment.getDataFromDB(getApplicationContext(),query);
                }
                else if (previousFragment.getTag() == "animeListFragment"){
                    SearchFragment.getAnimeFromApi(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (previousFragment.getTag() == "favoriteAnimeFragment"){
                    SearchFragment.getDataFromDB(getApplicationContext(),newText);
                }
                else if (previousFragment.getTag() == "animeListFragment"){
                    SearchFragment.getAnimeFromApi(newText);
                }

                return true;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setVisibility(View.GONE);
                fragmentManager.beginTransaction().hide(activeFragment).show(searchFragment).commit();
                activeFragment = searchFragment;
                navigation.setVisibility(View.GONE);

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleView.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(activeFragment).show(previousFragment).commit();
                activeFragment = previousFragment;
                navigation.setVisibility(View.VISIBLE);

                return false;
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.navigation_anime_list) {
            fragmentManager.beginTransaction().hide(activeFragment).show(animeListFragment).commit();activeFragment = animeListFragment;
            previousFragment = activeFragment;
            searchView.setVisibility(View.VISIBLE);

            return true;
        } else if (itemId == R.id.navigation_favorite_anime) {
            fragmentManager.beginTransaction().hide(activeFragment).show(favoriteAnimeFragment).commit();
            activeFragment = favoriteAnimeFragment;
            previousFragment = activeFragment;
            searchView.setVisibility(View.VISIBLE);
            return true;
        }

        else if (itemId == R.id.navigation_backup) {
            fragmentManager.beginTransaction().hide(activeFragment).show(backupFragment).commit();
            activeFragment = backupFragment;
            previousFragment = activeFragment;
            searchView.setVisibility(View.GONE);

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
