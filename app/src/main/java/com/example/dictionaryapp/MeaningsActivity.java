package com.example.dictionaryapp;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeaningsActivity extends AppCompatActivity {
    private ArrayList<Meaning> meaningList;
    private TextView wordTextView;
    private Button audioButton;
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meanings);

        // Get the list of meanings passed from the main activity
        meaningList = getIntent().getParcelableArrayListExtra("meaningsList");

        // Set up the views
        wordTextView = findViewById(R.id.word_textview);
        audioButton = findViewById(R.id.audio_button);


        // Set the word in the heading
        String word = getIntent().getStringExtra("word");
        wordTextView.setText(word);

        String audioUrl = getIntent().getStringExtra("audioURL");

        // Set up the audio button
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download and play the audio
                // Implement audio playback
                // String audioUrl = meaningList.get(0).getJSONArray("phonetics").get(0).getAudioUrl(); // get the audio url from the first meaning object
                new DownloadAudioTask().execute(audioUrl); // start downloading the audio in the background
            }
        });

        // Set up the fragment for the meanings list
        MeaningListFragment fragment = new MeaningListFragment();
        Bundle arguments = new Bundle();
        Log.i("taging", String.valueOf(meaningList));

        arguments.putParcelableArrayList("meaningsList", meaningList);

        fragment.setArguments(arguments);
        fragment.setOnItemClickListener(new MeaningListFragment.OnItemClickListener() {
            @Override
            public void onItemClick(Meaning meaning) {
                // Navigate to the definition fragment for the selected meaning
                // Implement navigation to definition fragment
                DefinitionFragment definitionFragment = new DefinitionFragment();
                Bundle arguments = new Bundle();
                arguments.putParcelable("selectedMeaning", meaning);
                definitionFragment.setArguments(arguments);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.meanings_container, definitionFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.meanings_container, fragment);
        transaction.commit();

    } // -----------------> END OF onCreate().

    // AsyncTask to download audio in the background
    private class DownloadAudioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String audioUrl = urls[0];
            String audioFileName = audioUrl.substring(audioUrl.lastIndexOf('/') + 1); // get the filename from the audio url
            String audioFilePath = getApplicationContext().getFilesDir().getPath() + "/" + audioFileName; // create the file path to save the audio file
            try {
                URL url = new URL(audioUrl);
                URLConnection connection = url.openConnection();
                connection.connect();
                int fileLength = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(audioFilePath);
                byte[] data = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();
                return audioFilePath;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String audioFilePath) {
            if (audioFilePath != null) {
                // play the downloaded audio
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(audioFilePath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error downloading audio", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

