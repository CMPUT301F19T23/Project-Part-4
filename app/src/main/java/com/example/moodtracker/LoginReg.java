package com.example.moodtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class LoginReg extends AppCompatActivity {
    // Declare the variable.
    ArrayList<User> userDataList;
    EditText username, password;
    Button signUpButton;
    TextView goBackLogin;

    String name, pwd;
    User newUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_reg);
        setViews();

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
                name = username.getText().toString();
                pwd = password.getText().toString();
                newUser = new User(name, pwd);

                userDataList = new ArrayList<>();
                userDataList.add(newUser);
                Log.d("Datalist",userDataList.get(0).getUsername());
                Log.d("Datalist",userDataList.get(0).getPassword());
                //go back to login with username filled ( may be also and password)

                Intent intent = new Intent(LoginReg.this, MainActivity.class);
                startActivity(intent);
            }

        });

    }

    private void setViews(){
        username = findViewById(R.id.username_sign_up_edit_text);
        password = findViewById(R.id.password_sign_up_edit_text);
        signUpButton = findViewById(R.id.sign_up_button);
        goBackLogin = findViewById(R.id.go_back_to_login);
    }
}
