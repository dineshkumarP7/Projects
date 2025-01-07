package com.example.myproject2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText rname, remail, rusername, rpassword;
    private Button rsubmit;
    private TextView rlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialize views
        rname = findViewById(R.id.rname);
        remail = findViewById(R.id.remail);
        rusername = findViewById(R.id.rusername);
        rpassword = findViewById(R.id.rPassword);
        rsubmit = findViewById(R.id.rsubmit);
        rlogin = findViewById(R.id.rlogin);

        rsubmit.setOnClickListener(v -> {
            // Fetch input values inside the OnClickListener
            String name = rname.getText().toString().trim();
            String email = remail.getText().toString().trim();
            String username = rusername.getText().toString().trim();
            String password = rpassword.getText().toString().trim();

            // Check if any field is empty
            if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                ToastUtil.showCustomToast(this, "Please fill all fields");
                return;
            }

            // Create a new user map
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("username", username);
            user.put("password", password);

            // Add a new document with a generated ID
            db.collection("users")
                    .add(user)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            ToastUtil.showCustomToast(this, "Registered successfully");
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));

                        }
                    })
                    .addOnFailureListener(e -> ToastUtil.showCustomToast(this, "Registration failed"));
            clearForm();
        });
        rlogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish(); // Optional: Closes RegisterActivity
        });
    }

    private void clearForm() {
        ((EditText) findViewById(R.id.rname)).setText("");
        ((EditText) findViewById(R.id.remail)).setText("");
        ((EditText) findViewById(R.id.rusername)).setText("");
        ((EditText) findViewById(R.id.rPassword)).setText("");

    }
}


