package com.cs407.zoomfoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cs407.zoomfoods.database.entities.WaterLog;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<WaterLog> {
    public CustomListAdapter(@NonNull Context context,@NonNull List<WaterLog> waterLogItemArrayList) {
        super(context, R.layout.list_item, waterLogItemArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent){
        WaterLog rec = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView bottle = convertView.findViewById(R.id.bottle);
        TextView titleTextView = convertView.findViewById(R.id.title);
        TextView amountTextView = convertView.findViewById(R.id.water_amount);
        TextView timeTextView = convertView.findViewById(R.id.time);
        if (rec.getTitle().length() == 0) {
            titleTextView.setText("N/A");
        }
        else{
            titleTextView.setText(rec.getTitle());
        }
        if (rec.getAmount().length() == 0) amountTextView.setText("N/A");
        else amountTextView.setText(rec.getAmount()+" fl oz");
        if (rec.getTime().length() == 0) timeTextView.setText("N/A");
        else timeTextView.setText(rec.getTime());
        return convertView;
    }
}
