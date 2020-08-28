package com.developer.abhinavraj.servify_app.admin.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.wear.widget.CircledImageView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.admin.database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private CircledImageView circledImageView;
    private TextView name;
    private TextView age;
    private TextView phNumber;
    private TextView addressLine1;
    private TextView addressLine2;
    private TextView addressLine3;
    private ProgressBar pgsBar;
    private Button complete;
    private TextView prompt;

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
        complete = findViewById(R.id.complete);
        prompt = findViewById(R.id.no_user);

        db = FirebaseFirestore.getInstance();
        final CollectionReference docRef = db.collection("cities");

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().size() == 0) {
                    prompt.setVisibility(View.VISIBLE);
                }
                 else if(task.getResult().size() == 1) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
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
            if(value.size() == 1) {
                for (QueryDocumentSnapshot document : value) {
                    User user = document.toObject(User.class);
                    setUserProfile(user);
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUserProfile(User user) {
        name.setText(user.getFirstName());
        age.setText(user.getAge() + "years old");
        phNumber.setText(user.getMobileNumber());
    }
}