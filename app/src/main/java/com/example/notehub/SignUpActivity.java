package com.example.notehub;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {

    // Declare the UI elements
    EditText emailEditText, passwordEditText,confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Enable full-screen display
        setContentView(R.layout.activity_sign_up); // Set the layout to the sign-up screen layout

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        confirmPasswordEditText = findViewById(R.id.confirm_password_edit_text);
        createAccountBtn = findViewById(R.id.create_acc_btn);
        progressBar = findViewById(R.id.progress_bar);
        loginBtnTextView = findViewById(R.id.login_text_btn);

        // Set click listeners for the buttons.
        createAccountBtn.setOnClickListener(v-> createAccount());
        loginBtnTextView.setOnClickListener(v-> finish());

    }

    void createAccount(){
        // Get the values entered by the user in the input fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        // Validate the entered data
        boolean isValidated = validateData(email, password, confirmPassword);
        if(!isValidated) {
            return;
        }

        // Create account in firebase if data is valid
        createAccountInFirebase(email,password);

    }

    // creates the user account in firebase
    void createAccountInFirebase(String email, String password) {
        changeInProgress(true); // Change the UI to show progress

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance(); //interact with Firebase authentication.

        //create new user account with the provided email and password
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this,  // Change here
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        changeInProgress(false);
                        if (task.isSuccessful()) {
                            // Account creation is done
                            Util.showToast(SignUpActivity.this, "Successfully created account, Check email to verify");
                            firebaseAuth.getCurrentUser().sendEmailVerification();  // Send an email verification to the user
                            firebaseAuth.signOut(); // Logout user after account creation
                            finish();
                        } else {
                            // If account creation fail
                            Util.showToast(SignUpActivity.this, task.getException().getLocalizedMessage());
                        }
                    }
                }
        );
    }
    // Show the loading progress
    void changeInProgress(boolean inProgress) {
        if(inProgress){
             progressBar.setVisibility(View.VISIBLE);  // Show the prog bar,
             createAccountBtn.setVisibility(View.GONE); // Hide the "Create Account" button when the process is in progress
        } else {
            progressBar.setVisibility(View.GONE);  // Hide the progress bar,
            createAccountBtn.setVisibility(View.VISIBLE); // show the "Create Account" button when the process is finished
        }
    }

    // validates entered data
    boolean validateData(String email, String password, String confirmPassword) {

        // Check if the email is valid.
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is invalid");
            return false;
        }
        // Check if password is at least 8 characters long
        if (password.length()<8) {
            passwordEditText.setError("Password must be at least 8 characters long");
            return false;
        }
        // Check if the password and confirm password match
        if(!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords do not match. Please try again.");
            return false;
        }

        // Return true if all validation checks pass
        return true;
    }

}