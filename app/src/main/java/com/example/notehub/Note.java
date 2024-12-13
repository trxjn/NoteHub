package com.example.notehub;

import com.google.firebase.Timestamp;

public class Note {

    // Data variables for note
    String title;
    String content;
    Timestamp timestamp;


    // Constructor needed for Firestore to create empty Note object
    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
