package com.example.myproject2;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Get the selected category from the intent
        String selectedCategory = getIntent().getStringExtra("selectedCategory");

        // Setup the Spinner for District
        Spinner districtSpinner = findViewById(R.id.District);
        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this,
                R.array.district_options, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(districtAdapter);

        // Setup the Spinner for Category and set the pre-selected category
        Spinner categorySpinner = findViewById(R.id.Category);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_options, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Set the selected category in the spinner
        if (selectedCategory != null) {
            int position = categoryAdapter.getPosition(selectedCategory);
            categorySpinner.setSelection(position);
        }

        // Setup the Spinner for Size (Kg/Liter/Piece)
        Spinner sizeSpinner = findViewById(R.id.Unit);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.size_options, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Setup the Submit Button to handle form submission
        Button submitButton = findViewById(R.id.buttonSubmit);
        submitButton.setOnClickListener(v -> {
            // Capture the values from form fields
            String name = ((EditText) findViewById(R.id.Name)).getText().toString();
            String phone = ((EditText) findViewById(R.id.PhoneNumber)).getText().toString();
            String district = districtSpinner.getSelectedItem().toString();
            String category = categorySpinner.getSelectedItem().toString();
            String itemName = ((EditText) findViewById(R.id.ItemName)).getText().toString();
            String price = ((EditText) findViewById(R.id.Price)).getText().toString();
            String size = sizeSpinner.getSelectedItem().toString();

            // Validate the fields (add more checks as necessary)
            if (name.isEmpty() || itemName.isEmpty() || price.isEmpty()) {
                // Show an error message
                Toast.makeText(RegistrationActivity.this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get the current date and format it
            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(currentDate);

            // Prepare data to be stored as a map
            Map<String, Object> item = new HashMap<>();
            item.put("name", name);
            item.put("phone", phone);
            item.put("district", district);
            item.put("category", category);
            item.put("itemName", itemName);
            item.put("price", price);
            item.put("size", size);
            item.put("date", formattedDate); // Only store formatted date
            if (category.equals("காய்கறிகள்")) {
                db.collection("காய்கறிகள்").add(item)
                        .addOnSuccessListener(documentReference ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Data saved successfully!"))
                        .addOnFailureListener(e ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Error saving data"));
            } else if (category.equals("மலர்கள்")) {
                db.collection("மலர்கள்").add(item)
                        .addOnSuccessListener(documentReference ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Data saved successfully!"))
                        .addOnFailureListener(e ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Error saving data"));
            } else if (category.equals("எண்ணெய் வகைகள்")) {
                db.collection("எண்ணெய் வகைகள்").add(item)
                        .addOnSuccessListener(documentReference ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Data saved successfully!"))
                        .addOnFailureListener(e ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Error saving data"));
            } else if (category.equals("பழங்கள்")) {
                db.collection("பழங்கள்").add(item)
                        .addOnSuccessListener(documentReference ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Data saved successfully!"))
                        .addOnFailureListener(e ->
                                ToastUtil.showCustomToast(RegistrationActivity.this, "Error saving data"));
            } else {
                // Notify the user if the selected category is unsupported
                ToastUtil.showCustomToast(RegistrationActivity.this, "Please select a valid category.");
            }
            clearForm();
        });
    }

    // Method to clear the form fields after submission
    private void clearForm() {
        ((EditText) findViewById(R.id.Name)).setText("");
        ((EditText) findViewById(R.id.PhoneNumber)).setText("");
        ((EditText) findViewById(R.id.ItemName)).setText("");
        ((EditText) findViewById(R.id.Price)).setText("");
        ((Spinner) findViewById(R.id.District)).setSelection(0);
        ((Spinner) findViewById(R.id.Category)).setSelection(0);
        ((Spinner) findViewById(R.id.Unit)).setSelection(0);
    }
}
