package com.example.hikermanagement;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.myViewHolder>{
    private Context context;
    private ArrayList id, name, location, length, date, description, difficulty, isParking;
    HikeAdapter(Context context, ArrayList id, ArrayList name, ArrayList location, ArrayList date, ArrayList isParking, ArrayList length, ArrayList difficulty, ArrayList description){
        this.context = context;
        this.id = id;
        this.name = name;
        this.location = location;
        this.length = length;
        this.date = date;
        this.difficulty = difficulty;
        this.description = description;
        this.isParking = isParking;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hike_view, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.idTV.setText(String.valueOf(id.get(position)));
        holder.nameTV.setText(String.valueOf(name.get(position)));
        holder.locationTV.setText(String.valueOf(location.get(position)));
        holder.dateTV.setText(String.valueOf(date.get(position)));
        holder.difficultyTV.setText(String.valueOf(difficulty.get(position)));
//        holder.lengthTV.setText(String.valueOf(length.get(position)));
//        holder.descriptionTV.setText(String.valueOf(description.get(position)));
//        holder.isParkingTV.setText(String.valueOf(isParking.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class myViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView idTV, nameTV, locationTV, lengthTV, dateTV, descriptionTV, difficultyTV, isParkingTV;
        ImageButton imageButton;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.hikeId);
            nameTV = itemView.findViewById(R.id.name_txt);
            locationTV = itemView.findViewById(R.id.location_txt);
            difficultyTV = itemView.findViewById(R.id.difficulty_txt);
            dateTV = itemView.findViewById(R.id.date_txt);
//            lengthTV = itemView.findViewById(R.id.length_txt);
//            descriptionTV = itemView.findViewById(R.id.description_txt);
//            isParkingTV = itemView.findViewById(R.id.isParking_txt);

            imageButton = itemView.findViewById(R.id.imageButton);
            imageButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            showPopupMenu(view);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_popup_edit:
                    return true;

                case R.id.action_popup_delete:

                    return true;

                default:
                    return false;
            }

            return false;
        }
    }
}
