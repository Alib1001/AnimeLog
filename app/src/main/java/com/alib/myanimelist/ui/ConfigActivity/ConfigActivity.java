package com.alib.myanimelist.ui.ConfigActivity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;

import com.alib.myanimelist.R;

public class ConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Spinner statusSpinner = findViewById(R.id.statusSpinner);
        Spinner scoreSpinner = findViewById(R.id.scoreSpinner);

        String[] statusOptions = {"Watching", "Completed", "On-Hold","Dropped","Plan to watch"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, statusOptions);
        statusSpinner.setAdapter(statusAdapter);



        String[] scoreOptions = {"(1)Appalling", "(2)Horrible", "(3)Very Bad", "(4)Bad",
                "(5)Average", "(6)Fine","(7)Good","(8)Very Good","(9)Great","(10)Masterpiece"};
        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, scoreOptions);
        scoreSpinner.setAdapter(scoreAdapter);


        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedStatus = parent.getItemAtPosition(position).toString();
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
                int score = Integer.parseInt(numericString);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}