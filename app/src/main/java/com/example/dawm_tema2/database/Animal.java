package com.example.dawm_tema2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "animale")
public class Animal {
    @PrimaryKey(autoGenerate = true)
    public int animalID;
    @ColumnInfo(name="animal_name")
    public String animalName;
    @ColumnInfo(name="continent_name")
    public String continentName;
    public Animal(String animalName, String continentName)
    {
        this.animalName=animalName;
        this.continentName=continentName;
    }
    public Animal()
    {

    }
    public int getAnimalID() {
        return animalID;
    }

    public void setAnimalID(int animalID) {
        this.animalID = animalID;
    }

    public String getAnimalName() {
        return animalName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }
}

