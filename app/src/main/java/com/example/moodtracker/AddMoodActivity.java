package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class AddMoodActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db;
    private String moodpath;
    EditText eventNameEditText, eventDateEditText, eventTimeEditText;
    Button optionButton, confirmButton;
    Spinner selectMood, selectSocialSituation;
    String selectedMood;
    int selectedSocialSituation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mood);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        moodpath = intent.getStringExtra(MainActivity.EXTRA_USERPATH) + intent.getStringExtra(MainActivity.EXTRA_USER) + "/" + "Moods/";

        eventNameEditText = findViewById(R.id.event_name_edit_text);
        eventDateEditText = findViewById(R.id.event_date_edit_text);
        eventTimeEditText = findViewById(R.id.event_time_edit_text);
        optionButton = findViewById(R.id.option_button);
        confirmButton = findViewById(R.id.add_comfirm_button);

        selectMood = findViewById(R.id.mood_type_select);
        ArrayAdapter<CharSequence> moodAdapter = ArrayAdapter.createFromResource(this,
                R.array.emotions_array, android.R.layout.simple_spinner_item);
        moodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMood.setAdapter(moodAdapter);
        selectMood.setOnItemSelectedListener(this);

        selectSocialSituation = findViewById(R.id.mood_social_situation_select);
        selectedSocialSituation = -1;
        ArrayAdapter<CharSequence> situationAdapter = ArrayAdapter.createFromResource(this,
                R.array.situations_array, android.R.layout.simple_spinner_item);
        situationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSocialSituation.setAdapter(situationAdapter);
        selectSocialSituation.setOnItemSelectedListener(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO:Add new event and update the database
                if(validate()){
                    String moodName = eventNameEditText.getText().toString().trim();
                    String dateStr = eventDateEditText.getText().toString().trim();
                    String timeStr = eventTimeEditText.getText().toString().trim();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(MoodEvent.longFormat.parse(dateStr + " " + timeStr));
                    MoodEvent mood = new MoodEvent(moodName, selectedSocialSituation, , selectedMood);
                    //Intent intent = new Intent(AddMoodActivity.this, MoodHistory.class);
                    //startActivity(intent);
                }

            }
        });

        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AddMoodActivity.this, OptionActivity.class);
                startActivity(intent);
            }
        });
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
                        Toast.makeText(AddMoodActivity.this, "Data addition successful.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data addition failed " + e.toString());
                        Toast.makeText(AddMoodActivity.this, "Data addition failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String s = (String) parent.getItemAtPosition(pos);
        // if mood was unselected, selectedMood will be null
        // if situation was unselected, selectedSocialSituation will be -1
        for(int i = 0; i < EmotionData.EmotionStrings.length; i++){
            if(s.equals(EmotionData.EmotionStrings[i])){
                selectedMood = s;
                return;
            }
        }
        selectedSocialSituation = MoodEvent.situationToInt(s);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private boolean validate(){
        String moodName = eventNameEditText.getText().toString().trim();
        String dateStr = eventDateEditText.getText().toString().trim();
        String timeStr = eventTimeEditText.getText().toString().trim();
        boolean valid = true;

        if(moodName.isEmpty()){
            valid = false;
            Toast.makeText(AddMoodActivity.this, "Mood name cannot be empty.", Toast.LENGTH_SHORT).show();
        }

        if(dateStr.isEmpty()){
            valid = false;
            Toast.makeText(AddMoodActivity.this, "Date and time cannot be empty.", Toast.LENGTH_SHORT).show();
        }

        if(timeStr.isEmpty()){
            valid = false;
            Toast.makeText(AddMoodActivity.this, "Date and time cannot be empty.", Toast.LENGTH_SHORT).show();
        }

        if(selectedSocialSituation == -1){
            valid = false;
            Toast.makeText(AddMoodActivity.this, "You have to pick a situation.", Toast.LENGTH_SHORT).show();
        }

        if(selectedMood == null){
            valid = false;
            Toast.makeText(AddMoodActivity.this, "You have to pick a mood.", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }

}
