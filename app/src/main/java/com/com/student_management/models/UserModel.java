package com.com.student_management.models;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.com.student_management.constants.Roles;
import com.com.student_management.entities.User;
import com.com.student_management.utils.FormatDateTime;
import com.com.student_management.utils.HandlePassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserModel extends Model {
    private static final String TAG = "UserModel";
    private static final String USER_COLLECTION = "users";
    private static final String COUNTRY_CODE = "+84";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String vetificationId = "";

    public interface UserCallBacks {
        void onCallback(User user);
    }

    public interface onAddConfigListener {
        void onCompleted();

        void onFailure();
    }

    public interface onGetConfigListener {
        void onCompleted(List<Map<String, Object>> config);

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

    public UserModel() {
        super();
    }

    public UserModel(DatabaseReference database) {
        super(database);
    }

    // login with email and password
    public void login(String email, String password, LoginCallBacks loginCallBacks) {
        Log.e(TAG, "login: in login without uuid");
        Query query = database.child(USER_COLLECTION);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                boolean isFound = false;
//                retrieve data from user collection
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JSONObject userObject = new JSONObject((Map) dataSnapshot.getValue());
                    HashMap<String, Object> userMap = new HashMap<>();
                    Iterator<String> keys = userObject.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        try {
                            userMap.put(key, userObject.get(key));
                            Log.d(TAG, "onDataChange: " + key.equals("history") + ": " + userObject.get(key).getClass().getName());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    user = dataSnapshot.getValue(User.class);
                    Log.e(TAG, "onDataChange: " + userMap.toString());
                       user = new User(userMap);
                    Log.e(TAG, "onDataChange: " + user.toString());
                    if (user != null) {
                        try {
                            boolean isMatch = HandlePassword.verifyPassword(password, user.getPassword());
                            Log.e(TAG, "onDataChange: password User: " + user.toString() + "; pass: " + user.getPassword() + "; is match: " + isMatch + "; map: " + user.toMap().toString());
                            if (user.getEmail().equals(email) && isMatch) {
//                                updateHistory(user.getUuid());
                                loginCallBacks.onCompleted(user);
                                isFound = true;
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loginCallBacks.onFailure();
                        }

                    }
                }
                if (!isFound) {
                    loginCallBacks.onFailure();
                } else {
                    Log.e(TAG, "onDataChange: user found: " + user.toString());
                    loginCallBacks.onCompleted(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.toString());
                loginCallBacks.onFailure();
            }
        });
    }

    public void create(String email, String name, String phone, String role, Long age) {
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
                                Log.e(TAG, "create: " + newUser.toString());
                                database.child(USER_COLLECTION).child(user.getUid()).setValue(newUser.toMap())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Log.e(TAG, "create: success");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: write failed ");
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "authUserByFirebaseWithEmailAndPassword: create user failed");
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "authUserByFirebaseWithEmailAndPassword: create user failed");
                    });
        } catch (Exception e) {
            Log.e(TAG, "create: " + e.toString());
        }
    }


    public void addUserByGoogle(String uuid, String name, String email, Roles role, Long age, Uri avatar, String status) {
        ArrayList<String> history = new ArrayList<>();
        history.add(FormatDateTime.formatDateTime());
        User user = new User(uuid, name, age, email, role.name(), avatar, status, history);

        database.child(USER_COLLECTION).child(uuid).setValue(user.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: add user by google success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void getAllUsers(final OnUserRetrievedListener listener) {
        database.child(USER_COLLECTION).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    JSONObject jsonObject = new JSONObject((java.util.Map) dataSnapshot.getValue());
                    User user = new User();
                    String uuid = dataSnapshot.getKey();
                    String name = jsonObject.optString("name");
                    Roles role = Roles.valueOf(jsonObject.optString("role"));
                    String status = jsonObject.optString("status");

                    user.setUuid(uuid);
                    user.setName(name);
                    user.setRole(role.name());
                    user.setStatus(status);

                    users.add(user);

                }
                listener.onCompleted(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: user not exist in system");
            }
        });
    }

    public void isExistUser(String email, CheckUserExistCallBacks callbacks) {
        Query query = database.child(USER_COLLECTION);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = null;
                boolean isExist = false;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Map<String, Object> userMap = (Map<String, Object>) dataSnapshot.getValue();

                    if (userMap.get("email").equals(email)) {
                        isExist = true;
                        break;
                    }

                }
                callbacks.onNotFound();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.toString());
                callbacks.onNotFound();
            }
        });

    }

    public void getRole(String uuid, final UserCallBacks userCallBacks) {
        Query query = database.child(USER_COLLECTION).child(uuid);
        String role = "";
        query.addListenerForSingleValueEvent(new ValueEventListener() {
        String role = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    role = user.getRole();
                }
                userCallBacks.onCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.toString());
                userCallBacks.onCallback(null);
            }
        });
    }

    private void updateHistory(String uuid) {
        database.child(USER_COLLECTION).child(uuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = new User((HashMap<String, Object>) snapshot.getValue());
                ArrayList<String> history = user.getHistory();
                history.add(FormatDateTime.formatDateTime());
                database.child(USER_COLLECTION).child(uuid).child("history").setValue(history);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.toString());
            }
        });
    }

}
