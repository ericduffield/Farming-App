package com.example.farmingapp.Animals;

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
public class AnimalList extends Fragment {

    public AnimalList() {
        // Required empty public constructor
    }


    public static final int NEW_ANIMAL_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_ANIMAL_ACTIVITY_REQUEST_CODE = 2;

    public static final String EXTRA_DATA_UPDATE_ANIMAL = "extra_animal_to_be_updated";
    public static final String EXTRA_DATA_ID = "extra_data_id";

    static public AnimalViewModel mAnimalViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_animal_list, container, false);

        // Set up the RecyclerView.
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        final AnimalListAdapter adapter = new AnimalListAdapter(this.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        // Set up the AnimalViewModel.
        mAnimalViewModel = ViewModelProviders.of(this).get(AnimalViewModel.class);
        // Get all the animals from the database
        // and associate them to the adapter.
        mAnimalViewModel.getAllAnimals().observe(this, new Observer<List<Animal>>() {
            @Override
            public void onChanged(@Nullable final List<Animal> animals) {
                // Update the cached copy of the animals in the adapter.
                adapter.setAnimals(animals);
            }
        });

        // Floating action button setup
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), NewAnimalActivity.class);
                startActivityForResult(intent, NEW_ANIMAL_ACTIVITY_REQUEST_CODE);
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
                    // When the use swipes a animal,
                    // delete that animal from the database.
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Animal myAnimal = adapter.Animal(position);
                        Toast.makeText(getContext(),
                                getString(R.string.delete_preamble) + " " +
                                        myAnimal.getType(), Toast.LENGTH_LONG).show();

                        // Delete the animal.
                        mAnimalViewModel.deleteAnimal(myAnimal);
                    }
                });

        // Attach the item touch helper to the recycler view.
        helper.attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new AnimalListAdapter.ClickListener()  {

            @Override
            public void onItemClick(View v, int position) {
                Animal animal = adapter.Animal(position);
//                launchUpdateAnimalActivity(animal);
            }
        });

        // Inflate the layout for this fragment.
        return view;
    }
    
    public void launchUpdateAnimalActivity(Animal animal) {
        Intent intent = new Intent(getContext(), NewAnimalActivity.class);
        intent.putExtra(EXTRA_DATA_UPDATE_ANIMAL, animal.getType());
        intent.putExtra(EXTRA_DATA_ID, animal.getId());
        startActivityForResult(intent, UPDATE_ANIMAL_ACTIVITY_REQUEST_CODE);
    }
}