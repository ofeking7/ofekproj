package com.example.ofekrealpro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginS extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        FirebaseApp.initializeApp(this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        // Set up login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Check for empty fields
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(loginS.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Show loading indicator or disable button here if needed
                    loginUser(email, password);
                }
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(loginS.this, "Login successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to home activity
                            Intent intent = new Intent(loginS.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // Close the login activity
                        } else {
                            // If sign in fails, display a message to the user
                            Toast.makeText(loginS.this, "Authentication failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(loginS.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}