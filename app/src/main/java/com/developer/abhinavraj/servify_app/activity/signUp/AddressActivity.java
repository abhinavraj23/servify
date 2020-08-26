package com.developer.abhinavraj.servify_app.activity.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.database.models.Address;
import com.developer.abhinavraj.servify_app.utils.Utility;
import com.developer.abhinavraj.servify_app.viewModel.AddressViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AddressActivity extends AppCompatActivity {

    private EditText firstLine;
    private EditText secondLine;
    private EditText thirdLine;
    private EditText postalCode;
    private EditText city;
    private EditText state;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    //private AddressViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firstLine = findViewById(R.id.first_line);
        secondLine = findViewById(R.id.second_line);
        thirdLine = findViewById(R.id.third_line);
        postalCode = findViewById(R.id.postal_code);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);

      //  mViewModel = new ViewModelProvider(this).get(AddressViewModel.class);

        findViewById(R.id.next).setOnClickListener(view -> {
            String mFirstLine = firstLine.getText().toString();
            String mSecondLine = secondLine.getText().toString();
            String mThirdLine = thirdLine.getText().toString();
            String mPostalCode = postalCode.getText().toString();
            String mCity = city.getText().toString();
            String mState = state.getText().toString();

            boolean validate = !TextUtils.isEmpty(mFirstLine) && !TextUtils.isEmpty(mPostalCode)
                    && !TextUtils.isEmpty(mCity) && !TextUtils.isEmpty(mState);

            if (validate) {
                String email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                assert email != null;

//                if (mAuth.getCurrentUser().get != null) {
//                    Toast.makeText(getApplicationContext(), "Address already exists", Toast.LENGTH_SHORT).show();
//                } else {
                    Address address = new Address(email, mFirstLine, mSecondLine, mThirdLine, mPostalCode, mCity, mState);
                    db.collection("customers").document(mEmail).set(userMap);
                    startActivity(new Intent(AddressActivity.this, PasswordActivity.class));

            } else Utility.showInputError(getApplicationContext());
        });
    }
}