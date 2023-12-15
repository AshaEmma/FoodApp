package com.cs407.zoomfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs407.zoomfoods.services.UserSessionService;

import java.util.ArrayList;


public class FridgeActivity extends AppCompatActivity {
    private ArrayList<FoodItem> fridgeItems;
    private FridgeItemAdapter itemsAdapter;
    private ListView listView;
    private EditText editText;
    private Button search;
    private Button addProduct;
    long userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);
        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();

        fridgeItems = new ArrayList<>();
        listView = findViewById(R.id.listView);
        editText = findViewById(R.id.editTextItem);
        search = findViewById(R.id.buttonSearch);
        addProduct = findViewById(R.id.buttonAdd);

        itemsAdapter = new FridgeItemAdapter(this, fridgeItems);
        listView.setAdapter(itemsAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            FoodItem selectedItem = fridgeItems.get(position);
            showItemDetails(selectedItem);
        });

        addProduct.setOnClickListener(v -> gotoAddActivity());
        search.setOnClickListener(v -> onSearchClicked());
    }

    private void onSearchClicked() {
        String searchQuery = editText.getText().toString().trim();

        boolean itemFound = false;
        for (FoodItem item : fridgeItems) {
            if (item.getName().equalsIgnoreCase(searchQuery)) {
                itemFound = true;
                break;
            }
        }

        if (itemFound) {
            Toast.makeText(this, searchQuery + " is in the fridge!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, searchQuery + " is not in the fridge.", Toast.LENGTH_SHORT).show();
        }
    }


    // method for camera button click
//    public void onCameraClicked(View view) {
//        Intent intent = new Intent(this, ImageRecognitionActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String name = data.getStringExtra("ITEM");
            String category = data.getStringExtra("CATEGORY");
            String quantity = data.getStringExtra("QUANTITY");
            String unit = data.getStringExtra("UNIT");
            String storageType = data.getStringExtra("STORAGE_TYPE");
            String expiryDate = data.getStringExtra("EXPIRY_DATE");

            FoodItem newItem = new FoodItem(name, category, quantity, unit, storageType, expiryDate);
            fridgeItems.add(newItem);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void showItemDetails(FoodItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(item.getName());

        String message = "Category: " + item.getCategory() +
                "\nQuantity: " + item.getQuantity() + " " + item.getUnit() +
                "\nStorage: " + item.getStorageType() +
                "\nExpiry Date: " + item.getExpiryDate();

        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void gotoAddActivity() {
        Intent intent = new Intent(this, AddProductActivity.class);
        startActivity(intent);
    }

    public static class FridgeItemAdapter extends ArrayAdapter<FoodItem> {
        public FridgeItemAdapter(Context context, ArrayList<FoodItem> items) {
            super(context, 0, items);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            FoodItem item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_fridge, parent, false);
            }

            TextView itemName = convertView.findViewById(R.id.textViewItemName);
            TextView itemDetails = convertView.findViewById(R.id.textViewDetails);
            ImageView itemIcon = convertView.findViewById(R.id.imageView); // Reference to the ImageView

            assert item != null;
            itemName.setText(item.getName());
            String details = "Quantity: " + item.getQuantity() + "\nExpiry: " + item.getExpiryDate();
            itemDetails.setText(details);
            itemIcon.setImageResource(item.getIconResId());

            return convertView;
        }
    }


    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    }
