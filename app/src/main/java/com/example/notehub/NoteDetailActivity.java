package com.example.notehub;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailActivity extends AppCompatActivity {

    EditText titleEditText, contentEditText;
    ImageButton saveNoteBtn;
    TextView pageTitleTextView; // Show page title
    String title,content,docID;  // Variables to store note data
    boolean isEditMode = false; // Edit indicator for existing note
    Button deleteNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note_detail);

        // Initialize
        titleEditText = findViewById(R.id.note_title);
        saveNoteBtn = findViewById(R.id.save_note_btn);
        contentEditText = findViewById(R.id.content_text);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteNoteBtn = findViewById(R.id.delete_note_btn);

        //Get data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docID = getIntent().getStringExtra("docID");

        // Check if in edit mode
        if(docID!=null && !docID.isEmpty()) {
            isEditMode = true;
        }

        // Set the edit text fields with the existing note data
        titleEditText.setText(title);
        contentEditText.setText(content);

        // If in edit mode, change the title and show the delete button
        if(isEditMode) {
            pageTitleTextView.setText("Edit Note");
            deleteNoteBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v)-> saveNote());
        deleteNoteBtn.setOnClickListener((v)-> deleteNoteFromFirebase());

    }

    // Method to SAVE/CREATE a new or updated note
    void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        // Check if the title is empty
        if(noteTitle==null || noteTitle.isEmpty() ){
            titleEditText.setError("Input title");
            return;
        }

        // CREATE a new note
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    // Method to save the note to Firestore
    void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;
        if (isEditMode) {

            // Update note after edited
            documentReference = Util.getCollectionRefernceForNotes().document(docID);
        } else {

            // Create new note
            documentReference = Util.getCollectionRefernceForNotes().document();
        }

        // Save the note and show a success or failure message
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // If successfully note is added
                    Util.showToast(NoteDetailActivity.this, "Note saved successfully.");
                    finish();

                } else {
                    Util.showToast(NoteDetailActivity.this, "Failed to save the note.");


                }

            }

        });
    }

    // Method to DELETE a note from Firestore
    void deleteNoteFromFirebase() {
        DocumentReference documentReference;
            documentReference = Util.getCollectionRefernceForNotes().document(docID);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Note deleted successfully
                    Util.showToast(NoteDetailActivity.this, "Note deleted");
                    finish();

                } else {
                    Util.showToast(NoteDetailActivity.this, "Failed to delete the note");

                }

            }

        });

    }


}
