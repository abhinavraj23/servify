package com.developer.abhinavraj.servify_app.client.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.database.models.User;
import com.developer.abhinavraj.servify_app.client.utils.Utility;
import com.developer.abhinavraj.servify_app.client.viewModel.UserViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText age;
    private FirebaseAuth mAuth;
    private UserViewModel mUserViewModel;
    private FirebaseFirestore db;
    private ProgressBar pgBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        db = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phone_number);
        age = findViewById(R.id.age);
        pgBar = findViewById(R.id.pBar);

        findViewById(R.id.next).setOnClickListener(view -> {
            String mFirstName = firstName.getText().toString();
            String mLastName = lastName.getText().toString();
            String mEmail = email.getText().toString();
            String mPhoneNumber = phoneNumber.getText().toString();
            String mAge = age.getText().toString();

            boolean validate = !TextUtils.isEmpty(mFirstName) && !TextUtils.isEmpty(mLastName)
                    && Utility.validateEmail(mEmail) && Utility.isValidMobile(mPhoneNumber);

            if (validate) {
                pgBar.setVisibility(View.VISIBLE);
                String tempPass = mFirstName + "_" + mLastName;
                User user = new User(mEmail, mFirstName, mLastName, mPhoneNumber, mAge);

                /*if (mUserViewModel.getUser(mEmail) != null) {

                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                } else {*/
                mAuth.createUserWithEmailAndPassword(mEmail, tempPass)
                        .addOnCompleteListener(SignUpActivity.this, task -> {
                            if (task.isSuccessful()) {
                                Log.d(getApplicationContext().toString(), "createUserWithEmail:success");
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(mFirstName)
                                        .build();

                                mAuth.getCurrentUser().updateProfile(profileUpdates);
                                db.collection("customers").document(mEmail).set(user)
                                        .addOnCompleteListener(task1 -> {
                                            pgBar.setVisibility(View.INVISIBLE);
                                            if (task1.isSuccessful()) {
                                                Log.d(getApplicationContext().toString(), "User profile updated.");
                                                startActivity(new Intent(SignUpActivity.this, AddressActivity.class));
                                            } else {
                                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            } else {
                Utility.showInputError(getApplicationContext());
            }
        });

    }
}