package com.developer.abhinavraj.servify_app.client.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.developer.abhinavraj.servify_app.client.database.models.Address;
import com.developer.abhinavraj.servify_app.client.repository.ServifyRepository;

public class AddressViewModel extends AndroidViewModel {

    private ServifyRepository mRepository;

    public AddressViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ServifyRepository(application);
    }

    public Address getAddress(String email) {
        return mRepository.getAddress(email);
    }

    public void insert(Address address) {
        mRepository.insertAddress(address);
    }
}
