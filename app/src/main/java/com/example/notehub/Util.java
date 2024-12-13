package com.example.notehub;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Util {

    // Method to show a short toast message on the screen
    static void showToast(Context context, String message) {
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionRefernceForNotes() {
        // Get the current logged-in user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return  FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");

    }

    // Method to convert a Timestamp to a formatted date string
    static String timestampToString(Timestamp timestamp) {
        return new SimpleDateFormat("MM/dd/yy").format(timestamp.toDate());
    }

}
