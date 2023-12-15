package com.cs407.zoomfoods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cs407.zoomfoods.services.UserSessionService;

import java.util.Calendar;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText editTextNewItem, editTextQuantity;
    private Spinner spinnerCategory, spinnerUnit;
    private Button buttonSave, buttonFridge, buttonFreezer;
    private String storageType = "Fridge"; // default value
    private EditText editTextExpiryDate;
    private DatePickerDialog datePickerDialog;

    long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        UserSessionService userSessionService = UserSessionService.getInstance();
        userId = userSessionService.getUserId();
        checkLoggedIn();

        editTextNewItem = findViewById(R.id.editTextNewItem);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        buttonSave = findViewById(R.id.buttonSave);
        buttonFridge = findViewById(R.id.buttonFridge);
        buttonFreezer = findViewById(R.id.buttonFreezer);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);

        spinnerCategory.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);



        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                R.array.unit_array, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        editTextExpiryDate.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(AddProductActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> editTextExpiryDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            datePickerDialog.show();
        });

        buttonFridge.setOnClickListener(v -> storageType = "Fridge");
        buttonFreezer.setOnClickListener(v -> storageType = "Freezer");
        buttonSave.setOnClickListener(v -> onSaveClicked());

        Button buttonBackToFridge = findViewById(R.id.buttonBackToFridge);
        buttonBackToFridge.setOnClickListener(v -> backToFridge());
    }

    public void onStorageTypeClicked(View view) {
        Button buttonFridge = findViewById(R.id.buttonFridge);
        Button buttonFreezer = findViewById(R.id.buttonFreezer);



        if (view.getId() == R.id.buttonFridge) {
            storageType = "Fridge";

        } else if (view.getId() == R.id.buttonFreezer) {
            storageType = "Freezer";

        }
    }

    public void backToFridge(){
        Intent intent = new Intent(this, FridgeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int pos, long id){

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent){

    }

    private void checkLoggedIn() {
        if (userId == -1) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onSaveClicked(){
        Intent intent = new Intent(this, FridgeActivity.class);
        startActivity(intent);
    }
//
//    public void onSaveClicked() {
//        String item = editTextNewItem.getText().toString().trim();
//        String category = spinnerCategory.getSelectedItem().toString();
//        String quantity = editTextQuantity.getText().toString().trim();
//        String unit = spinnerUnit.getSelectedItem().toString();
//        String expiryDate = editTextExpiryDate.getText().toString().trim();
//
//        if (!item.isEmpty() && !category.isEmpty() && !quantity.isEmpty()
//                && !unit.isEmpty() && !storageType.isEmpty() && !expiryDate.isEmpty()) {
//
//            Intent returnIntent = new Intent();
//            returnIntent.putExtra("ITEM", item);
//            returnIntent.putExtra("CATEGORY", category);
//            returnIntent.putExtra("QUANTITY", quantity);
//            returnIntent.putExtra("UNIT", unit);
//            returnIntent.putExtra("STORAGE_TYPE", storageType);
//            returnIntent.putExtra("EXPIRY_DATE", expiryDate);
//
//            setResult(RESULT_OK, returnIntent);
//            finish(); // Closing the activity
//        } else {
//            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
//        }
//    }
    }