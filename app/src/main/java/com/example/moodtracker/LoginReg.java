package com.example.moodtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class LoginReg extends AppCompatActivity {
    // Declare the variable.
    EditText username, useremail, password, cpassword;
    Button signUpButton;
    TextView goBackLogin;
    ProgressBar progressBar;

    String email, pwd, confirm, name;

    FirebaseAuth auth;
    FirebaseFirestore db;
    private String userPathStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username_sign_up_edit_text);
        useremail = findViewById(R.id.email_sign_up_edit_text);
        password = findViewById(R.id.password_sign_up_edit_text);
        cpassword = findViewById(R.id.password_confirm_edit_text);
        signUpButton = findViewById(R.id.sign_up_button);
        goBackLogin = findViewById(R.id.go_back_to_login);
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        String newEmail  = intent.getStringExtra("Email");
        String newPassword  = intent.getStringExtra("Password");
        useremail.setText(newEmail);
        password.setText(newPassword);
        userPathStr = intent.getStringExtra(MainActivity.EXTRA_USERPATH);

        goBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginReg.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()){
                    registerUser();
                    Intent intent = new Intent(LoginReg.this, MainActivity.class);
                    startActivity(intent);

                }
            }

        });

    }

    public void registerUser(){
        final String name = username.getText().toString().trim();
        final String email = useremail.getText().toString().trim();
        final String userpass = password.getText().toString();
        final User userobj = new User(name, email);

        if (validate()){
            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email,userpass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        //we will store the additional fields in firebase database

                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userobj.getUsername())
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
                        HashMap<String, String> users = new HashMap<>();
                        users.put("userType", "Participant");
                        db.document(userPathStr + name)
                                .set(users)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "Data addition successful");
                                        Toast.makeText(LoginReg.this, "Data addition successful.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Data addition failed " + e.toString());
                                        Toast.makeText(LoginReg.this, "Data addition failed", Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }
                    else{
                        Log.d(TAG, "Failed to create user");
                        Toast.makeText(LoginReg.this, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                }

            });

        }

    }

    public boolean validate(){
        name = username.getText().toString();
        email = useremail.getText().toString();
        pwd = password.getText().toString();
        confirm = cpassword.getText().toString();
        boolean valid = true;

        if (name.isEmpty()){
            Toast.makeText(LoginReg.this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(LoginReg.this, "Please enter valid email address.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (pwd.isEmpty()){
            Toast.makeText(LoginReg.this, "Please enter password.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (confirm.isEmpty() || !pwd.equals(confirm)){
            Toast.makeText(LoginReg.this, "Password does not match.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}
