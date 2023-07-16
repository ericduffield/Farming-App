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
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * The CustomerViewModel provides the interface between the UI and the data layer of the app,
 * represented by the Repository
 */

public class AnimalViewModel extends AndroidViewModel {

    private AnimalRepository mRepository;

    private LiveData<List<Animal>> mAllAnimals;

    public AnimalViewModel(Application application) {
        super(application);
        mRepository = new AnimalRepository(application);
        mAllAnimals = mRepository.getAllAnimals();
    }

    public LiveData<List<Animal>> getAllAnimals() {
        return mAllAnimals;
    }

    public void insert(Animal animal) {
        mRepository.insert(animal);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public void deleteAnimal(Animal animal) {
        mRepository.deleteAnimal(animal);
    }

    public void update(Animal animal) {
        mRepository.update(animal);
    }

}
