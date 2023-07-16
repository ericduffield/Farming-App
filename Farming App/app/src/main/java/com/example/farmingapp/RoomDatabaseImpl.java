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

package com.example.farmingapp;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.farmingapp.Animals.Animal;
import com.example.farmingapp.Animals.AnimalDao;
import com.example.farmingapp.Crops.Crop;
import com.example.farmingapp.Crops.CropDao;

import java.sql.Date;

/**
 * AnimalRoomDatabase. Includes code to create the database.
 * After the app creates the database, all further interactions
 * with it happen through the different view models.
 */

@Database(entities = {Animal.class, Crop.class }, version = 12, exportSchema = false)
public abstract class RoomDatabaseImpl extends RoomDatabase {

    public abstract AnimalDao animalDao();
    public abstract CropDao cropDao();

    private static RoomDatabaseImpl INSTANCE;

    public static RoomDatabaseImpl getAnimalDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabaseImpl.class) {
                if (INSTANCE == null) {
                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabaseImpl.class, "animal_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static RoomDatabaseImpl getCropDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDatabaseImpl.class) {
                if (INSTANCE == null) {
                    // Create database here.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    RoomDatabaseImpl.class, "crop_database")
                            // Wipes and rebuilds instead of migrating if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    // This callback is called when the database has opened.
    // In this case, use PopulateDbAsync to populate the database
    // with the initial data set if the database has no entries.
    private static Callback sRoomDatabaseCallback =
            new Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    // Populate the database with the initial data set
    // only if the database has no entries.
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AnimalDao mDao;
        private final CropDao cDao;


        // Initial animal data set
        private static String [] animalTypes = {"Cows", "Pigs", "Sheep", "Chickens", "Goats", "Horses", "Duck", "Geese", "Donkeys"};
        private static int [] animalQuantities = {45, 24, 65, 23, 12, 17, 10, 7, 2};
        private static String [] animalDates = {"2022-11-25", "2022-11-28", "2022-12-01", "2022-12-09", "2022-12-12", "2022-12-15", "2022-12-18", "2022-12-21", "2022-12-24"};

        // Initial crop data set
        private static String [] cropTypes = {"Wheat", "Barley", "Rye", "Oats", "Maize", "Potatoes", "Sugar Beet", "Corn"};
        private static int [] cropQuantities = {23, 63, 94, 35, 45, 65, 34, 12};
        private static String [] cropDates = {"2022-11-25", "2022-11-28", "2022-12-01", "2022-12-09", "2022-12-12", "2022-12-15", "2022-12-18", "2022-12-21"};
        PopulateDbAsync(RoomDatabaseImpl db) {
            mDao = db.animalDao();
            cDao = db.cropDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If we have no animals, then create the initial list of animals.
            if (mDao.getAnyAnimal().length < 1) {
                for (int i = 0; i <= animalTypes.length - 1; i++) {
                    Animal animal = new Animal(animalTypes[i], animalQuantities[i], animalDates[i]);
                    mDao.insert(animal);
                }
            }

            if (cDao.getAnyCrop().length < 1) {
                for (int i = 0; i <= cropTypes.length - 1; i++) {
                    Crop crop = new Crop(cropTypes[i], cropQuantities[i], cropDates[i]);
                    cDao.insert(crop);
                }
            }

            return null;
        }
    }
}

