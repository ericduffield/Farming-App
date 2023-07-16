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

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.farmingapp.RoomDatabaseImpl;

import java.util.List;

/**
 * This class holds the implementation code for the methods that interact with the database.
 * Using a repository allows us to group the implementation methods together,
 * and allows the AnimalViewModel to be a clean interface between the rest of the app
 * and the database.
 *
 * For insert, update and delete, and longer-running queries,
 * you must run the database interaction methods in the background.
 *
 * Typically, all you need to do to implement a database method
 * is to call it on the data access object (DAO), in the background if applicable.
 */

public class AnimalRepository {

    private com.example.farmingapp.Animals.AnimalDao mAnimalDao;
    private LiveData<List<com.example.farmingapp.Animals.Animal>> mAllAnimals;

    public AnimalRepository(Application application) {
        RoomDatabaseImpl db = RoomDatabaseImpl.getAnimalDatabase(application);
        mAnimalDao = db.animalDao();
        mAllAnimals = mAnimalDao.getAllAnimals();
    }

    public LiveData<List<com.example.farmingapp.Animals.Animal>> getAllAnimals() {
        return mAllAnimals;
    }

    public void insert(com.example.farmingapp.Animals.Animal animal) {
        new insertAsyncTask(mAnimalDao).execute(animal);
    }

    public void update(Animal animal)  {
        new updateAnimalAsyncTask(mAnimalDao).execute(animal);
    }

    public void deleteAll()  {
        new deleteAllAnimalsAsyncTask(mAnimalDao).execute();
    }

    // Must run off main thread
    public void deleteAnimal(Animal animal) {
        new deleteAnimalAsyncTask(mAnimalDao).execute(animal);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts an animal into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Animal, Void, Void> {

        private AnimalDao mAsyncTaskDao;

        insertAsyncTask(AnimalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all animals from the database (does not delete the table).
     */
    private static class deleteAllAnimalsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AnimalDao mAsyncTaskDao;

        deleteAllAnimalsAsyncTask(AnimalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single animal from the database.
     */
    private static class deleteAnimalAsyncTask extends AsyncTask<Animal, Void, Void> {
        private AnimalDao mAsyncTaskDao;

        deleteAnimalAsyncTask(AnimalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            mAsyncTaskDao.deleteAnimal(params[0]);
            return null;
        }
    }

    /**
     *  Updates a animal in the database.
     */
    private static class updateAnimalAsyncTask extends AsyncTask<Animal, Void, Void> {
        private AnimalDao mAsyncTaskDao;

        updateAnimalAsyncTask(AnimalDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Animal... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
