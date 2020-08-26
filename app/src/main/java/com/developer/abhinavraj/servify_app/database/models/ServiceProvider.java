package com.developer.abhinavraj.servify_app.database.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "service_provider_table")
public class ServiceProvider {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "first_name", defaultValue = "")
    private String firstName;

    @ColumnInfo(name = "last_name", defaultValue = "")
    private String lastName;

    @ColumnInfo(name = "mobile_number")
    private String mobileNumber;

    @ColumnInfo(name = "age")
    private String age;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "rating")
    private double rating;

    @ColumnInfo(name = "service")
    private Service service;

    @ColumnInfo(name = "gender")
    private Gender gender;

    public ServiceProvider(@NotNull String email,
                           String firstName,
                           String lastName,
                           String mobileNumber,
                           String age,
                           String city,
                           double rating,
                           Service service,
                           Gender gender) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.age = age;
        this.city = city;
        this.rating = rating;
        this.service = service;
        this.gender = gender;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
