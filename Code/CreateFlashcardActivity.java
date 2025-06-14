package com.example.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateFlashcardActivity extends AppCompatActivity {
    private EditText categoryInput, questionInput, answerInput;
    private Button saveCategoryButton, saveFlashcardButton;
    private FlashcardDatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flashcard);

        categoryInput = findViewById(R.id.category_input);
        questionInput = findViewById(R.id.question_input);
        answerInput = findViewById(R.id.answer_input);
        saveCategoryButton = findViewById(R.id.save_category_button);
        saveFlashcardButton = findViewById(R.id.save_flashcard_button);

        dbHelper = new FlashcardDatabaseHelper(this);

        saveCategoryButton.setOnClickListener(v -> {
            String category = categoryInput.getText().toString().trim();
            if (!TextUtils.isEmpty(category)) {
                dbHelper.addCategory(category);
                Toast.makeText(this, "Category saved!", Toast.LENGTH_SHORT).show();
                categoryInput.setText("");
            } else {
                Toast.makeText(this, "Category cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        saveFlashcardButton.setOnClickListener(v -> {
            String question = questionInput.getText().toString().trim();
            String answer = answerInput.getText().toString().trim();
            String category = categoryInput.getText().toString().trim();

            if (!TextUtils.isEmpty(question) && !TextUtils.isEmpty(answer) && !TextUtils.isEmpty(category)) {
                // Get the category ID
                int categoryId = dbHelper.getCategoryIdByName(category);
                if (categoryId != -1) {
                    dbHelper.addFlashcard(question, answer, categoryId);
                    Toast.makeText(this, "Flashcard saved!", Toast.LENGTH_SHORT).show();
                    questionInput.setText("");
                    answerInput.setText("");
                } else {
                    Toast.makeText(this, "Category not found. Please save the category first.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}