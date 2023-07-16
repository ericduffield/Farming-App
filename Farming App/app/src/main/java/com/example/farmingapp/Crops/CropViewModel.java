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
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * The CustomerViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class CropViewModel extends AndroidViewModel {

    private CropRepository mRepository;

    private LiveData<List<Crop>> mAllCrops;

    public CropViewModel(Application application) {
        super(application);
        mRepository = new CropRepository(application);
        mAllCrops = mRepository.getAllCrops();
    }

    public LiveData<List<Crop>> getAllCrops() {
        return mAllCrops;
    }

    public void insert(Crop crop) {
        mRepository.insert(crop);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteCrop(Crop crop) {
        mRepository.deleteCrop(crop);
    }

    public void update(Crop crop) {
        mRepository.update(crop);
    }

}
