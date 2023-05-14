package com.example.dawm_tema2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Animal.class},version=3, exportSchema = true)
public abstract class AnimalDatabase extends RoomDatabase {
    public abstract AnimalDao animalDao();
    private static volatile AnimalDatabase INSTANCE;
    public static AnimalDatabase getInstance(Context context)
    {
        if(INSTANCE==null)
        {
            synchronized (AnimalDatabase.class)
            {
                if(INSTANCE==null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            AnimalDatabase.class,
                            "animale").build();

                }
            }
        }
        return INSTANCE;
    }
    private static volatile AnimalDatabase db;
    public static AnimalDatabase getDb()
    {
        return db;
    }
}

