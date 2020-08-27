package com.developer.abhinavraj.servify_app.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.utils.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private ProgressBar pgsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String TAG = getApplicationContext().toString();

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        pgsBar = findViewById(R.id.pBar);
        Button login = findViewById(R.id.next);

        login.setOnClickListener(view -> {
            String mEmail = email.getText().toString();
            String mPassword = password.getText().toString();
            pgsBar.setVisibility(View.VISIBLE);

            if (Utility.validateEmail(mEmail) && !TextUtils.isEmpty(mPassword)) {
                mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            pgsBar.setVisibility(View.INVISIBLE);
                            if (task.isSuccessful()) {
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Successfully Authenticated",
                                        Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else Utility.showInputError(getApplicationContext());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}