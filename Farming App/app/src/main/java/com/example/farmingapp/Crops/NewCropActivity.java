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

import static com.example.farmingapp.Crops.CropList.EXTRA_DATA_ID;
import static com.example.farmingapp.Crops.CropList.EXTRA_DATA_UPDATE_CROP;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.farmingapp.Animals.Animal;
import com.example.farmingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class displays a screen where the user enters a new crop.
 * The NewCropActivity returns the entered crop to the calling activity
 * (MainActivity), which then stores the new crop and updates the list of
 * displayed crops.
 */
public class NewCropActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.android.example.farmingapp.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.farmingapp.REPLY_ID";


    private EditText mEditCropTypeView;
    private EditText mEditCropAcresView;
    public DatePicker mCropDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_crops);

        mEditCropTypeView = findViewById(R.id.type_EditText);
        mEditCropAcresView = findViewById(R.id.acres_EditText);
        mCropDate = findViewById(R.id.datePickerCrop);

        int id = -1;

        final Bundle extras = getIntent().getExtras();


        final Button button = findViewById(R.id.button_save);

        // When the user presses the Save button, create a new Intent for the reply.
        // The reply Intent will be sent back to the calling activity (in this case, MainActivity).
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Create a new Intent for the reply.
                Intent replyIntent = new Intent();
                int day = mCropDate.getDayOfMonth();
                int month = mCropDate.getMonth() + 1;
                int year = mCropDate.getYear();

                String date =  year + "-" + month + "-" + day;
                    // Get the new crop that the user entered.
                String cropType = mEditCropTypeView.getText().toString();
                String cropAcres = mEditCropAcresView.getText().toString();
                Crop crop = new Crop(cropType, Integer.parseInt(cropAcres), date);

                addEventToCalendar(cropType, cropAcres, year, month, day);

                // Put the new crop in the extras for the reply Intent.
                replyIntent.putExtra(EXTRA_REPLY, crop.toString());
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

    private void addEventToCalendar(String cropType, String cropAcres, int year, int month, int date) {

        Calendar start = Calendar.getInstance();
        start.set(year, month, date);

        //Code from https://www.youtube.com/watch?v=NK_-phxyIAM
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, "Crop Inventory");
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "My Farm");
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Type: " + cropType + "\nAcres: " + cropAcres);
        intent.putExtra(CalendarContract.Events.DTSTART, start.getTimeInMillis());
        intent.putExtra(CalendarContract.Events.ALL_DAY, "true");
        startActivity(intent);

    }
}
