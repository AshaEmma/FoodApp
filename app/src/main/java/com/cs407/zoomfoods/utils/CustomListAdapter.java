package com.cs407.zoomfoods.utils;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cs407.zoomfoods.R;

import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends ArrayAdapter<Record> {
    private Context mContext;
    private List<Record> recordList;
    public CustomListAdapter(@NonNull Context context,@NonNull ArrayList<Record> recordArrayList) {
        super(context, R.layout.list_item, recordArrayList);
        mContext = context;
        recordList = recordArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent){
        Record rec = getItem(position);
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
