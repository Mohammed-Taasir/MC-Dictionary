package com.example.dictionaryapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Meaning implements Parcelable {
    private String partOfSpeech;
    private String definition;

    public Meaning(String partOfSpeech, String definition) {
        this.partOfSpeech = partOfSpeech;
        this.definition = definition;
    }

    protected Meaning(Parcel in) {
        partOfSpeech = in.readString();
        definition = in.readString();
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

    public String getDefinition() {
        return definition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(partOfSpeech);
        parcel.writeString(definition);
    }
}

