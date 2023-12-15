package com.cs407.zoomfoods;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.cs407.zoomfoods.database.entities.ReminderItem;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{


    private List<ReminderItem> reminderItemList;
    private SelectListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewReminder;
        private Switch switchReminder;
        private LinearLayout reminder_container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewReminder = itemView.findViewById(R.id.reminderTime);
            switchReminder = itemView.findViewById(R.id.switch_reminder);
            reminder_container = itemView.findViewById(R.id.reminder_container);
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param reminderItemList containing the data to populate views to be used
     * by RecyclerView
     */
    public ReminderAdapter(List<ReminderItem> reminderItemList, SelectListener listener){
        this.reminderItemList = reminderItemList;
        this.listener = listener;

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_items, parent, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReminderItem item = reminderItemList.get(position);
        int current_position = position;
        holder.textViewReminder.setText(item.getTime());
        holder.switchReminder.setChecked(item.getIsActive());
        holder.reminder_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(item, current_position);
            }
        });
        // Set action long press
        holder.reminder_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClicked(item, current_position);
                return true;
            }
        });
        // Set a change listener for the switch
        holder.switchReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSwitchClicked(item, current_position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderItemList.size();
    }
}

