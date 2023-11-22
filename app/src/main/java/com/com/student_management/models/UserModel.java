package com.com.student_management.models;

import android.util.Log;


import androidx.annotation.NonNull;

import com.com.student_management.entities.User;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.HandlePassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.Query;
import com.google.firebase.firestore.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class UserModel extends Model {
    private static final String TAG = "UserModel";
    private static final String USER_COLLECTION = "users";

    public interface UserCallBacks {
        void onCallback(User user);
    }

    public interface OnUserUpdatedListener {
        void onCompleted();

        void onFailure();
    }

    public interface OnUserChangedListener {
        void onCompleted(User user);

        void onFailure();
    }

    public interface OnUserRetrievedListener {
        void onCompleted(ArrayList<User> users);
    }

    public interface LoginCallBacks {
        void onCompleted(User user);

        void onFailure();
    }

    public interface CheckUserExistCallBacks {
        void onExist();

        void onNotFound();
    }

    public interface isDeletedCallBacks {
        boolean onDeleted();
    }

    public interface isLockedCallBacks {
        boolean onLocked();
    }

    public UserModel() {
        super();
    }


    public UserModel(FirebaseFirestore firebaseFirestore) {
        super(firebaseFirestore);
    }

    // login with email and password
    public void login(String email, String password, LoginCallBacks loginCallBacks) {
        Log.d(TAG, "login: in login without uuid");
        firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                Log.d(TAG, documentSnapshot.getId() + " => " + documentSnapshot.getData());
                                User user = documentSnapshot.toObject(User.class);
                                if (user != null) {
                                    try {
                                        boolean isMatch = HandlePassword.verifyPassword(password, user.getPassword());
                                        if (isMatch) {
                                            Log.e(TAG, "onDataChange: password User: " + user.toString() + "; pass: " + user.getPassword() + "; is match: " + isMatch + "; map: " + user.toMap().toString());
                                            ArrayList<String> history = user.getHistory();
                                            history.add(FormatDateTime.formatDateTime());
                                            firebaseFirestore.collection(USER_COLLECTION).document(user.getUuid()).update("history", history);
                                            if (user.getStatus().equals("active"))
                                                loginCallBacks.onCompleted(user);
                                            else
                                                loginCallBacks.onFailure();
                                        } else {
                                            loginCallBacks.onFailure();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    loginCallBacks.onFailure();
                                }
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loginCallBacks.onFailure();
                    }
                });
    }

    public void create(String email, String name, String phone, String role, Long age, UserCallBacks userCallBacks) {
        try {
            String password = email.split("@")[0];
            String hashedPassword = HandlePassword.hashPassword(password);
            ArrayList<String> history = new ArrayList<>();
            history.add(FormatDateTime.formatDateTime());
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "authUserByFirebaseWithEmailAndPassword: create user success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Log.d(TAG, "authUserByFirebaseWithEmailAndPassword: " + user.getUid());
                                User newUser = new User(user.getUid(), name, age, phone, hashedPassword, email, role, "active", history);
                                firebaseFirestore.collection(USER_COLLECTION).document(user.getUid()).set(newUser.toMap())
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "onSuccess: create user success");
                                                Log.d(TAG, "create: " + newUser.toString());
                                                userCallBacks.onCallback(newUser);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "create: " + e.toString());
                                                userCallBacks.onCallback(null);
                                            }
                                        });

                            }
                        } else {
                            Log.e(TAG, "authUserByFirebaseWithEmailAndPassword: create user failed");
                            userCallBacks.onCallback(null);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "authUserByFirebaseWithEmailAndPassword: create user failed");
                        userCallBacks.onCallback(null);
                    });
        } catch (Exception e) {
            Log.e(TAG, "create: " + e.toString());
            userCallBacks.onCallback(null);
        }
    }

    public void getAllUsers(final OnUserRetrievedListener listener) {
        firebaseFirestore.collection(USER_COLLECTION).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    ArrayList<User> users = new ArrayList<>();

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            User user = documentSnapshot.toObject(User.class);
                            users.add(user);
                        }
                        listener.onCompleted(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onCompleted(null);
                    }
                });
    }

    public void isExistUser(String email, CheckUserExistCallBacks callbacks) {
        Query query = firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("email", email);
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    boolean isFound = false;

                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        User user = null;
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            user = documentSnapshot.toObject(User.class);
                            Log.e(TAG, "onSuccess: " + user.toString());
                            if (user != null) {
                                if (user.getEmail().equals(email)) {
                                    callbacks.onExist();
                                    isFound = true;
                                    break;
                                }
                            }
                        }
                        if (!isFound) {
                            callbacks.onNotFound();
                        } else {
                            Log.e(TAG, "onDataChange: user found: " + user.toString());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void getRole(String uuid, final UserCallBacks userCallBacks) {
        firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("uuid", uuid)
                .get()
                .addOnCompleteListener(task -> {
                    String role = "";
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                role = user.getRole();
                                userCallBacks.onCallback(user);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onCancelled: " + e.toString());
                    userCallBacks.onCallback(null);
                });

    }

    public void getUser(String uuid, final UserCallBacks userCallBacks) {
        firebaseFirestore.collection(USER_COLLECTION).whereEqualTo("uuid", uuid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                userCallBacks.onCallback(user);
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "onCancelled: " + e.toString());
                    userCallBacks.onCallback(null);
                });
    }

    public void updateAvatar(String uuid, String toString) {
        firebaseFirestore.collection(USER_COLLECTION).document(uuid).update("avatar", toString)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: update avatar success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG + "updateAvatar", e.toString());
                    }
                });
    }

    public void updateUser(String uuid, Map<String, Object> values, OnUserUpdatedListener
            listener) {
        Log.e(TAG, "updateUser: " + values.toString());
        firebaseFirestore.collection(USER_COLLECTION).document(uuid).update(values)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: update user success");
                        listener.onCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailure();
                        Log.e(TAG + "updateUser", e.toString());
                    }
                });
    }

    public void deleteUser(String uuid, isDeletedCallBacks listener) {
        Log.e(TAG, "deleteUser: " + uuid);
        firebaseFirestore.collection(USER_COLLECTION).document(uuid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: delete user success");
                        listener.onDeleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG + "deleteUser", e.toString());
                    }
                });
    }

    public void setLockUser(String uuid, String status, isLockedCallBacks listener) {
        Log.e(TAG, "setLockUser: " + uuid);
        firebaseFirestore.collection(USER_COLLECTION).document(uuid).update("status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "onSuccess: lock user success");
                        listener.onLocked();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG + "setLockUser", e.toString());
                    }
                });
    }

}
