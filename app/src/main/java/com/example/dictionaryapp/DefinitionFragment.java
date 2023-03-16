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
    private TextView synText;
    private TextView antText;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the views
        definitionTextView = view.findViewById(R.id.definition_textview);
//        exampleTextView = view.findViewById(R.id.example_textview);
        partOfSpeechTextView = view.findViewById(R.id.part_of_speech_textview);
        antonymTextView = view.findViewById(R.id.antonyms_textview);
        synonymTextView = view.findViewById(R.id.synonyms_textview);
        synText = view.findViewById(R.id.syn);
        antText = view.findViewById(R.id.ant);



        // Retrieve the Meaning object
        assert getArguments() != null;
        Meaning meaning = getArguments().getParcelable("selectedMeaning");

        // Display the definition, example, part of speech, antonym, and synonym (if present)
        List<String> definitions = meaning.getDefinitions();
        List<String> examples = meaning.getExamples();
        int defctr = 1;
        StringBuilder definitionString = new StringBuilder();

        for(int i=0; i<definitions.size(); i++){
            String definition = definitions.get(i);
            String example = examples.get(i);

            definitionString.append("\n");
            definitionString.append(defctr+". ");
            definitionString.append(definition).append("\n\n");
            definitionString.append("  -> "+example).append("\n\n");
            definitionString.append("-------------------------------------------------------------------------------------\n");
            defctr++;
        }
        definitionTextView.setText(definitionString.toString());

        partOfSpeechTextView.setText(meaning.getPartOfSpeech());


        List<String> antonyms = meaning.getAntonyms();
        StringBuilder antonymString = new StringBuilder();
        if(antonyms == null || antonyms.isEmpty()){
            antonymTextView.setText("no antonyms found");
            antText.setText("");
        }else{
            for (String antonym : antonyms) {
                antonymString.append(antonym).append("      ");
            }
            antonymTextView.setText(antonymString.toString().trim());
        }

        List<String> synonyms = meaning.getSynonyms();
        StringBuilder synonymString = new StringBuilder();
        if(synonyms == null || synonyms.isEmpty()){
            synonymTextView.setText("no synonyms found");
            synText.setText("");
        }else{
            for (String synonym : synonyms) {
                synonymString.append(synonym).append("      ");
            }
            synonymTextView.setText(synonymString.toString().trim());
        }
    }

}