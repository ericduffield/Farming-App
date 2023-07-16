package com.example.farmingapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.farmingapp.Animals.Animal;
import com.example.farmingapp.Crops.Crop;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_CROP_ACTIVITY_REQUEST_CODE = 2;
    public static final String EXTRA_REPLY = "com.android.example.farmingapp.REPLY";
    public static final String EXTRA_REPLY_ID = "com.android.example.farmingapp.REPLY_ID";
    public static final String EXTRA_DATA_UPDATE_WORD = "extra_word_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar =
                findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create an instance of the tab layout from the view.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // Set the text for each tab.
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_animal));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_action_crop));
        // Set the tabs to fill the entire layout.
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Use PagerAdapter to manage page views in fragments.
        // Each page is represented by its own fragment.
        final ViewPager viewPager = findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);


        // Setting a listener for clicks.
        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new
       TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem(tab.getPosition());
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {
           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {
           }
       });

        reminderNotification();
    }

    public void reminderNotification()
    {
        NotificationUtils _notificationUtils = new NotificationUtils(this);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 3);
        calendar.set(Calendar.SECOND, 1);

        _notificationUtils.setReminder(calendar.getTimeInMillis());
    }


    /**
     * When the user enters a new word in the NewWordActivity,
     * that activity returns the result to this activity.
     * If the user entered a new word, save it in the database.

     * @param requestCode ID for the request
     * @param resultCode indicates success or failure
     * @param data The Intent sent back from the NewWordActivity,
     *             which includes the word that the user entered
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String commas = data.getStringExtra(EXTRA_REPLY);
            String[] split = commas.split(",");

            if (Objects.equals(split[0], "Animal")){
                // Save the data.
                int qty = Integer.parseInt(split[2]);
                Animal animal = new Animal(split[1],qty,split[3]);
                com.example.farmingapp.Animals.AnimalList.mAnimalViewModel.insert(animal);
            }else {
                int acres = Integer.parseInt(split[2]);
                Crop crop = new Crop(split[1],acres,split[3]);
                com.example.farmingapp.Crops.CropList.mCropViewModel.insert(crop);
            }


        } else {
            Toast.makeText(
                    this, "Error adding item. Please try again.", Toast.LENGTH_LONG).show();
        }
    }

}
