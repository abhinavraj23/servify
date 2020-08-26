package com.developer.abhinavraj.servify_app.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

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
                        }
                    });

                    String email = mUser.getEmail();
                    assert email != null;
                   /* User user = userViewModel.getUser(email);
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("first_name", user.getFirstName());
                    userMap.put("last_name", user.getLastName());
                    userMap.put("age", user.getAge());
                    userMap.put("phone_number", user.getMobileNumber());*/

                    DocumentReference ref = db.collection("customers").document(email);
                    Address address = addressViewModel.getAddress(email);

                    Map<String, Object> addressMap = new HashMap<>();
                    addressMap.put("first_line", address.getAddressLine1());
                    addressMap.put("second_line", address.getAddressLine2());
                    addressMap.put("third_line", address.getAddressLine3());
                    addressMap.put("postal_code", address.getPostalCode());
                    addressMap.put("city", address.getCity());
                    addressMap.put("state", address.getState());

                    ref.collection("address").document(email).set(addressMap).addOnSuccessListener(mVoid -> {
                        Log.d(TAG, "Address DocumentSnapshot added");
                        startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                    }).addOnFailureListener(e -> {
                        Log.d(TAG, "Address DocumentSnapshot Failed");
                    });
/*                    ref.set(userMap).addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "User DocumentSnapshot added");

                        Address address = addressViewModel.getAddress(email);

                        Map<String, Object> addressMap = new HashMap<>();
                        addressMap.put("first_line", address.getAddressLine1());
                        addressMap.put("second_line", address.getAddressLine2());
                        addressMap.put("third_line", address.getAddressLine3());
                        addressMap.put("postal_code", address.getPostalCode());
                        addressMap.put("city", address.getCity());
                        addressMap.put("state", address.getState());

                        ref.collection("address").document(email).set(addressMap).addOnSuccessListener(mVoid -> {
                            Log.d(TAG, "Address DocumentSnapshot added");
                            startActivity(new Intent(PasswordActivity.this, HomeActivity.class));
                        }).addOnFailureListener(e -> {
                            Log.d(TAG, "Address DocumentSnapshot Failed");
                        });

                    }).addOnFailureListener(e -> {
                        Log.d(TAG, "User DocumentSnapshot Failed");
                    });*/


                } else Utility.showInputError(getApplicationContext());
            } else Utility.showInputError(getApplicationContext());
        });
    }

    private boolean validatePassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}