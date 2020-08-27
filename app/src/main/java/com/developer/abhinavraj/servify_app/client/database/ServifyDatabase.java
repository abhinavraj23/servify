package com.developer.abhinavraj.servify_app.client.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.developer.abhinavraj.servify_app.client.database.dao.AddressDao;
import com.developer.abhinavraj.servify_app.client.database.dao.UserDao;
import com.developer.abhinavraj.servify_app.client.database.models.Address;
import com.developer.abhinavraj.servify_app.client.database.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {User.class, Address.class}, version = 1, exportSchema = false)
public abstract class ServifyDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static volatile ServifyDatabase INSTANCE;

    public static ServifyDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ServifyDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ServifyDatabase.class, "servify_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();

    public abstract AddressDao addressDao();
}
