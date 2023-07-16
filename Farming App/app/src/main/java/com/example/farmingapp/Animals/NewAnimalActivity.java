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

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.example.farmingapp.Animals.AnimalList.EXTRA_DATA_ID;
import static com.example.farmingapp.Animals.AnimalList.EXTRA_DATA_UPDATE_ANIMAL;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmingapp.R;

import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * This class displays a screen where the user enters a new animal.
 * The NewAnimalActivity returns the entered animal to the calling activity
 * (MainActivity), which then stores the new animal and updates the list of
 * displayed animals.
 */
public class NewAnimalActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.android.example.farmingapp.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.farmingapp.REPLY_ID";

    private EditText mEditAnimalQtyView;
    private EditText mEditAnimalTypeView;
    public DatePicker mAnimalDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_animals);

        mEditAnimalQtyView = findViewById(R.id.quantity_EditText);
        mEditAnimalTypeView = findViewById(R.id.type_EditText);
        mAnimalDate = findViewById(R.id.datePickerAnimal);

        int id = -1;


        final Bundle extras = getIntent().getExtras();


        final Button button = findViewById(R.id.button_save);

        // When the user presses the Save button, create a new Intent for the reply.
        // The reply Intent will be sent back to the calling activity (in this case, MainActivity).
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Create a new Intent for the reply.
                Intent replyIntent = new Intent();
                int day = mAnimalDate.getDayOfMonth();
                int month = mAnimalDate.getMonth() + 1;
                int year = mAnimalDate.getYear();
                String date = year + "-" + month + "-" + day;
                // Get the new animal that the user entered.
                String animalType = mEditAnimalTypeView.getText().toString();
                String animalQty = mEditAnimalQtyView.getText().toString();
                Animal animal = new Animal(animalType, Integer.parseInt(animalQty), date);

                addEventToCalendar(animalType, animalQty, year, month, day);

                // Put the new animal in the extras for the reply Intent.
                replyIntent.putExtra(EXTRA_REPLY, animal.toString());
                if (extras != null && extras.containsKey(EXTRA_DATA_ID)) {
                    int id = extras.getInt(EXTRA_DATA_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(EXTRA_REPLY_ID, id);
                    }
                }
                // Set the result status to indicate success.
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }

    private void addEventToCalendar(String animalType, String animalQty, int year, int month, int date) {

        Calendar start = Calendar.getInstance();
        start.set(year, month, date);

        //Code from https://www.youtube.com/watch?v=NK_-phxyIAM
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Animal Inventory");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Farm");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Type: " + animalType + "\nQuantity: " + animalQty);
        intent.putExtra(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
        startActivity(intent);

    }
}
