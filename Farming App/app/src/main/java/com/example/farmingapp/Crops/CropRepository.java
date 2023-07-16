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

package com.example.farmingapp.Crops;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.farmingapp.RoomDatabaseImpl;

import java.util.List;

/**
 * This class holds the implementation code for the methods that interact with the database.
 * Using a repository allows us to group the implementation methods together,
 * and allows the CropViewModel to be a clean interface between the rest of the app
 * and the database.
 *
 * For insert, update and delete, and longer-running queries,
 * you must run the database interaction methods in the background.
 *
 * Typically, all you need to do to implement a database method
 * is to call it on the data access object (DAO), in the background if applicable.
 */

public class CropRepository {

    private CropDao mCropDao;
    private LiveData<List<Crop>> mAllCrops;

    public CropRepository(Application application) {
        RoomDatabaseImpl db = RoomDatabaseImpl.getCropDatabase(application);
        mCropDao = db.cropDao();
        mAllCrops = mCropDao.getAllCrops();
    }

    public LiveData<List<Crop>> getAllCrops() {
        return mAllCrops;
    }

    public void insert(Crop crop) {
        new insertAsyncTask(mCropDao).execute(crop);
    }

    public void update(Crop crop)  {
        new updateCropAsyncTask(mCropDao).execute(crop);
    }

    public void deleteAll()  {
        new deleteAllCropsAsyncTask(mCropDao).execute();
    }

    // Must run off main thread
    public void deleteCrop(Crop crop) {
        new deleteCropAsyncTask(mCropDao).execute(crop);
    }

    // Static inner classes below here to run database interactions in the background.

    /**
     * Inserts an crop into the database.
     */
    private static class insertAsyncTask extends AsyncTask<Crop, Void, Void> {

        private CropDao mAsyncTaskDao;

        insertAsyncTask(CropDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Crop... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    /**
     * Deletes all crops from the database (does not delete the table).
     */
    private static class deleteAllCropsAsyncTask extends AsyncTask<Void, Void, Void> {
        private CropDao mAsyncTaskDao;

        deleteAllCropsAsyncTask(CropDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    /**
     *  Deletes a single crop from the database.
     */
    private static class deleteCropAsyncTask extends AsyncTask<Crop, Void, Void> {
        private CropDao mAsyncTaskDao;

        deleteCropAsyncTask(CropDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Crop... params) {
            mAsyncTaskDao.deleteCrop(params[0]);
            return null;
        }
    }

    /**
     *  Updates a crop in the database.
     */
    private static class updateCropAsyncTask extends AsyncTask<Crop, Void, Void> {
        private CropDao mAsyncTaskDao;

        updateCropAsyncTask(CropDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Crop... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }
}
