package com.developer.abhinavraj.servify_app.client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.adapter.ServiceAdapter;
import com.developer.abhinavraj.servify_app.client.database.models.ServiceProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private ServiceAdapter serviceAdapter;
    private List<ServiceProvider> serviceProviderList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        serviceProviderList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(getApplicationContext(), serviceProviderList);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView appBarTitle = findViewById(R.id.app_bar_title);
        appBarTitle.setText(R.string.app_name);

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setVisibility(View.VISIBLE);
        profileBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        db = FirebaseFirestore.getInstance();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceAdapter);

        TextView textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.description),Html.FROM_HTML_MODE_LEGACY));

        findViewById(R.id.maid).setOnClickListener(onServiceClicked("maids"));
        findViewById(R.id.cleaner).setOnClickListener(onServiceClicked("house_cleaners"));
        findViewById(R.id.cook).setOnClickListener(onServiceClicked("cooks"));
        findViewById(R.id.gardener).setOnClickListener(onServiceClicked("gardeners"));
    }

    private View.OnClickListener onServiceClicked(String serviceName) {
        return view -> {
            db.collection(serviceName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    serviceProviderList.clear();
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        /*
                        ServiceProvider serviceProvider = document.toObject(ServiceProvider.class);
                        serviceProviderList.add(serviceProvider);
                        */
                        System.out.println(document.getData());
                    }
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    Log.d(getApplicationContext().toString(), "Error getting documents: ", task.getException());
                }
            });
        };
    }
}