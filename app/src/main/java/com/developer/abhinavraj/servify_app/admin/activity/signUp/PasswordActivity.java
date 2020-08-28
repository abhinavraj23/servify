package com.developer.abhinavraj.servify_app.admin.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.admin.activity.HomeActivity;
import com.developer.abhinavraj.servify_app.client.utils.Utility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordActivity extends AppCompatActivity {

    private static String TAG = "PasswordActivity.class";
    private EditText password;
    private EditText confirmPassword;
    private ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        pgBar = findViewById(R.id.pBar);

        /*
        UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        AddressViewModel addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);
        */


        findViewById(R.id.finish).setOnClickListener(view -> {
            pgBar.setVisibility(View.VISIBLE);
            String mPassword = password.getText().toString();
            String mConfirmPassword = confirmPassword.getText().toString();

            boolean validateInput = !TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mConfirmPassword);

            if (validateInput) {
                if (validatePassword(mPassword, mConfirmPassword)) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    FirebaseUser mUser = mAuth.getCurrentUser();
                    assert mUser != null;
                    mUser.updatePassword(mConfirmPassword).addOnCompleteListener(task -> {
                        pgBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            Log.d(getApplicationContext().toString(), "User password updated.");
                            startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Utility.showInputError(getApplicationContext());
            } else Utility.showInputError(getApplicationContext());
        });
    }

    private boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}