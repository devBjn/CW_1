package com.example.hikermanagement;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.myViewHolderObservation> {
    private Context context;
    private ArrayList obsId, obsText, obsTime, obsComment;
    private String obsDate, hikeId;

    Animation translate_animation;

    ObservationAdapter(Context context, ArrayList obsId, ArrayList obsText, ArrayList obsTime, ArrayList obsComment, String obsDate, String hikeId){
        this.context = context;
        this.obsId = obsId;
        this.obsText = obsText;
        this.obsTime = obsTime;
        this.obsComment = obsComment;
        this.obsDate = obsDate;
        this.hikeId = hikeId;
    }
    @NonNull
    @Override
    public ObservationAdapter.myViewHolderObservation onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.observation_view, parent, false);
        return new ObservationAdapter.myViewHolderObservation(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationAdapter.myViewHolderObservation holder, int position) {
        holder.textTV.setText(String.valueOf(obsText.get(position)));
        holder.timeTV.setText(String.valueOf(obsTime.get(position)));
        holder.commentTV.setText(String.valueOf(obsComment.get(position)));
        holder.dateTV.setText(obsDate);
    }

    @Override
    public int getItemCount() {
        if(obsId.size() > 0) {
            return obsId.size();
        } else {
            return 0;
        }
    }

    public class myViewHolderObservation extends  RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        TextView textTV, timeTV, commentTV, dateTV;
        ImageButton imageButton;
        LinearLayout observationLayout;
        public myViewHolderObservation(@NonNull View itemView) {
            super(itemView);
            textTV = itemView.findViewById(R.id.text_txt);
            timeTV = itemView.findViewById(R.id.time_txt);
            commentTV = itemView.findViewById(R.id.comment_txt);
            dateTV = itemView.findViewById(R.id.date_txt);

            imageButton = itemView.findViewById(R.id.imageButtonAction);
            imageButton.setOnClickListener(this);

            observationLayout = itemView.findViewById(R.id.observationLayout);
            translate_animation = AnimationUtils.loadAnimation(context, R.anim.translate_animation);
            observationLayout.setAnimation(translate_animation);

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
                Intent intent = new Intent(context, UpdateObservation.class);
                intent.putExtra("obsId", String.valueOf(obsId.get(getBindingAdapterPosition())));
                intent.putExtra("hikeId", hikeId);
                intent.putExtra("text", String.valueOf(obsText.get(getBindingAdapterPosition())));
                intent.putExtra("time", String.valueOf(obsTime.get(getBindingAdapterPosition())));
                intent.putExtra("comment", String.valueOf(obsComment.get(getBindingAdapterPosition())));
                intent.putExtra("date", obsDate);
                context.startActivity(intent);
                return true;
            } else {
                confirmDialog(String.valueOf(obsText.get(getBindingAdapterPosition())));
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
                    db.deleteOneObservation(String.valueOf(obsId.get(positionToDelete)));

                    obsId.remove(positionToDelete);
                    obsText.remove(positionToDelete);
                    obsComment.remove(positionToDelete);
                    obsTime.remove(positionToDelete);

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
