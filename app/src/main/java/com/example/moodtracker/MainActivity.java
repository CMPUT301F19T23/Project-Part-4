package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    // Declare the variable.
    EditText usernameField, passwordField;
    EditText emailField; // remove this if you don't make the signup page a fragment
    Button signUpButton;
    Button signInButton;
    FirebaseAuth auth;
    FirebaseFirestore db;
    public static final String EXTRA_USERPATH = "com.example.myfirstapp.MESSAGE"; // Filepath to get to the Users database
    private String userPathStr = "Users/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        usernameField = findViewById(R.id.username_sign_in_edit_text);
        passwordField = findViewById(R.id.password_sign_in_edit_text);
        signInButton = findViewById(R.id.sign_in_button);
        signUpButton = findViewById(R.id.go_to_sign_up_button);


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * Check if the username and password ( in the database ) are correct
                 * if correct, sent Username to Profile
                 */
                // TODO: need to add a field for email
                String email = ""; // need to get email from some editText...
                String password = passwordField.getText().toString();
                attemptLogin(email, password);
                sendUserToProfile();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = usernameField.getText().toString();
                String password = passwordField.getText().toString();
                // TODO: need to add a field for email, and maybe a field to confirm password
                String email = ""; // need to get email from some editText...
                createUser(email, password, name);
            }
        });
    }

    private void sendUserToProfile(){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
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

                        //TODO: create Intent and switch, or call function that does
                        // make sure that you set EXTRA_USERPATH so other activities can
                        // use the database

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

    public void createUser(String email, String password, final String username){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(TAG, "User profile updated.");
                                        }
                                    }
                                });

                    } else {
                        // TODO: If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });

        HashMap<String, String> hm = new HashMap<>();
        hm.put("userType", "Participant");
        db.document(userPathStr+username)
                .set(hm)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Data addition successful");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Data addition failed " + e.toString());
                    }
                });

    }

    }






