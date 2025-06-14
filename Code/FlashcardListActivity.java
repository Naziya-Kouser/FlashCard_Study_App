package com.example.study;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import android.content.Intent;
import java.util.stream.Collectors;
public class FlashcardListActivity extends AppCompatActivity {
    private ListView categoryListView, flashcardListView;
    private FlashcardDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_list);
        categoryListView = findViewById(R.id.category_list_view);
        flashcardListView = findViewById(R.id.flashcard_list_view);
        dbHelper = new FlashcardDatabaseHelper(this);
        loadCategories();
        View starredFlashcardsButton = findViewById(R.id.starred_flashcards_button);
        starredFlashcardsButton.setOnClickListener(v -> {
            Intent intent = new Intent(FlashcardListActivity.this, StarredFlashcardsActivity.class);
            startActivity(intent);
        });

        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            List<HashMap<String, String>> categories = (List<HashMap<String, String>>) categoryListView.getTag();
            int categoryId = Integer.parseInt(categories.get(position).get("id"));
            loadFlashcards(categoryId);
        });

        categoryListView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedCategory = (String) parent.getItemAtPosition(position);
            dbHelper.deleteCategory(selectedCategory);
            Toast.makeText(this, "Category deleted!", Toast.LENGTH_SHORT).show();
            loadCategories();
            return true;
        });
        flashcardListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected flashcard's question
            String selectedQuestion = (String) parent.getItemAtPosition(position);
            // Retrieve the answer for the selected question from the database
            String answer = dbHelper.getAnswerForQuestion(selectedQuestion);
            // Toggle between showing the question and its answer
            if (selectedQuestion.equals(view.getTag())) {
                // If the answer is already displayed, switch back to the question
                ((android.widget.TextView) view).setText(selectedQuestion);
                view.setTag(null);
            } else {
                // Show the answer and set it as the tag for the view
                ((android.widget.TextView) view).setText(answer);
                view.setTag(selectedQuestion);
            }
        });
    }
    private void loadCategories() {
        List<HashMap<String, String>> categories = dbHelper.getAllCategoriesWithIds();
        List<String> categoryNames = categories.stream()
                .map(category -> category.get("name"))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryNames);
        categoryListView.setAdapter(adapter);

        // Save category IDs for future use
        categoryListView.setTag(categories);
    }

    private void loadFlashcards(int categoryId) {
        List<HashMap<String, String>> flashcardsMap = dbHelper.getFlashcardsByCategory(categoryId);
        List<String> flashcards = flashcardsMap.stream()
                .map(card -> card.get("question"))
                .collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, flashcards);
        flashcardListView.setAdapter(adapter);
    }


}
