package com.alib.myanimelist;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alib.myanimelist.ui.AnimeList.AnimeListFragment;
import com.alib.myanimelist.ui.FavAnime.FavAnimeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;



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

        // Get the default ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the custom layout for the ActionBar
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_layout);

            // Retrieve the views and set their attributes
            TextView titleView = actionBar.getCustomView().findViewById(R.id.actionbar_title);
            titleView.setText(getTitle()); // Set the title to the current activity title


            ImageButton searchButton = actionBar.getCustomView().findViewById(R.id.actionbar_search);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Perform search action here
                }
            });
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
