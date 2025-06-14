package com.example.study;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.HashMap;

public class FlashcardAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, String>> flashcards;
    private FlashcardDatabaseHelper dbHelper;

    public FlashcardAdapter(Context context, List<HashMap<String, String>> flashcards, FlashcardDatabaseHelper dbHelper) {
        this.context = context;
        this.flashcards = flashcards;
        this.dbHelper = dbHelper;
    }

    @Override
    public int getCount() {
        return flashcards.size();
    }

    @Override
    public Object getItem(int position) {
        return flashcards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_flashcard, parent, false);
        }

        TextView questionText = convertView.findViewById(R.id.question_input);
        ImageButton starButton = convertView.findViewById(R.id.star_button);

        HashMap<String, String> flashcard = flashcards.get(position);
        questionText.setText(flashcard.get("question"));

        // Update star icon based on is_starred
        boolean isStarred = flashcard.get("is_starred").equals("1");
        starButton.setImageResource(isStarred ? R.drawable.ic_star_border : R.drawable.ic_star_border);

        // Toggle star on click
        starButton.setOnClickListener(v -> {
            int flashcardId = Integer.parseInt(flashcard.get("id"));
            dbHelper.toggleStarFlashcard(flashcardId, !isStarred);
            flashcard.put("is_starred", isStarred ? "0" : "1");
            notifyDataSetChanged();
        });

        return convertView;
    }
}

