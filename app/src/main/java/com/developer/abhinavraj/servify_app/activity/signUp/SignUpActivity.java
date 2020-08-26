package com.developer.abhinavraj.servify_app.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.utils.Utility;
import com.developer.abhinavraj.servify_app.viewModel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phoneNumber;
    private EditText age;
    private FirebaseAuth mAuth;
    private UserViewModel mUserViewModel;
    private FirebaseFirestore db;

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

        findViewById(R.id.next).setOnClickListener(view -> {
            String mFirstName = firstName.getText().toString();
            String mLastName = lastName.getText().toString();
            String mEmail = email.getText().toString();
            String mPhoneNumber = phoneNumber.getText().toString();
            String mAge = age.getText().toString();

            boolean validate = !TextUtils.isEmpty(mFirstName) && !TextUtils.isEmpty(mLastName)
                    && Utility.validateEmail(mEmail) && Utility.isValidMobile(mPhoneNumber);

            if (validate) {
                String email = mAuth.getCurrentUser().getEmail();
                String tempPass = mFirstName + "_" + mLastName;
                final Map<String, Object> userMap = new HashMap<>();
                userMap.put("first_name", mFirstName);
                userMap.put("last_name", mLastName);
                userMap.put("age", mAge);
                userMap.put("phone_number", mPhoneNumber);

                Log.e("Debug", email);
                if (email.isEmpty() || !email.equals(mEmail)) {
                    Log.e("Debug", "Reached here");
                /*if (mUserViewModel.getUser(mEmail) != null) {

                    Toast.makeText(getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
                } else {*/
                    mAuth.createUserWithEmailAndPassword(mEmail, tempPass)
                            .addOnCompleteListener(SignUpActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    Log.d(getApplicationContext().toString(), "createUserWithEmail:success");
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(mFirstName)
                                            //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();

                                    mAuth.getCurrentUser().updateProfile(profileUpdates);
                                    db.collection("customers").document(mEmail).set(userMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d(getApplicationContext().toString(), "User profile updated.");
                                                        startActivity(new Intent(SignUpActivity.this, AddressActivity.class));
                                                    }
                                                }
                                            });
                                    /*mUserViewModel.createAndInsertUser(mEmail, tempPass, mFirstName, mLastName, mPhoneNumber, mAge)*/
                                    //startActivity(new Intent(SignUpActivity.this, AddressActivity.class));

                                    /*mUserViewModel.updateDisplayName(getApplicationContext(), mFirstName);

                                 mUserViewModel.createAndInsertUser(mEmail, tempPass, mFirstName, mLastName, mPhoneNumber, mAge);*/
                                }
                            });


                }
            } else {
                Utility.showInputError(getApplicationContext());
            }
        });

    }
}