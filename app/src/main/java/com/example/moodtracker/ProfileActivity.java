package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    TextView username;
    Button myMoodHistoryButton, friendMoodHistoryButton;
    Button logoutButton, editPictureButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        username = findViewById(R.id.profile_username);
        myMoodHistoryButton = findViewById(R.id.my_mood_history_button);
        friendMoodHistoryButton = findViewById(R.id.firends_mood_history_button);
        logoutButton = findViewById(R.id.log_out_button);
        editPictureButton = findViewById(R.id.edit_profile_picture);

        // Get the User object from  MainActivity
        if (getIntent().hasExtra("UserProfile")){
            User user = getIntent().getParcelableExtra("UserProfile");
            username.setText(user.getUsername());
            Toast.makeText(ProfileActivity.this, "email " + user.getEmail(),
                    Toast.LENGTH_SHORT).show();
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
