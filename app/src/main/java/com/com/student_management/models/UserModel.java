package com.com.student_management.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.com.student_management.entities.User;
import com.com.student_management.utils.HandlePassword;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserModel extends Model {
    private static final String TAG = "UserModel";
    private static final String USER_COLLECTION = "users";
    private static final String COUNTRY_CODE = "+84";
//    private DatabaseReference database;

    private PhoneAuthOptions options;
    //    private FirebaseAuth mAuth;
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
    public void login(String uuid, String email, String password, LoginCallBacks loginCallBacks) {
        Query query = database.child(USER_COLLECTION).child(uuid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    try {
                        boolean isMatch = HandlePassword.verifyPassword(password, user.getPassword());
                        if (isMatch & user.getEmail().equals(email)) {
                            loginCallBacks.onCompleted(user);
                        } else {
                            loginCallBacks.onFailure();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        loginCallBacks.onFailure();
                    }
                } else {
                    loginCallBacks.onFailure();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " + error.toString());
                loginCallBacks.onFailure();
            }
        });
    }

    public void create(String email, String name, String phone, String role, Integer age) {
        try {
            String uuid = UUID.randomUUID().toString();
            String password = email.split("@")[0];
            String hashedPassword = HandlePassword.hashPassword(password);
            ArrayList<Date> history = new ArrayList<>();
            User newUser = new User(uuid, name, age, phone, hashedPassword, email, role, "active", history);
            database.child(USER_COLLECTION).child(uuid).setValue(newUser.toMap())
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
        } catch (Exception e) {
            Log.e(TAG, "create: " + e.toString());
        }
    }

}
