package com.example.personalizedlearningapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RegisterActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private EditText etUsername, etEmail, etConfirmEmail, etPassword, etConfirmPassword, etPhone;
    private DatabaseHelper dbHelper;
    private String copiedImagePath = null;

    // Image picker launcher for selecting avatar
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    ivProfile.setImageURI(uri);
                    copiedImagePath = copyImageToInternalStorage(uri); // Save avatar to internal storage
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        // Bind views
        ivProfile = findViewById(R.id.ivProfile);
        ImageView ivAddImage = findViewById(R.id.ivAddImage);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etConfirmEmail = findViewById(R.id.etConfirmEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        Button btnRegister = findViewById(R.id.btnRegister);

        // Launch image picker on avatar add icon click
        ivAddImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(intent);
        });

        // Handle register button click
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String confirmEmail = etConfirmEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            // Input validation
            if (username.isEmpty()) {
                Toast.makeText(this, "Please create a username", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Please input your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Please input your password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(this, "Please input your phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!email.equals(confirmEmail) || !password.equals(confirmPassword)) {
                Toast.makeText(this, "Email or Password does not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Optional avatar — fallback to null if not selected
            String avatarPath = (copiedImagePath != null) ? copiedImagePath : null;

            boolean success = dbHelper.registerUser(username, email, password, phone, avatarPath);
            if (success) {
                int userId = dbHelper.getUserIdByUsername(username); // Retrieve user ID

                Intent intent = new Intent(this, InterestsActivity.class);
                intent.putExtra("user_id", userId); // Pass user ID to next screen

                Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Copy selected image to internal storage and return its path
    private String copyImageToInternalStorage(Uri sourceUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(sourceUri);
            File file = new File(getFilesDir(), "avatar_" + System.currentTimeMillis() + ".jpg");
            OutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            return file.getAbsolutePath(); // For saving in database
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
