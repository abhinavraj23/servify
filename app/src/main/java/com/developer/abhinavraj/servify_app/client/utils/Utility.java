package com.developer.abhinavraj.servify_app.client.utils;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Utility {

    public static boolean validateEmail(String email) {
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidMobile(String phone) {
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;
    }

    public static void showInputError(Context context) {
        Toast.makeText(context, "Input error", Toast.LENGTH_SHORT).show();
    }
}
