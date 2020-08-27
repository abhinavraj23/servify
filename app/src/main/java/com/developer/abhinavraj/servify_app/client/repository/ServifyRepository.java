package com.developer.abhinavraj.servify_app.client.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.developer.abhinavraj.servify_app.client.database.ServifyDatabase;
import com.developer.abhinavraj.servify_app.client.database.dao.AddressDao;
import com.developer.abhinavraj.servify_app.client.database.dao.UserDao;
import com.developer.abhinavraj.servify_app.client.database.models.Address;
import com.developer.abhinavraj.servify_app.client.database.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ServifyRepository {

    private UserDao mUserDao;
    private AddressDao mAddressDao;
    private LiveData<User> mUser;
    private FirebaseAuth mAuth;

    public ServifyRepository(Application application) {
        ServifyDatabase db = ServifyDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAddressDao = db.addressDao();
        mAuth = FirebaseAuth.getInstance();
    }

    public User getUser(String email) {
        return mUserDao.getUserFromEmail(email);
    }

    public Address getAddress(String email) {
        return mAddressDao.getAddressFromEmail(email);
    }

    public void insertUser(User user) {
        ServifyDatabase.databaseWriteExecutor.execute(() -> {
            mUserDao.insert(user);
        });
    }

    public void createAndInsertUser(String email, String password, String firstName,
                                    String lastName, String phoneNumber, String age) {
        insertUser(new User(email, password, firstName, lastName, phoneNumber, age));
    }

    public void updateDisplayNameAtFirebase(Context context, String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(context.toString(), "User profile updated.");
                        }
                    }
                });
    }

    public void insertAddress(Address address) {
        ServifyDatabase.databaseWriteExecutor.execute(() -> {
            mAddressDao.insert(address);
        });
    }
}
