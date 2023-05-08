package com.example.dawm_tema2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AnimalDao {
    @Query("SELECT *FROM animale")
    List<Animal> getAllAnimals();
    @Delete
    void deleteAnimal(Animal animal);
    @Insert
    void insertAnimal(Animal animals);
}
