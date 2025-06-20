package com.example.personalizedlearningapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.personalizedlearningapp.model.Question;
import com.example.personalizedlearningapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "learning_app.db";
    public static final int DB_VERSION = 4;

    public static final String TABLE_USERS = "users";
    public static final String TABLE_INTERESTS = "interests";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUsersTable = "CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "phone TEXT, " +
                "avatar_uri TEXT)";

        // Create interests table
        String createInterestsTable = "CREATE TABLE interests (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "interest TEXT)";

        // Create questions table
        String createQuestionsTable = "CREATE TABLE questions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "task_title TEXT, " +
                "question_text TEXT, " +
                "correct_answer TEXT, " +
                "selected_option TEXT, " +
                "option_a TEXT, " +
                "option_b TEXT, " +
                "option_c TEXT, " +
                "option_d TEXT, " +
                "timestamp TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createInterestsTable);
        db.execSQL(createQuestionsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop and recreate tables on upgrade
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS interests");
        db.execSQL("DROP TABLE IF EXISTS questions");
        onCreate(db);
    }

    // Insert user registration data
    public boolean registerUser(String username, String email, String password, String phone, String avatarUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("email", email);
        values.put("password", password);
        values.put("phone", phone);
        values.put("avatar_uri", avatarUri);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    // Get user ID by username
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM users WHERE username=?", new String[]{username});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    // Fetch user details by user ID
    public User getUserById(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE id=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            User user = new User();
            user.id = userId;
            user.username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            user.email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            user.avatarUri = cursor.getString(cursor.getColumnIndexOrThrow("avatar_uri"));
            return user;
        }
        cursor.close();
        return null;
    }

    // Validate user login
    public Cursor loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE username=? AND password=?", new String[]{username, password});
    }

    // Remove all users and their interests
    public void clearAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.delete(TABLE_INTERESTS, null, null);
    }

    // Get interests for a specific user
    public List<String> getInterestsForUser(int userId) {
        List<String> interests = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT interest FROM interests WHERE user_id=?", new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            interests.add(cursor.getString(0));
        }
        cursor.close();
        return interests;
    }

    // Save quiz question attempt without timestamp
    public void saveQuestionResult(int userId, String taskTitle, String questionText, String correctAnswer, String selectedOption,
                                   String optionA, String optionB, String optionC, String optionD) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("task_title", taskTitle);
        values.put("question_text", questionText);
        values.put("correct_answer", correctAnswer);
        values.put("selected_option", selectedOption);
        values.put("option_a", optionA);
        values.put("option_b", optionB);
        values.put("option_c", optionC);
        values.put("option_d", optionD);
        db.insert("questions", null, values);
    }

    // Check if user has completed a task
    public boolean isTaskCompleted(int userId, String taskTitle) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM questions WHERE user_id=? AND task_title=?", new String[]{String.valueOf(userId), taskTitle});
        boolean completed = false;
        if (cursor.moveToFirst()) {
            completed = cursor.getInt(0) > 0;
        }
        cursor.close();
        return completed;
    }

    // Get all quiz results by a user (used for profile statistics)
    public List<String[]> getAllQuestionsByUser(int userId) {
        List<String[]> results = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT correct_answer, selected_option FROM questions WHERE user_id=?", new String[]{String.valueOf(userId)});
        while (cursor.moveToNext()) {
            String correct = cursor.getString(0);
            String selected = cursor.getString(1);
            results.add(new String[]{correct, selected});
        }
        cursor.close();
        return results;
    }

    // Update user's avatar path
    public void updateAvatarUri(int userId, String avatarUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("avatar_uri", avatarUri);
        db.update("users", values, "id=?", new String[]{String.valueOf(userId)});
    }

    // Retrieve all quiz questions answered for a task
    public List<Question> getQuestionsForTask(int userId, String taskTitle) {
        List<Question> questions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT question_text, correct_answer, selected_option, option_a, option_b, option_c, option_d, timestamp " +
                        "FROM questions WHERE user_id=? AND task_title=?",
                new String[]{String.valueOf(userId), taskTitle}
        );

        while (cursor.moveToNext()) {
            String questionText = cursor.getString(0);
            String correct = cursor.getString(1);
            String selected = cursor.getString(2);
            String optionA = cursor.getString(3);
            String optionB = cursor.getString(4);
            String optionC = cursor.getString(5);
            String optionD = cursor.getString(6);
            String timestamp = cursor.getString(7);

            String[] opts = new String[]{optionA, optionB, optionC, optionD};
            Question q = new Question(questionText, opts);
            q.correctAnswer = correct;
            q.timestamp = timestamp;
            q.taskTitle = taskTitle;

            // Determine which option was selected
            if (selected != null && !selected.isEmpty()) {
                for (int i = 0; i < q.options.length; i++) {
                    if (q.options[i].equals(selected)) {
                        q.selectedOption = i;
                        break;
                    }
                }
            }
            questions.add(q);
        }
        cursor.close();
        return questions;
    }

    // Save quiz question with timestamp
    public void saveQuestionResult(int userId, String taskTitle, String questionText, String correctAnswer,
                                   String selectedOption, String optionA, String optionB, String optionC, String optionD,
                                   String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("task_title", taskTitle);
        values.put("question_text", questionText);
        values.put("correct_answer", correctAnswer);
        values.put("selected_option", selectedOption);
        values.put("option_a", optionA);
        values.put("option_b", optionB);
        values.put("option_c", optionC);
        values.put("option_d", optionD);
        values.put("timestamp", timestamp);
        db.insert("questions", null, values);
    }
}
