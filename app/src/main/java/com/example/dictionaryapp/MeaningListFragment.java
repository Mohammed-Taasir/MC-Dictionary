package com.example.dictionaryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MeaningListFragment extends ListFragment {

//    private ArrayList<Meaning> meaningList;
    private OnItemClickListener listener;
    ArrayList<Meaning> meaningsList;

    public MeaningListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        // Get the list of meanings passed from the main activity
//        meaningList = (List<Meaning>) getIntent().getSerializableExtra("meaningsList");

        // Get the list of meanings passed from the main activity
//        meaningList = getArguments().getParcelableArrayList("meaningsList");
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            ArrayList<Meaning> meaningsList = (ArrayList<Meaning>) bundle.getParcelableArrayList("meaningsList");
//        }
        Bundle bundle = getArguments();

        if (bundle != null) {
            // get the meanings list from the arguments
            meaningsList = bundle.getParcelableArrayList("meaningsList");
            // use the meanings list as needed
        }

//        if (bundle != null) {
//            meaningsList = new ArrayList<>();
//            ArrayList<Parcelable> parcelableList = bundle.getParcelableArrayList("meaningsList");
//            for (Parcelable p : parcelableList) {
//                if (p instanceof Meaning) {
//                    meaningsList.add((Meaning) p);
//                }
//            }
//        }
//        ArrayList<Meaning> meaningsList = bundle.getParcelableArrayList("meaningsList");
//        Parcelable[] parcelables = bundle.getParcelableArrayList("meaningsList");
//        ArrayList<Meaning> meaningList = new ArrayList<>();
//        if (parcelables != null) {
//            for (Parcelable parcelable : parcelables) {
//                meaningList.add((Meaning) parcelable);
//            }
//        }
        // Set up the adapter for the list view
        setListAdapter(new MeaningAdapter(this, meaningsList, listener));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Adapter for the list view
    private static class MeaningAdapter extends ArrayAdapter<Meaning> {
        private List<Meaning> meaningList;
        private OnItemClickListener listener;
        private Fragment parentFragment;

        public MeaningAdapter(Fragment parentFragment, List<Meaning> meaningList, OnItemClickListener listener) {
            super(parentFragment.getActivity(), R.layout.item_meaning, meaningList);
            this.meaningList = meaningList;
            this.listener = listener;
            this.parentFragment = parentFragment;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_meaning, parent, false);
            }

            final Meaning meaning = getItem(position);

            // Set the part of speech
            TextView partOfSpeechTextView = convertView.findViewById(R.id.part_of_speech_text_view);
            partOfSpeechTextView.setText(meaning.getPartOfSpeech());

            // Set the definition
            TextView definitionTextView = convertView.findViewById(R.id.definition_text_view);
            definitionTextView.setText(meaning.getDefinition());

            // Set the click listener
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(meaning);
                }
            });

            return convertView;
        }
    }

    // Interface for click listener
    public interface OnItemClickListener {
        void onItemClick(Meaning meaning);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_meaning_list, container, false);
//    }
}