package com.alib.myanimelist.ui.Backup;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alib.myanimelist.Database.AnimeDatabaseHelper;
import com.alib.myanimelist.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class BackupFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 100;

    private String mParam1;
    private String mParam2;

    public BackupFragment() {
    }

    public static BackupFragment newInstance(String param1, String param2) {
        BackupFragment fragment = new BackupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_backup, container, false);

        AnimeDatabaseHelper dbHelper = new AnimeDatabaseHelper(getContext());

        Button importBtn = rootView.findViewById(R.id.importBtn);
        Button exportBtn = rootView.findViewById(R.id.exportBtn);

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.exportDataToJSON();
                dbHelper.exportDataToTxt();
                Toast.makeText(getContext(), "Data exported", Toast.LENGTH_SHORT).show();

            }
        });

        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*"); // Set the type of files to be selected, you can customize this as needed
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                startActivityForResult(Intent.createChooser(intent, "Select File"), REQUEST_CODE);

            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Uri sourceUri = data.getData();

            ContentResolver contentResolver = getContext().getContentResolver();

            try {
                InputStream inputStream = contentResolver.openInputStream(sourceUri);
                File destinationDirectory = new File(getContext().getFilesDir(), "anime_data_json"); // Specify the destination directory
                destinationDirectory.mkdirs(); // Create the directory if it doesn't exist
                File destinationFile = new File(destinationDirectory, "anime_data.json"); // Specify the destination file
                OutputStream outputStream = new FileOutputStream(destinationFile);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();

                // File has been successfully imported, update the database
                Toast.makeText(getContext(),destinationFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to import data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}