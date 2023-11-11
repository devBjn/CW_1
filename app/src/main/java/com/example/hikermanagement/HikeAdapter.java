package com.example.hikermanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.myViewHolder> implements Filterable {
    private Context context;
    private ArrayList id, name, location, length, date, description, difficulty, isParking;
    private ArrayList filteredIds,filteredNames, filteredLocations, filteredDates, filteredIsParking, filteredLengths,filteredDifficulties, filteredDescriptions ;
    private ArrayList originalIds,originalNames, originalLocations, originalDates, originalIsParking, originalLengths,originalDifficulties, originalDescriptions ;
    Animation translate_animation;
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
        this.originalIds=id;
        this.originalNames = name;
        this.originalLocations = location;
        this.originalLengths = length;
        this.originalDates = date;
        this.originalDifficulties = difficulty;
        this.originalDescriptions = description;
        this.originalIsParking = isParking;
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
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("hikeId", String.valueOf(id.get(holder.getBindingAdapterPosition())));
                intent.putExtra("name", String.valueOf(name.get(holder.getBindingAdapterPosition())));
                intent.putExtra("location", String.valueOf(location.get(holder.getBindingAdapterPosition())));
                intent.putExtra("date", String.valueOf(date.get(holder.getBindingAdapterPosition())));
                intent.putExtra("parkingAvailable", String.valueOf(isParking.get(holder.getBindingAdapterPosition())));
                intent.putExtra("length", String.valueOf(length.get(holder.getBindingAdapterPosition())));
                intent.putExtra("difficulty", String.valueOf(difficulty.get(holder.getBindingAdapterPosition())));
                intent.putExtra("description", String.valueOf(description.get(holder.getBindingAdapterPosition())));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(id.size() > 0) {
            return id.size();
        } else {
            return 0;
        }
    }

    public class myViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView idTV, nameTV, locationTV, lengthTV, dateTV, descriptionTV, difficultyTV, isParkingTV;
        ImageButton imageButton;
        LinearLayout mainLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            idTV = itemView.findViewById(R.id.hikeId);
            nameTV = itemView.findViewById(R.id.name_txt);
            locationTV = itemView.findViewById(R.id.location_txt);
            difficultyTV = itemView.findViewById(R.id.difficulty_txt);
            dateTV = itemView.findViewById(R.id.date_txt);

            imageButton = itemView.findViewById(R.id.imageButtonHike);
            imageButton.setOnClickListener(this);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            mainLayout.setAnimation(translate_animation);

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
            int itemId = menuItem.getItemId();

            if (itemId == R.id.action_popup_edit) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf(id.get(getBindingAdapterPosition())));
                intent.putExtra("name", String.valueOf(name.get(getBindingAdapterPosition())));
                intent.putExtra("location", String.valueOf(location.get(getBindingAdapterPosition())));
                intent.putExtra("date", String.valueOf(date.get(getBindingAdapterPosition())));
                intent.putExtra("parkingAvailable", String.valueOf(isParking.get(getBindingAdapterPosition())));
                intent.putExtra("length", String.valueOf(length.get(getBindingAdapterPosition())));
                intent.putExtra("difficulty", String.valueOf(difficulty.get(getBindingAdapterPosition())));
                intent.putExtra("description", String.valueOf(description.get(getBindingAdapterPosition())));
                context.startActivity(intent);
                return true;
            } else {
                confirmDialog(String.valueOf(name.get(getBindingAdapterPosition())));
                return true;
            }
        }

        void confirmDialog(String nameHike) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete " + nameHike);
            builder.setMessage("Are you sure you want to delete " + nameHike + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseHelper db = new DatabaseHelper(context);
                    int positionToDelete = getBindingAdapterPosition();
                    db.deleteOne(String.valueOf(id.get(positionToDelete)));

                    id.remove(positionToDelete);
                    name.remove(positionToDelete);
                    location.remove(positionToDelete);
                    date.remove(positionToDelete);
                    isParking.remove(positionToDelete);
                    length.remove(positionToDelete);
                    difficulty.remove(positionToDelete);
                    description.remove(positionToDelete);

                    notifyItemRemoved(positionToDelete);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String searchText = charSequence.toString().toLowerCase().trim();

                if (searchText.isEmpty()) {
                    filteredIds = new ArrayList(originalIds);
                    filteredNames = new ArrayList(originalNames);
                    filteredLocations = new ArrayList(originalLocations);
                    filteredDates = new ArrayList(originalDates);
                    filteredIsParking = new ArrayList(originalIsParking);
                    filteredLengths = new ArrayList(originalLengths);
                    filteredDifficulties = new ArrayList(originalDifficulties);
                    filteredDescriptions = new ArrayList(originalDescriptions);
                } else {
                    initializeFilteredLists();
                    for (int i = 0; i < id.size(); i++) {
                        if (name.get(i).toString().toLowerCase().contains(searchText)) {
                            filteredIds.add(id.get(i));
                            filteredNames.add(name.get(i));
                            filteredLocations.add(location.get(i));
                            filteredDates.add(date.get(i));
                            filteredIsParking.add(isParking.get(i));
                            filteredLengths.add(length.get(i));
                            filteredDifficulties.add(difficulty.get(i));
                            filteredDescriptions.add(description.get(i));
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredIds;
                filterResults.count = filteredIds.size();
                Log.d(String.valueOf(filterResults.count), "size");
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.count > 0) {
                    id =(ArrayList) filterResults.values;
                    name = filteredNames;
                    location = filteredLocations;
                    date = filteredDates;
                    isParking = filteredIsParking;
                    length = filteredLengths;
                    difficulty = filteredDifficulties;
                    description = filteredDescriptions;
                } else {
                    Log.d(String.valueOf(id.size()), "size");
                    id = new ArrayList();
                    name = new ArrayList();
                    location = new ArrayList();
                    date = new ArrayList();
                    isParking = new ArrayList();
                    length = new ArrayList();
                    difficulty = new ArrayList();
                    description = new ArrayList();
                }

                notifyDataSetChanged();
            }

            private void initializeFilteredLists() {
                filteredIds = new ArrayList();
                filteredNames = new ArrayList();
                filteredLocations = new ArrayList();
                filteredDates = new ArrayList();
                filteredIsParking = new ArrayList();
                filteredLengths = new ArrayList();
                filteredDifficulties = new ArrayList();
                filteredDescriptions = new ArrayList();
            }
        };
    }
}

