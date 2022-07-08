package com.example.server_cl_com_emtyactivity.LocalDatabaseFiles;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.DAoFiles.massege_Dao;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.loginDetails_entity;
import com.example.server_cl_com_emtyactivity.LocalDatabaseFiles.entities.massege_entity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {massege_entity.class, loginDetails_entity.class}, version = 1)
public abstract class MainDatabaseClass extends RoomDatabase {
    public abstract massege_Dao MassegeDao();


    private static volatile MainDatabaseClass INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static MainDatabaseClass getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MainDatabaseClass.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MainDatabaseClass.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
