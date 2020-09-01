package com.developer.abhinavraj.servify_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.developer.abhinavraj.servify_app.admin.activity.LoginActivity;
import com.developer.abhinavraj.servify_app.admin.activity.signUp.SignUpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_top);
        findViewById(R.id.admin).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, com.developer.abhinavraj.servify_app.admin.activity.MainActivity.class)));
        findViewById(R.id.customer).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, com.developer.abhinavraj.servify_app.client.activity.MainActivity.class)));
    }
}