package com.developer.abhinavraj.servify_app.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.activity.HomeActivity;
import com.developer.abhinavraj.servify_app.database.models.Address;
import com.developer.abhinavraj.servify_app.utils.Utility;
import com.developer.abhinavraj.servify_app.viewModel.AddressViewModel;
import com.developer.abhinavraj.servify_app.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PasswordActivity extends AppCompatActivity {

    private static String TAG = "PasswordActivity.class";
    private EditText password;
    private EditText confirmPassword;
    private UserViewModel userViewModel;
    private AddressViewModel addressViewModel;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        db = FirebaseFirestore.getInstance();

        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirm_password);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        addressViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        findViewById(R.id.finish).setOnClickListener(view -> {
            String mPassword = password.getText().toString();
            String mConfirmPassword = confirmPassword.getText().toString();

            boolean validateInput = !TextUtils.isEmpty(mPassword) && !TextUtils.isEmpty(mConfirmPassword);

            if (validateInput) {
                if (validatePassword(mPassword, mConfirmPassword)) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    FirebaseUser mUser = mAuth.getCurrentUser();
                    assert mUser != null;
                    mUser.updatePassword(mConfirmPassword).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(getApplicationContext().toString(), "User password updated.");
                            startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                            finish();
                        } else{
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