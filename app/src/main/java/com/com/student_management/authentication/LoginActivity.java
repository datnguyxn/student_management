package com.com.student_management.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.com.student_management.MainActivity;
import com.com.student_management.R;
import com.com.student_management.constants.App;
import com.com.student_management.constants.Roles;
import com.com.student_management.entities.User;
import com.com.student_management.models.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText edtEmail, edtPassword;
    private Button btnLogin;
    private CheckBox cbRemember;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private EmailAuthCredential credential;
    private UserModel userModel;
    private String uuid = "";
    private String code;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        getDataIntent();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: login");
                handleLogin();
            }
        });
    }

    private void init() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        cbRemember = findViewById(R.id.cbRememberMe);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userModel = new UserModel(firebaseFirestore);

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        uuid = sharedPreferences.getString("uuid", "");
    }

    private void getDataIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            code = intent.getStringExtra("code");
        }
    }

    private void handleLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtEmail.setError("Email is required!");
            edtEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            edtPassword.setError("Password is required!");
            edtPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters!");
            edtPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(uuid)) {
            Log.d(TAG, "handleLogin: login without uuid " + uuid);
            userModel.login(email, password, new UserModel.LoginCallBacks() {
                @Override
                public void onCompleted(User user) {
                    if (user != null) {
                        Log.e(TAG, "onCompleted: Login success " + user.getEmail());
                        handleAuthEmailUseFirebase(email, password);
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure() {
                    Log.e(TAG, "onFailure: Login failed");
                    Toast.makeText(LoginActivity.this, "Your account was locked or have somethings wrong!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleAuthEmailUseFirebase(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "handleAuthEmailUseFirebase: success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d(TAG, "onCompleted: login with firebase success");
                        if (cbRemember.isChecked()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uuid", user.getUid());
                            editor.apply();
                        }
                        SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uuid", user.getUid());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e(TAG, "onCompleted: login with firebase failed");
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}