# NoteHub - Android App

A simple note-taking app built with **Android Studio** that allows users to authenticate using **Firebase Authentication** and store their notes in **Firebase Firestore**. The app provides full CRUD (Create, Read, Update, Delete) functionalities for managing notes.

## Features

- **Firebase Authentication**: Secure login with Firebase Authentication (email/password).
- **CRUD Operations**: 
  - **Create**: Add new notes.
  - **Read**: View all your notes.
  - **Update**: Edit existing notes.
  - **Delete**: Remove notes.
- **Firestore Database**: Store and sync notes in real-time with Firebase Firestore.
- **User-Specific Notes**: Each user’s notes are stored separately and are accessible only after login.

## Installation

Follow these steps to set up the project on your local machine.

### Prerequisites

- **Android Studio**: [Download Android Studio](https://developer.android.com/studio)
- **Firebase Project**: Set up a Firebase project to use Authentication and Firestore.
  
### Setting Up Firebase

1. Go to [Firebase Console](https://console.firebase.google.com/).
2. **Create a new project** or select an existing project.
3. Enable **Firebase Authentication**:
   - Go to **Authentication** > **Sign-in method**.
   - Enable **Email/Password** login method.
4. Set up **Firestore Database**:
   - Go to **Firestore Database** in the Firebase Console.
   - Create a Firestore database and select **Start in test mode** for development (ensure you change security rules before production).
5. **Download Firebase Configuration File**:
   - For **Android**, download the `google-services.json` file from your Firebase Console and place it in your `app/` directory.

### Clone the Repository

1. Clone this repository to your local machine:
   ```bash
   git clone https://github.com/trxjn/NoteHub.git
