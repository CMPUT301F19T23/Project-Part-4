package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Declare the variable.
    EditText username, password;
    Button signUpButton;
    Button signInButton;
    String name, pwd;

    public static final String EXTRA_TEXT = "com.example.moodtracker.EXTRA_TEXT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.username_sign_in_edit_text);
        password = findViewById(R.id.password_sign_in_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        signUpButton = findViewById(R.id.go_to_sign_up_button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Check if the username and password ( in the database ) are correct
                 * if correct, sent Username to Profile
                 */
                sendNametoProfile();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginReg.class);
                startActivity(intent);
            }
        });
    }


    private void sendNametoProfile(){
        name = username.getText().toString();
        pwd = password.getText().toString();
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        intent.putExtra(EXTRA_TEXT, name);
        startActivity(intent);
    }


}
