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

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.sql.Date;

/**
 * Entity class that represents a animal in the database
 */

@Entity(tableName = "animal_table")
public class Animal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "animal_type")
    private String type;

    @NonNull
    @ColumnInfo(name = "quantity")
    private int quantity;

    @NonNull
    @ColumnInfo(name = "date_recorded")
    private String dateRecorded;

    public Animal(@NonNull String type, @NonNull int quantity, @NonNull String dateRecorded) {
        this.type = type;
        this.quantity = quantity;
        this.dateRecorded = dateRecorded;
    }

    /*
    * This constructor is annotated using @Ignore, because Room expects only
    * one constructor by default in an entity class.
    */

    @Ignore
    public Animal(int id, @NonNull String type, @NonNull int quantity, @NonNull String dateRecorded) {
        this.id = id;
        this.type = type;
        this.quantity = quantity;
        this.dateRecorded = dateRecorded;
    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) { this.type = type;}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @NonNull
    public String getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(@NonNull String dateRecorded) {
        this.dateRecorded = dateRecorded;
    }

    @Override
    public String toString() {
        return "Animal," + this.type + "," + this.quantity + "," + this.dateRecorded;
    }
}
