package com.example.dictionaryapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class DefinitionFragment extends Fragment {

    private TextView definitionTextView;
    private TextView exampleTextView;
    private TextView partOfSpeechTextView;
    private TextView antonymTextView;
    private TextView synonymTextView;

    public DefinitionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_definition, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the views
        definitionTextView = view.findViewById(R.id.definition_textview);
        exampleTextView = view.findViewById(R.id.example_textview);
        partOfSpeechTextView = view.findViewById(R.id.part_of_speech_textview);
        antonymTextView = view.findViewById(R.id.antonyms_textview);
        synonymTextView = view.findViewById(R.id.synonyms_textview);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Retrieve the Meaning object
        assert getArguments() != null;
        Meaning meaning = getArguments().getParcelable("selectedMeaning");

        // Display the definition, example, part of speech, antonym, and synonym (if present)
        List<String> definitions = meaning.getDefinitions();
        StringBuilder definitionString = new StringBuilder();
        for (String definition : definitions) {
            definitionString.append(definition).append("\n");
        }
        definitionTextView.setText(definitionString.toString());
//        definitionTextView.setText((CharSequence) meaning.getDefinitions());
        List<String> examples = meaning.getExamples();
        StringBuilder exampleString = new StringBuilder();
        for (String example : examples) {
            exampleString.append(example).append("\n");
        }
        exampleTextView.setText(exampleString.toString());
        Log.i("examp", String.valueOf(exampleString));


        partOfSpeechTextView.setText(meaning.getPartOfSpeech());


        List<String> antonyms = meaning.getAntonyms();
        StringBuilder antonymString = new StringBuilder();
        if(antonyms.isEmpty()){
            antonymTextView.setText("No antonym found");
        }else{
            for (String antonym : antonyms) {
                antonymString.append(antonym).append("\n");
            }
        }
        antonymTextView.setText(antonymString.toString());


//        antonymTextView.setText(meaning.getAntonyms().isEmpty() ? "" : meaning.getAntonyms().get(0));
        List<String> synonyms = meaning.getSynonyms();
        StringBuilder synonymString = new StringBuilder();
        if(synonyms.isEmpty()){
            synonymTextView.setText("No synonyms found");
        }else{
            for (String synonym : synonyms) {
                synonymString.append(synonym).append("\n");
            }
        }
        synonymTextView.setText(synonymString.toString());
//        synonymTextView.setText(meaning.getSynonyms().isEmpty() ? "" : meaning.getSynonyms().get(0));
    }
}