package com.example.dawm_tema2.database;

import android.content.Context;

import androidx.room.Room;

public class AnimalDatabaseAction {

    private Context mContext;
    private static AnimalDatabaseAction mInstance;
    private AnimalDatabase db;
    public AnimalDatabaseAction(Context ctx)
    {
        this.mContext=ctx;
        db= Room.databaseBuilder(mContext, AnimalDatabase.class,"Animals.db").fallbackToDestructiveMigration().build();

    }
    public static synchronized AnimalDatabaseAction getInstance(Context ctx)
    {
        if(mInstance==null)
        {
            mInstance=new AnimalDatabaseAction(ctx);
        }
        return mInstance;
    }
    public AnimalDatabase getDb()
    {
        return db;

    }}
