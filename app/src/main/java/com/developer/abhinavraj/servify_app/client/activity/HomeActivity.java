package com.developer.abhinavraj.servify_app.client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.abhinavraj.servify_app.R;
import com.developer.abhinavraj.servify_app.client.adapter.ServiceAdapter;
import com.developer.abhinavraj.servify_app.client.database.models.ServiceProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

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

        db = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView appBarTitle = findViewById(R.id.app_bar_title);
        appBarTitle.setText(R.string.app_name);

        Button profileBtn = findViewById(R.id.profileBtn);
        profileBtn.setVisibility(View.VISIBLE);
        profileBtn.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        TextView textView = findViewById(R.id.textView);
        textView.setText(Html.fromHtml(getResources().getString(R.string.description),Html.FROM_HTML_MODE_LEGACY));

        findViewById(R.id.maid).setOnClickListener(onServiceClicked("maids"));
        findViewById(R.id.cleaner).setOnClickListener(onServiceClicked("house_cleaners"));
        findViewById(R.id.cook).setOnClickListener(onServiceClicked("cooks"));
        findViewById(R.id.gardener).setOnClickListener(onServiceClicked("gardeners"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        serviceProviderList = new ArrayList<>();
        db.collection("house_cleaners").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ServiceProvider serviceProvider = doc.toObject(ServiceProvider.class);
                        serviceProviderList.add(serviceProvider);
                    }

                    serviceAdapter = new ServiceAdapter(getApplicationContext(), serviceProviderList, new ServiceAdapter.BookListener() {
                        @Override
                        public void OnClick(View v, int position) {
                            AlertDialog dialog;
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                            alertDialog.setTitle(serviceProviderList.get(position).getFirstName());
                            alertDialog.setMessage("Do you want to Book the Service Provider?");
                            alertDialog.setCancelable(true);
                            alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            alertDialog.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Add to database or something like that TODO
                                }
                            });

                            dialog = alertDialog.create();
                            dialog.show();
                        }
                    });

                    recyclerView.setAdapter(serviceAdapter);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceAdapter);
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