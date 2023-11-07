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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.myViewHolder>{
    private Context context;
    private ArrayList id, name, location, length, date, description, difficulty, isParking;

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
        LinearLayout mainLayout;
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

        void confirmDialog(String name) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete " + name);
            builder.setMessage("Are you sure you want to delete " + name + " ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseHelper db = new DatabaseHelper(context);
                    int positionToDelete = getBindingAdapterPosition();
                    db.deleteOne(String.valueOf(id.get(getBindingAdapterPosition())));
                    db.deleteOne(String.valueOf(id.get(positionToDelete)));
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
}
