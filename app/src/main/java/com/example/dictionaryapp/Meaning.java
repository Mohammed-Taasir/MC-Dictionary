package com.example.dictionaryapp;

import java.io.Serializable;

public class Meaning implements Serializable {
    private String partOfSpeech;
    private String definition;

    public Meaning(String partOfSpeech, String definition) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public String getDefinition() {
        return definition;
    }
}

