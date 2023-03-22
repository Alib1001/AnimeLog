package com.alib.myanimelist;

import android.os.Bundle;
import android.view.MenuItem;

import com.alib.myanimelist.ui.AnimeListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alib.myanimelist.databinding.ActivityMainBinding;

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
        favoriteAnimeFragment = new AnimeListFragment();

        fragmentManager.beginTransaction().add(R.id.container, favoriteAnimeFragment, "2").hide(favoriteAnimeFragment).commit();
        fragmentManager.beginTransaction().add(R.id.container, animeListFragment, "1").commit();
        activeFragment = animeListFragment;
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
