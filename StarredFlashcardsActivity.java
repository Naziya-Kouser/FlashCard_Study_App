package com.example.study;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class StarredFlashcardsActivity extends AppCompatActivity {
    private ListView starredFlashcardsListView;
    private FlashcardDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starred_flashcards);

        ListView starredListView = findViewById(R.id.starred_flashcard_list_view);
        FlashcardDatabaseHelper dbHelper = new FlashcardDatabaseHelper(this);

        List<HashMap<String, String>> starredFlashcards = dbHelper.getStarredFlashcards();
        List<String> flashcardQuestions = starredFlashcards.stream()
                .map(card -> card.get("question"))
                .collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flashcardQuestions);
        starredListView.setAdapter(adapter);
    }


    private void loadStarredFlashcards() {
        List<HashMap<String, String>> starredFlashcardsMap = dbHelper.getStarredFlashcards();
        List<String> starredFlashcards = starredFlashcardsMap.stream()
                .map(card -> card.get("question"))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, starredFlashcards);
        starredFlashcardsListView.setAdapter(adapter);
    }
}

