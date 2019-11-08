package com.example.moodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    // Declare the variable.
    EditText passwordField;
    EditText emailField; // remove this if you don't make the signup page a fragment
    Button signUpButton;
    Button signInButton;
    FirebaseAuth auth;
    FirebaseFirestore db;
    FirebaseAuth.AuthStateListener authStateListener;


    public static final String EXTRA_USERPATH = "com.example.moodtracker.MESSAGE"; // Filepath to get to the Users database
    private String userPathStr = "Users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.email_sign_in_edit_text);
        passwordField = findViewById(R.id.password_sign_in_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        signUpButton = findViewById(R.id.go_to_sign_up_button);

        //go to loginReg activity and create user at there
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                // TODO: need to add a field for email, and maybe a field to confirm password
                if (validate(email, password)){
                    Intent intent = new Intent(MainActivity.this, LoginReg.class);
                    intent.putExtra("Email",email);
                    intent.putExtra("Password",password);
                    startActivity(intent);
                }

            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Check if the username and password ( in the database ) are correct
                 * if correct, sent Username to Profile
                 */
                // TODO: need to add a field for email
                String email = emailField.getText().toString(); // need to get email from some editText...
                String password = passwordField.getText().toString();

                Log.d("current_user", passwordField.getText().toString());
                attemptLogin(email, password);

            }
        });

    }

    public void attemptLogin(String email, String password){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        //TODO:
                        Intent intent = new Intent(MainActivity.this, CreateMoodActivity.class);
                        //intent.putExtra("ProfileUserName",user.getDisplayName());
                        intent.putExtra(EXTRA_USERPATH, userPathStr);
                        intent.putExtra("CurrentUsername", user.getDisplayName());
                        startActivity(intent);

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        // Set the UI so the user knows login failed
                    }
                }
            });
    }

    public boolean validate(String email, String pwd){
        boolean valid = true;

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(MainActivity.this, "Please enter valid email address.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (pwd.isEmpty()){
            Toast.makeText(MainActivity.this, "Please enter password.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }
}







