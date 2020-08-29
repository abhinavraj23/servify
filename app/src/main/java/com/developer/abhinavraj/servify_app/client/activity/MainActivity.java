package com.developer.abhinavraj.servify_app.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.activity.signUp.SignUpActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.next);
        Button signUp = findViewById(R.id.sign_up);

        login.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        signUp.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignUpActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }
}