package com.example.farmingapp.Crops;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.farmingapp.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CropList extends Fragment {

    public CropList() {
        // Required empty public constructor
    }


    public static final int NEW_CROP_ACTIVITY_REQUEST_CODE = 2;
    public static final int UPDATE_CROP_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_CROP = "extra_crop_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    public static CropViewModel mCropViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crop_list, container, false);

        // Set up the RecyclerView.
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final CropListAdapter adapter = new CropListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Set up the CropViewModel.
        mCropViewModel = ViewModelProviders.of(this).get(CropViewModel.class);
        // Get all the crops from the database
        // and associate them to the adapter.
        mCropViewModel.getAllCrops().observe(this, new Observer<List<Crop>>() {
            @Override
            public void onChanged(@Nullable final List<Crop> crops) {
                // Update the cached copy of the crops in the adapter.
                adapter.setCrops(crops);
            }
        });

        // Floating action button setup
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewCropActivity.class);
                startActivityForResult(intent, NEW_CROP_ACTIVITY_REQUEST_CODE);
            }
        });

        // Add the functionality to swipe items in the
        // RecyclerView to delete the swiped item.
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    // We are not implementing onMove() in this app.
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    // When the use swipes a crop,
                    // delete that crop from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Crop myCrop = adapter.Crop(position);
                        Toast.makeText(getContext(),
                                getString(R.string.delete_preamble) + " " +
                                        myCrop.getType(), Toast.LENGTH_LONG).show();

                        // Delete the crop.
                        mCropViewModel.deleteCrop(myCrop);
                    }
                });

        // Attach the item touch helper to the recycler view.
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new CropListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Crop crop = adapter.Crop(position);
//                launchUpdateCropActivity(crop);
            }
        });

        // Inflate the layout for this fragment.
        return view;
    }
    
    public void launchUpdateCropActivity(Crop crop) {
        Intent intent = new Intent(getContext(), NewCropActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_CROP, crop.getType());
        intent.putExtra(EXTRA_DATA_ID, crop.getId());
        startActivityForResult(intent, UPDATE_CROP_ACTIVITY_REQUEST_CODE);
    }
}