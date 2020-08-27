package com.developer.abhinavraj.servify_app.client.viewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.developer.abhinavraj.servify_app.client.database.models.User;
import com.developer.abhinavraj.servify_app.client.repository.ServifyRepository;

public class UserViewModel extends AndroidViewModel {

    private ServifyRepository mRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ServifyRepository(application);
    }

    public User getUser(String email) {
        return mRepository.getUser(email);
    }

    public void insert(User user) {
        mRepository.insertUser(user);
    }

    public void createAndInsertUser(String email, String password, String firstName,
                                    String lastName, String phoneNumber, String age) {
        mRepository.createAndInsertUser(email, password, firstName, lastName, phoneNumber, age);
    }

    public void updateDisplayName(Context context, String displayName) {
        mRepository.updateDisplayNameAtFirebase(context, displayName);
    }
}
