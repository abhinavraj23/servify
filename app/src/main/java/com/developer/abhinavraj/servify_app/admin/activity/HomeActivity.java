package com.developer.abhinavraj.servify_app.admin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.CircledImageView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.admin.database.models.User;
import com.developer.abhinavraj.servify_app.client.database.models.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CircledImageView circledImageView;
    private TextView name;
    private TextView age;
    private TextView phNumber;
    private TextView addressLine1;
    private TextView addressLine2;
    private TextView addressLine3;
    private TextView city;
    private TextView postalCode;
    private ProgressBar pgsBar;
    private TextView prompt;
    private String email;
    private String currentUserId;

    private String TAG = "HomeActivity.class";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        pgsBar = findViewById(R.id.pBar);
        name = findViewById(R.id.profile_name);
        age = findViewById(R.id.profile_age);
        phNumber = findViewById(R.id.profile_number);
        addressLine1 = findViewById(R.id.address_line_1);
        addressLine2 = findViewById(R.id.address_line_2);
        addressLine3 = findViewById(R.id.address_line_3);
        city = findViewById(R.id.city);
        postalCode = findViewById(R.id.postal_code);
        Button complete = findViewById(R.id.complete);
        prompt = findViewById(R.id.no_user);

        db = FirebaseFirestore.getInstance();
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        email = mUser.getEmail();
        final CollectionReference docRef = db.collection("service_provider").document(email).
                collection("current_user");

        docRef.get().addOnCompleteListener(task -> {
            pgsBar.setVisibility(View.INVISIBLE);
            if (task.isSuccessful()) {
                if (task.getResult().size() == 0) {
                    prompt.setVisibility(View.VISIBLE);
                } else if (task.getResult().size() == 1) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        currentUserId = document.getId();
                        setUserProfile(user);
                    }
                } else {
                    prompt.setVisibility(View.VISIBLE);
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });

        docRef.addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (value.size() == 1) {
                for (QueryDocumentSnapshot document : value) {
                    User user = document.toObject(User.class);
                    setUserProfile(user);
                }
            }
        });

        complete.setOnClickListener(view -> {
            if (currentUserId != null)
                docRef.document(currentUserId).delete();
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUserProfile(User user) {
        name.setText(user.getFirstName());
        age.setText(user.getAge() + " years old");
        phNumber.setText(user.getMobileNumber());

        db.collection("service_provider").document(email).collection("address").document(email).get()
                .addOnSuccessListener(documentSnapshot -> {
                    Address address = documentSnapshot.toObject(Address.class);
                    assert address != null;
                    addressLine1.setText(address.addressLine1);
                    city.setText(address.city);
                    postalCode.setText(address.postalCode);
                    if (!address.addressLine2.isEmpty()) {
                        addressLine2.setText(address.addressLine2);
                    }
                    if (!address.addressLine3.isEmpty()) {
                        addressLine3.setText(address.addressLine3);
                    }
                });
    }
}