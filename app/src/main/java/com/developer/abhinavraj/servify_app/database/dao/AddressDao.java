package com.developer.abhinavraj.servify_app.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.developer.abhinavraj.servify_app.database.models.Address;

@Dao
public interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Address address);

    @Query("DELETE FROM address_table")
    void deleteAll();

    @Query("SELECT * FROM address_table WHERE email=:mEmail")
    Address getAddressFromEmail(String mEmail);
}
