package com.developer.abhinavraj.servify_app.client.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.adapter.ServiceAdapter;
import com.developer.abhinavraj.servify_app.client.database.models.ServiceProvider;
import com.developer.abhinavraj.servify_app.client.database.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ServiceAdapter serviceAdapter;
    private List<ServiceProvider> serviceProviderList;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView appBarTitle = findViewById(R.id.app_bar_title);
        appBarTitle.setText(R.string.app_name);

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setVisibility(View.VISIBLE);
        profileBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        TextView textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.description), Html.FROM_HTML_MODE_LEGACY));



        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        String mEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        assert mEmail != null;
        db.collection("customers").document(mEmail).get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    assert documentSnapshot != null;
                    user = documentSnapshot.toObject(User.class);

                    serviceProviderList = new ArrayList<>();

                    findViewById(R.id.maid).setOnClickListener(onServiceClicked("maids"));
                    findViewById(R.id.cleaner).setOnClickListener(onServiceClicked("house_cleaners"));
                    findViewById(R.id.cook).setOnClickListener(onServiceClicked("cooks"));
                    findViewById(R.id.gardener).setOnClickListener(onServiceClicked("gardeners"));

                    serviceAdapter = new ServiceAdapter(getApplicationContext(), serviceProviderList, (v, position) -> {
                        AlertDialog dialog;
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                        alertDialog.setTitle(serviceProviderList.get(position).getFirstName());
                        alertDialog.setMessage("Do you want to Book the Service Provider?");
                        alertDialog.setCancelable(true);
                        alertDialog.setPositiveButton("No", (dialog1, which) -> dialog1.dismiss());
                        String serviceEmail = serviceProviderList.get(position).getEmail();

                        alertDialog.setNegativeButton("Yes", (dialog12, which) -> {
                            //Add to database or something like that TODO
                            db.collection("service_providers").document(serviceEmail).collection("current_user").get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            queryDocumentSnapshots.forEach(queryDocumentSnapshot -> {
                                                if (queryDocumentSnapshot.get("email") != mEmail) {
                                                    Toast.makeText(HomeActivity.this, "Service Provider is currently not available", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            db.collection("service_providers").document(serviceEmail).collection("current_user")
                                                    .document(mEmail).set(user).addOnCompleteListener(task1 ->
                                                    Toast.makeText(HomeActivity.this, "Booking successfully completed!", Toast.LENGTH_SHORT).show());
                                        }
                                    });
                        });

                        dialog = alertDialog.create();
                        dialog.show();
                    });

                    recyclerView.setAdapter(serviceAdapter);

                });

    }

    private View.OnClickListener onServiceClicked(String serviceName) {
        return view -> {
            serviceProviderList.clear();
            serviceAdapter.notifyDataSetChanged();
            findViewById(R.id.maid).setBackgroundColor(getColor(R.color.lightRed));
            findViewById(R.id.cleaner).setBackgroundColor(getColor(R.color.lightRed));
            findViewById(R.id.cook).setBackgroundColor(getColor(R.color.lightRed));
            findViewById(R.id.gardener).setBackgroundColor(getColor(R.color.lightRed));
            view.setBackgroundColor(Color.WHITE);

            String serviceMap;
            switch (serviceName) {
                case "maids":
                    serviceMap = "1";
                    break;
                case "gardeners":
                    serviceMap = "2";
                    break;
                case "cooks":
                    serviceMap = "3";
                    break;
                default:
                    serviceMap = "4";
                    break;
            }

            db.collection("service_providers").whereEqualTo("service", serviceMap).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        ServiceProvider serviceProvider = doc.toObject(ServiceProvider.class);
                        serviceProviderList.add(serviceProvider);
                    }
                    serviceAdapter.notifyDataSetChanged();
                }
            });
        };
    }
}