package com.example.study;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    private Button createFlashcardButton;
    private Button viewFlashcardsButton;
    private TextView categoryCounter, flashcardCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createFlashcardButton = findViewById(R.id.create_flashcard_button);
        viewFlashcardsButton = findViewById(R.id.view_flashcards_button);
        Button starredFlashcardsButton = findViewById(R.id.starred_flashcards_button);
        starredFlashcardsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, StarredFlashcardsActivity.class);
            startActivity(intent);
        });

        createFlashcardButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                CreateFlashcardActivity.class)));
        viewFlashcardsButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this,
                FlashcardListActivity.class)));

        categoryCounter = findViewById(R.id.category_counter);
        flashcardCounter = findViewById(R.id.flashcard_counter);

        FlashcardDatabaseHelper dbHelper = new FlashcardDatabaseHelper(this);

        // Set initial counters
        updateCounters(dbHelper);

        // Listeners for navigation
        createFlashcardButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CreateFlashcardActivity.class));
            updateCounters(dbHelper); // Update after returning
        });

        viewFlashcardsButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, FlashcardListActivity.class));
            updateCounters(dbHelper); // Update after returning
        });

        starredFlashcardsButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StarredFlashcardsActivity.class));
        });
    }

    private void updateCounters(FlashcardDatabaseHelper dbHelper) {
        int categoryCount = dbHelper.getCategoryCount();
        int flashcardCount = dbHelper.getFlashcardCount();

        categoryCounter.setText("Categories: " + categoryCount);
        flashcardCounter.setText("Flashcards: " + flashcardCount);
    }
}
