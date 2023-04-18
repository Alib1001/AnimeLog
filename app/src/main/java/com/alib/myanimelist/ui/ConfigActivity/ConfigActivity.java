package com.alib.myanimelist.ui.ConfigActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;

import net.sandrohc.jikan.Jikan;
import net.sandrohc.jikan.exception.JikanQueryException;
import net.sandrohc.jikan.model.anime.Anime;

import org.w3c.dom.Text;

import io.netty.resolver.DefaultAddressResolverGroup;

public class ConfigActivity extends AppCompatActivity {

    Jikan jikan = new Jikan.JikanBuilder()
            .httpClientCustomizer(httpClient -> httpClient.resolver(DefaultAddressResolverGroup.INSTANCE))
            .build();

    Anime anime;

    String selectedStatus;
    int score;
    int watchedEps;
    String notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        TextView animeName = findViewById(R.id.anime_name);
        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        Spinner scoreSpinner = findViewById(R.id.scoreSpinner);
        EditText watchedSeriesEd = findViewById(R.id.seriesEd);
        EditText notesEd = findViewById(R.id.notesEd);
        Button saveBtn = findViewById(R.id.saveBtn);

        Context context = getApplicationContext();
        AnimeDatabaseHelper dbHelper = new AnimeDatabaseHelper(context);

        Intent intent = getIntent();
        int malId = intent.getIntExtra("malId", 1);

        try {
            anime = jikan.query().anime().get(malId)
                    .execute()
                    .block();
        } catch (JikanQueryException e) {
            throw new RuntimeException(e);
        }

        animeName.setText(anime.getTitle());

        String[] statusOptions = {"Watching", "Completed", "On-Hold", "Dropped", "Plan to watch"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, statusOptions);
        statusSpinner.setAdapter(statusAdapter);

        String[] scoreOptions = {"(1)Appalling", "(2)Horrible", "(3)Very Bad", "(4)Bad",
                "(5)Average", "(6)Fine", "(7)Good", "(8)Very Good", "(9)Great", "(10)Masterpiece"};
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, scoreOptions);
        scoreSpinner.setAdapter(scoreAdapter);

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        scoreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedScore = parent.getItemAtPosition(position).toString();
                String numericString = selectedScore.replaceAll("[^\\d]", "");
                score = Integer.parseInt(numericString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchedEps = Integer.parseInt(watchedSeriesEd.getText().toString());
                notes = notesEd.getText().toString();
                dbHelper.updateAnimeConfig(anime,watchedEps,score,selectedStatus,notes);
                dbHelper.exportDataToTxt();
            }
        });

    }
}
