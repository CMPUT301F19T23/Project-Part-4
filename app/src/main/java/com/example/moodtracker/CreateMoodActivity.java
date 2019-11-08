package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class CreateMoodActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String moodpath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        moodpath = intent.getStringExtra(MainActivity.EXTRA_USERPATH) + intent.getStringExtra("CurrentUsername") + "/" + "Moods/";
    }

    private void writeMoodToDB(MoodEvent mood){
        HashMap<String, String> moodData = new HashMap<>();
        moodData.put("mood_name", mood.getName());
        moodData.put("mood_date", mood.getDay() + " " + mood.getTime());
        moodData.put("mood_situation", String.valueOf(mood.getSituation()));
        moodData.put("mood_reason_str", mood.getReasonString());
        moodData.put("mood_emotion", mood.getEmotion());

        db.document(moodpath + mood.getName())
                .set(moodData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data addition successful");
                        Toast.makeText(CreateMoodActivity.this, "Data addition successful.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data addition failed " + e.toString());
                        Toast.makeText(CreateMoodActivity.this, "Data addition failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
