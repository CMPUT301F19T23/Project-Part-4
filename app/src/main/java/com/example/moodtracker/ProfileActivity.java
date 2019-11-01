package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    TextView username;
    Button myMoodHistoryButton, friendMoodHistoryButton;
    Button logoutButton, editPictureButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = findViewById(R.id.profile_username);
        myMoodHistoryButton = findViewById(R.id.my_mood_history_button);
        friendMoodHistoryButton = findViewById(R.id.firends_mood_history_button);
        logoutButton = findViewById(R.id.log_out_button);
        editPictureButton = findViewById(R.id.edit_profile_picture);

        /**
         * Get User name from input
         */
        Intent intent= getIntent();
        String profileName = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        username.setText(profileName);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
