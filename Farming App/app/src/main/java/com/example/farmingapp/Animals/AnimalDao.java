/*
 * Copyright (C) 2018 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.farmingapp.Animals;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Data Access Object (DAO) for an animal.
 * Each method performs a database operation, such as inserting or deleting an animal,
 * running a DB query, or deleting all animals.
 */

@Dao
public interface AnimalDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(com.example.farmingapp.Animals.Animal animal);

    @Query("DELETE FROM animal_table")
    void deleteAll();

    @Delete
    void deleteAnimal(com.example.farmingapp.Animals.Animal animal);

    @Query("SELECT * from animal_table LIMIT 1")
    com.example.farmingapp.Animals.Animal[] getAnyAnimal();

    @Query("SELECT * from animal_table ORDER BY id ASC")
    LiveData<List<com.example.farmingapp.Animals.Animal>> getAllAnimals();

    @Update
    void update(com.example.farmingapp.Animals.Animal... animal);
}
