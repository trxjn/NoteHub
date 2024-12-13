package com.example.notehub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context; // Start new activities

    // Constructor to create adapter
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        // Set note title, content, timestamp on the view holder
        holder.titleTextView.setText(note.title);
        holder.contentTextView.setText(note.content);
        holder.timestampTextView.setText(Util.timestampToString(note.timestamp));

        // Open NoteDetailActivity.class when item is click
        holder.itemView.setOnClickListener((v)-> {
            Intent intent = new Intent(context, NoteDetailActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);

            // Get document ID
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docID",docId);
            context.startActivity(intent);
        });


    }

    // Creates new view holder for each note item
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        // Display the title, content, and timestamp
        TextView titleTextView, contentTextView, timestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            // Find views by their ID in the layout file -> show notes
            titleTextView = itemView.findViewById(R.id.title_text_view);
            contentTextView = itemView.findViewById(R.id.content_text_view);
            timestampTextView = itemView.findViewById(R.id.timestamp_text_view);
        }
    }
}
