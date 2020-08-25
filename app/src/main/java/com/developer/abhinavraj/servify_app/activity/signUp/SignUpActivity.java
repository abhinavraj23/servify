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
import com.developer.abhinavraj.servify_app.utils.Utility;
import com.developer.abhinavraj.servify_app.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText age;
    private FirebaseAuth mAuth;
    private UserViewModel mUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        age = findViewById(R.id.age);

        findViewById(R.id.next).setOnClickListener(view -> {
            String mFirstName = firstName.getText().toString();
            String mLastName = lastName.getText().toString();
            String mEmail = email.getText().toString();
            String mPhoneNumber = phoneNumber.getText().toString();
            String mAge = age.getText().toString();

            boolean validate = !TextUtils.isEmpty(mFirstName) && !TextUtils.isEmpty(mLastName)
                    && !Utility.validateEmail(mEmail) && !Utility.isValidMobile(mPhoneNumber);

            if (validate) {
                String tempPass = mFirstName + "_" + mLastName;

                if (mUserViewModel.getUser(mEmail) != null) {
                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(mEmail, tempPass)
                            .addOnCompleteListener(SignUpActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    Log.d(getApplicationContext().toString(), "createUserWithEmail:success");
                                    mUserViewModel.updateDisplayName(getApplicationContext(), mFirstName);
                                    mUserViewModel.createAndInsertUser(mEmail, tempPass, mFirstName, mLastName, mPhoneNumber, mAge);
                                    startActivity(new Intent(SignUpActivity.this, AddressActivity.class));
                                } else {
                                    Log.w(getApplicationContext().toString(), "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            } else Utility.showInputError(getApplicationContext());

        });
    }
}