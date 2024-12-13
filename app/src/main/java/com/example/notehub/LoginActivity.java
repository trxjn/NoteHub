package com.example.notehub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // Declare the UI elements
    EditText emailEditText, passwordEditText;
    Button loginAccountBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        loginAccountBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);
        createAccountBtnTextView = findViewById(R.id.create_account_text_btn);


        loginAccountBtn.setOnClickListener((v)-> loginUser() );
        createAccountBtnTextView.setOnClickListener((v)->startActivity(new Intent(LoginActivity.this,SignUpActivity.class)) );
    }

    void loginUser() {
        // Get the values entered by the user in the input fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        // Validate the entered data
        boolean isValidated = validateData(email, password);
        if(!isValidated) {
            return;
        }

        // Login in firebase if data is valid
        loginAccountInFirebase(email,password);

    }

    //interact with Firebase authentication.
    void loginAccountInFirebase(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        changeInProgress(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgress(false);
                if(task.isSuccessful()){

                    // If login successful
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        //Go to the Main Activity
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));

                    } else {
                        Util.showToast(LoginActivity.this,"Email verification pending. Please check your email.");
                    }

                } else {
                    // If login failed
                    Util.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });
    }

    void changeInProgress(boolean inProgress) {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);  // Show the prog bar,
            loginAccountBtn.setVisibility(View.GONE); // Hide the "Create Account" button when the process is in progress
        } else {
            progressBar.setVisibility(View.GONE);  // Hide the progress bar,
            loginAccountBtn.setVisibility(View.VISIBLE); // show the "Create Account" button when the process is finished
        }
    }

    // validates entered data
    boolean validateData(String email, String password) {

        // Check if the email is valid
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        // Check if password is at least 8 characters long
        if (password.length()<8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            return false;
        }

        // Return true if all validation checks pass
        return true;
    }
}