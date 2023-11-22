package com.com.student_management.models;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Model {
    public static final String TAG = "Model";

    protected FirebaseAuth mAuth;
    protected DatabaseReference database;
    protected FirebaseStorage storage;
    protected FirebaseFirestore firebaseFirestore;
    protected StorageReference storageRef;

    public Model() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        database.keepSynced(true);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public Model(DatabaseReference database) {
        mAuth = FirebaseAuth.getInstance();
        this.database = database;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public Model(FirebaseFirestore firebaseFirestore) {
        mAuth = FirebaseAuth.getInstance();
        this.firebaseFirestore = firebaseFirestore;
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public FirebaseAuth getAuth() {
        return mAuth;
    }

    public void setAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }
}
