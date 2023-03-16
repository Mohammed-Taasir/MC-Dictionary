package com.example.dictionaryapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class Meaning implements Parcelable {
    private String partOfSpeech;
    private List<String> definitions;
    private List<String> synonyms;
    private List<String> antonyms;
    private List<String> examples;

    public Meaning(String partOfSpeech, List<String> definitions, List<String> synonyms, List<String> antonyms, List<String> examples) {
        this.partOfSpeech = partOfSpeech;
        this.definitions = definitions;
        this.synonyms = synonyms;
        this.antonyms = antonyms;
        this.examples = examples;
    }

    protected Meaning(Parcel in) {
        partOfSpeech = in.readString();
        definitions = in.createStringArrayList();
        synonyms = in.createStringArrayList();
        antonyms = in.createStringArrayList();
        examples = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(partOfSpeech);
        dest.writeStringList(definitions);
        dest.writeStringList(synonyms);
        dest.writeStringList(antonyms);
        dest.writeStringList(examples);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Meaning> CREATOR = new Creator<Meaning>() {
        @Override
        public Meaning createFromParcel(Parcel in) {
            return new Meaning(in);
        }

        @Override
        public Meaning[] newArray(int size) {
            return new Meaning[size];
        }
    };

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public List<String> getDefinitions() {
        return definitions;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public List<String> getExamples() {
        return examples;
    }
}


