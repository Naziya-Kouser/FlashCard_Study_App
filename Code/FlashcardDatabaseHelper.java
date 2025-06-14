package com.example.study;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class FlashcardDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "flashcards.db";
    private static final int DATABASE_VERSION = 2;
    // Table and column names
    private static final String TABLE_CATEGORY = "categories";
    private static final String TABLE_FLASHCARD = "flashcards";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "name";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER = "answer";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_STARRED = "starred";
    public FlashcardDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_FLASHCARD + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_ANSWER + " TEXT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                COLUMN_STARRED + " INTEGER DEFAULT 0, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" +
                COLUMN_ID + ") ON DELETE CASCADE)");
    }

    public void starFlashcard(int id, boolean isStarred) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STARRED, isStarred ? 1 : 0);
        db.update(TABLE_FLASHCARD, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    @SuppressLint("Range")
    public List<HashMap<String, String>> getStarredFlashcards() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM flashcards WHERE is_starred = 1", null);

        List<HashMap<String, String>> flashcards = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> card = new HashMap<>();
            card.put("id", cursor.getString(cursor.getColumnIndex("id")));
            card.put("question", cursor.getString(cursor.getColumnIndex("question")));
            card.put("answer", cursor.getString(cursor.getColumnIndex("answer")));
            flashcards.add(card);
        }
        cursor.close();
        return flashcards;
    }

    public void toggleStarFlashcard(int flashcardId, boolean isStarred) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("is_starred", isStarred ? 1 : 0);
        db.update("flashcards", values, "id = ?", new String[]{String.valueOf(flashcardId)});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) { // Increment schema version as needed
            db.execSQL("ALTER TABLE flashcards ADD COLUMN is_starred INTEGER DEFAULT 0");
        }
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FLASHCARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        onCreate(db);
    }

    public String getAnswerForQuestion(String question) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ANSWER + " FROM " + TABLE_FLASHCARD +
                " WHERE " + COLUMN_QUESTION + "=?", new String[]{question});
        String answer = null;
        if (cursor.moveToFirst()) {
            answer = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER));
        }
        cursor.close();
        return answer;
    }
    // CRUD operations for categories
    public long addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        return db.insert(TABLE_CATEGORY, null, values);
    }
    public List<HashMap<String, String>> getAllCategoriesWithIds() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + ", " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_CATEGORY, null);
        List<HashMap<String, String>> categories = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> category = new HashMap<>();
            category.put("id", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            category.put("name", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)));
            categories.add(category);
        }
        cursor.close();
        return categories;
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_CATEGORY + " WHERE " + COLUMN_CATEGORY_NAME + "=?", new String[]{categoryName});
        int categoryId = -1;
        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        }
        cursor.close();
        return categoryId;
    }


    public void deleteCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Get the category ID
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_CATEGORY +
                " WHERE " + COLUMN_CATEGORY_NAME + "=?", new String[]{name});
        if (cursor.moveToFirst()) {
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
            // Delete flashcards linked to this category
            db.delete(TABLE_FLASHCARD, COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        }
        cursor.close();

        // Delete the category itself
        db.delete(TABLE_CATEGORY, COLUMN_CATEGORY_NAME + "=?", new String[]{name});
    }

    // CRUD operations for flashcards
    public long addFlashcard(String question, String answer, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUESTION, question);
        values.put(COLUMN_ANSWER, answer);
        values.put(COLUMN_CATEGORY_ID, categoryId);
        return db.insert(TABLE_FLASHCARD, null, values);
    }
    public List<HashMap<String, String>> getFlashcardsByCategory(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FLASHCARD + " WHERE " +
                COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        List<HashMap<String, String>> flashcards = new ArrayList<>();
        while (cursor.moveToNext()) {
            HashMap<String, String> card = new HashMap<>();
            card.put("question", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_QUESTION)));
            card.put("answer", cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ANSWER)));
            flashcards.add(card);
        }
        cursor.close();
        return flashcards;
    }
    public void deleteFlashcard(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FLASHCARD, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int getCategoryCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORY, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getFlashcardCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FLASHCARD, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

}
