package com.developer.abhinavraj.servify_app.client.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.database.models.Address;
import com.developer.abhinavraj.servify_app.client.database.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView appBarTitle = findViewById(R.id.app_bar_title);
        appBarTitle.setText(R.string.profile);

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setVisibility(View.GONE);

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this,MainActivity.class));
            finish();
        });

        TextView membershipApply = findViewById(R.id.membership_apply);

        membershipApply.setOnClickListener(view -> {
            membershipApply.setText("Membership activated");
        });

        String mEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        TextView profileName = findViewById(R.id.profile_name);
        TextView profileAge = findViewById(R.id.profile_age);
        TextView profileNumber = findViewById(R.id.profile_number);
        TextView addressLine1 = findViewById(R.id.address_line_1);
        TextView addressLine2 = findViewById(R.id.address_line_2);
        TextView addressLine3 = findViewById(R.id.address_line_3);
        TextView city = findViewById(R.id.city);
        TextView postalCode = findViewById(R.id.postal_code);

        Source source = Source.CACHE;

        assert mEmail != null;
        db.collection("customers").document(mEmail).get(source)
                .addOnSuccessListener(documentSnapshot -> {
                    User user = documentSnapshot.toObject(User.class);

                    assert user != null;
                    String mFirstName = user.getFirstName();
                    String mLastName = user.getLastName();
                    String mProfileName = mFirstName + " " + mLastName;
                    String mPhoneNumber = user.getPhoneNumber();
                    String mAge = user.getAge() + " years old";

                    profileName.setText(mProfileName);
                    profileAge.setText(mAge);
                    profileNumber.setText(mPhoneNumber);
                });

        db.collection("customers").document(mEmail).collection("address").document(mEmail).get(source)
                .addOnSuccessListener(documentSnapshot -> {
                    Address address = documentSnapshot.toObject(Address.class);
                    assert address != null;
                    addressLine1.setText(address.addressLine1);
                    city.setText(address.city);
                    postalCode.setText(address.postalCode);
                    if(!address.addressLine2.isEmpty()){
                        addressLine2.setText(address.addressLine2);
                    }
                    if(!address.addressLine3.isEmpty()){
                        addressLine3.setText(address.addressLine3);
                    }
                });
    }
}