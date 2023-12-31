package com.com.student_management;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.com.student_management.authentication.LoginActivity;
import com.com.student_management.constants.App;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acitvity);
        FirebaseApp.initializeApp(this);
        userModel = new UserModel();

//        run main activity after 3 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
                SharedPreferences preferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                boolean isFirstTime = preferences.getBoolean("firstTime", true);
                SharedPreferences uuidPref = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                String uuid = uuidPref.getString(App.SHARED_PREFERENCES_UUID, null);
                if (isFirstTime) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("firstTime", false);
                    editor.commit();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
//                    check if user was signed in
                    if (uuid != null) {
                        Log.e(TAG, "run: uuid found " + uuid);
                        setRole(uuid);
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
//                        finish();
                    } else {
                        Log.e(TAG, "run: uuid not found " + uuid);
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
//                        finish();
                    }
                }
            }
        }, 1500);
    }

    private void setRole(String uuid) {
        userModel.getUser(uuid, new UserModel.UserCallBacks() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("role", user.getRole());
                    editor.apply();
                }
            }
        });
    }
}