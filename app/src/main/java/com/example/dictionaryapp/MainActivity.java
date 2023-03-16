package com.example.dictionaryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mWordEditText;
    private Button mSearchButton;
    String word;
    String audioURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWordEditText = findViewById(R.id.input);
        mSearchButton = findViewById(R.id.button);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                word = mWordEditText.getText().toString().trim();
                if (word.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a word", Toast.LENGTH_SHORT).show();
                } else if (!word.matches("[a-zA-Z]+")) {
                    Toast.makeText(MainActivity.this, "Please enter only alphabetical characters", Toast.LENGTH_SHORT).show();
                } else {
                    new FetchMeaningsTask().execute(word);
                }
            }
        });
    }

    private class FetchMeaningsTask extends AsyncTask<String, Void, ArrayList<Meaning>> {

        @Override
        protected ArrayList<Meaning> doInBackground(String... params) {
            String word = params[0];
            try {
                return fetchMeanings(word);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Meaning> meaningsList) {
            if (meaningsList == null || meaningsList.isEmpty()) {
                Toast.makeText(MainActivity.this, "Word not found in the dictionary", Toast.LENGTH_SHORT).show();
            } else {
                // Start the meanings activity and pass the meanings list as an extra
                Intent intent = new Intent(MainActivity.this, MeaningsActivity.class);
//                intent.putExtra("meaningsList", meaningsList);
                intent.putParcelableArrayListExtra("meaningsList", meaningsList);
                intent.putExtra("word", word);
                intent.putExtra("audioURL", audioURL);
                startActivity(intent);
            }
            audioURL = "";
        }
    }

    private ArrayList<Meaning> fetchMeanings(String word) throws IOException, JSONException {
        URL url = new URL("https://api.dictionaryapi.dev/api/v2/entries/en/" + word);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("API response code is not OK");
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            responseBuilder.append(line);
        }
        bufferedReader.close();

        String jsonResponse = responseBuilder.toString();

        JSONArray jsonArray = new JSONArray(jsonResponse);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray meaningsArray = jsonObject.getJSONArray("meanings");
        ArrayList<Meaning> meaningList = new ArrayList<>();

        if (jsonArray.length() == 0) {
            throw new JSONException("API response is empty");
        }

        JSONArray audioArray = jsonObject.getJSONArray("phonetics");

        for(int i=0; i<audioArray.length(); i++){
            audioURL = audioArray.getJSONObject(i).getString("audio");
            if(audioURL.length() == 0){
                continue;
            }else{
                break;
            }
        }


        for (int i = 0; i < meaningsArray.length(); i++) {
            JSONObject meaningObject = meaningsArray.getJSONObject(i);
            String partOfSpeech = meaningObject.getString("partOfSpeech");
            JSONArray definitionsArray = meaningObject.getJSONArray("definitions");

            List<String> definitionsList = new ArrayList<>();
            List<String> examplesList = new ArrayList<>();
            List<String> antonymsList = new ArrayList<>();
            List<String> synonymsList = new ArrayList<>();

            for (int j = 0; j < definitionsArray.length(); j++) {
                JSONObject definitionObject = definitionsArray.getJSONObject(j);
                String definition = definitionObject.getString("definition");
                definitionsList.add(definition);

                if (definitionObject.has("example")) {
                    String example = definitionObject.getString("example");
                    examplesList.add("eg. "+example);
                }else{
                    examplesList.add("no example found");
                }

                if (definitionObject.has("antonyms")) {
                    JSONArray antonymsArray = definitionObject.getJSONArray("antonyms");
                    String antonyms = antonymsArray.join(", ");
                    antonymsList.add(antonyms);
                }

                if (definitionObject.has("synonyms")) {
                    JSONArray synonymsArray = definitionObject.getJSONArray("synonyms");
                    String synonyms = synonymsArray.join(", ");
                    synonymsList.add(synonyms);
                }
            }

//
            JSONArray synonymsArray = meaningObject.optJSONArray("synonyms");
            if (synonymsArray != null) {
                for (int j = 0; j < synonymsArray.length(); j++) {
                    synonymsList.add(synonymsArray.getString(j));
                }
            }else{
                synonymsList = null;
            }

            JSONArray antonymsArray = meaningObject.optJSONArray("antonyms");
            if (antonymsArray != null) {
                for (int j = 0; j < antonymsArray.length(); j++) {
                    antonymsList.add(antonymsArray.getString(j));
                }
            }else{
                antonymsList = null;
            }

            Meaning meaning = new Meaning(partOfSpeech, definitionsList, synonymsList, antonymsList, examplesList);
            meaningList.add(meaning);
        }

        return meaningList;
    }

}
