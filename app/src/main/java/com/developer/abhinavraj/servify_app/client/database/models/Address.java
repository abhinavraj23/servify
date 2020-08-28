package com.developer.abhinavraj.servify_app.client.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "address_table")
public class Address {

    @NonNull
    @ColumnInfo(name = "address_line_1")
    public String addressLine1;
    @ColumnInfo(name = "address_line_2", defaultValue = "")
    public String addressLine2;
    @ColumnInfo(name = "address_line_3", defaultValue = "")
    public String addressLine3;
    @NonNull
    @ColumnInfo(name = "postal_code")
    public String postalCode;
    @NonNull
    @ColumnInfo(name = "city")
    public String city;
    @NonNull
    @ColumnInfo(name = "state")
    public String state;
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    public Address () {}

    public Address(
            @NonNull String email,
            @NonNull String addressLine1,
            String addressLine2,
            String addressLine3,
            @NonNull String postalCode,
            @NonNull String city,
            @NonNull String state) {
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.postalCode = postalCode;
        this.city = city;
        this.state = state;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(@NonNull String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull String city) {
        this.city = city;
    }

    @NonNull
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(@NonNull String postalCode) {
        this.postalCode = postalCode;
    }

    @NonNull
    public String getState() {
        return state;
    }

    public void setState(@NonNull String state) {
        this.state = state;
    }
}
