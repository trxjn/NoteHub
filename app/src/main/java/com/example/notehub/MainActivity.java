package com.example.notehub;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    // Declare instances
    FloatingActionButton addNoteBtn; // Button to add new note
    RecyclerView recyclerView; // List to show notes
    ImageButton menuBtn; // Button to show menu
    NoteAdapter noteAdapter; // Manage RecyclerView data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize elements
        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recycler);
        menuBtn = findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,NoteDetailActivity.class)));
        menuBtn.setOnClickListener((v)-> showMenu());
        setupRecyclerView();

    }
    // Display popup menu
    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Logout"); // Add logout option on the menu
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout") {
                    FirebaseAuth.getInstance().signOut(); // Logout user
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    // Setup list to display notes
    void setupRecyclerView() {
        Query query = Util.getCollectionRefernceForNotes().orderBy("timestamp",Query.Direction.DESCENDING); // Display notes from firestore in order by timestamp
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start tracking for updates
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop tracking updates if screen not visible
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh notes list
        noteAdapter.notifyDataSetChanged();
    }

}