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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmingapp.R;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of animals.
 */

public class AnimalListAdapter extends RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder> {

    private final LayoutInflater mInflater;
    private List<Animal> mAnimals; // Cached copy of animals
	private static ClickListener clickListener;

    public AnimalListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AnimalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.animal_recyclerview_item, parent, false);
        return new AnimalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AnimalViewHolder holder, int position) {
        if (mAnimals != null) {
            Animal currentAnimal = mAnimals.get(position);

            holder.animalIDView.setText(String.valueOf(currentAnimal.getId()));
            holder.animalTypeView.setText(currentAnimal.getType());
            holder.animalQuantityView.setText(String.valueOf(currentAnimal.getQuantity()));
            holder.animalDateView.setText(String.valueOf(currentAnimal.getDateRecorded()));


        } else {
            // Covers the case of data not being ready yet.
            holder.animalIDView.setText(R.string.no_animal);
        }
    }

    /**
     * Associates a list of animals with this adapter
    */
    public void setAnimals(List<Animal> animals) {
        mAnimals = animals;
        notifyDataSetChanged();
    }


    /**
     * getItemCount() is called many times, and when it is first called,
     * mAnimals has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mAnimals != null)
            return mAnimals.size();
        else return 0;
    }

    /**
     * Gets the animal at a given position.
     * This method is useful for identifying which animal
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the animal in the RecyclerView
     * @return The animal at the given position
     */
    public Animal Animal(int position) {
        return mAnimals.get(position);
    }

    class AnimalViewHolder extends RecyclerView.ViewHolder {
        private final TextView animalIDView;
        private final TextView animalTypeView;
        private final TextView animalQuantityView;
        private final TextView animalDateView;


        private AnimalViewHolder(View itemView) {
            super(itemView);
            animalIDView = itemView.findViewById(R.id.animalId_TextView);
            animalTypeView = itemView.findViewById(R.id.animalType_TextView);
            animalQuantityView = itemView.findViewById(R.id.animalQuantity_TextView);
            animalDateView = itemView.findViewById(R.id.animalDate_TextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AnimalListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
