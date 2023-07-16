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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.farmingapp.R;

import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of crops.
 */

public class CropListAdapter extends RecyclerView.Adapter<CropListAdapter.CropViewHolder> {

    private final LayoutInflater mInflater;
    private List<Crop> mCrops; // Cached copy of crops
	private static ClickListener clickListener;

    public CropListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public CropViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.crop_recyclerview_item, parent, false);
        return new CropViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CropViewHolder holder, int position) {
        if (mCrops != null) {
            Crop currentCrop = mCrops.get(position);

            holder.cropIDView.setText(String.valueOf(currentCrop.getId()));
            holder.cropTypeView.setText(currentCrop.getType());
            holder.cropAcresView.setText(String.valueOf(currentCrop.getAcres()));
            holder.cropDateView.setText(String.valueOf(currentCrop.getDateRecorded()));


        } else {
            // Covers the case of data not being ready yet.
            holder.cropIDView.setText(R.string.no_crop);
        }
    }

    /**
     * Associates a list of crops with this adapter
    */
    public void setCrops(List<Crop> crops) {
        mCrops = crops;
        notifyDataSetChanged();
    }


    /**
     * getItemCount() is called many times, and when it is first called,
     * mCrops has not been updated (means initially, it's null, and we can't return null).
     */
    @Override
    public int getItemCount() {
        if (mCrops != null)
            return mCrops.size();
        else return 0;
    }

    /**
     * Gets the crop at a given position.
     * This method is useful for identifying which crop
     * was clicked or swiped in methods that handle user events.
     *
     * @param position The position of the crop in the RecyclerView
     * @return The crop at the given position
     */
    public Crop Crop(int position) {
        return mCrops.get(position);
    }

    class CropViewHolder extends RecyclerView.ViewHolder {
        private final TextView cropIDView;
        private final TextView cropTypeView;
        private final TextView cropAcresView;
        private final TextView cropDateView;


        private CropViewHolder(View itemView) {
            super(itemView);
            cropIDView = itemView.findViewById(R.id.animalId_TextView);
            cropTypeView = itemView.findViewById(R.id.animalType_TextView);
            cropAcresView = itemView.findViewById(R.id.animalQuantity_TextView);
            cropDateView = itemView.findViewById(R.id.animalDate_TextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        CropListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View v, int position);
    }

}
