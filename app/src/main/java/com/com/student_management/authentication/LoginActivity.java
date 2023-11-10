package com.com.student_management.authentication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private TextInputEditText edtEmail, edtPassword;
    private Button btnLogin, btnLoginWithGG;
    private CheckBox cbRemember;
    private FirebaseAuth mAuth;
    private PhoneAuthOptions options;
    GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference database;
    private Dialog dialog;
    private UserModel userModel;
    private String uuid = "";
    private String code;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
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
                String uuid = UUID.randomUUID().toString();
                Log.d(TAG, "onClick: uuid " + uuid);
//                handleLogin();
            }
        });

        btnLoginWithGG.setOnClickListener(v -> {
            Log.d(TAG, "onClick: login with google");
            handleLoginGoogle();
        });
    }

    private void init() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginWithGG = findViewById(R.id.btnLoginWithGG);
        cbRemember = findViewById(R.id.cbRememberMe);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        userModel = new UserModel(database);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        dialog = new Dialog(this);

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

        if (!TextUtils.isEmpty(uuid)) {
            Log.e(TAG, "handleLogin: login with uuid " + uuid);
            userModel.login(uuid, email, password, new UserModel.LoginCallBacks() {
                @Override
                public void onCompleted(User user) {
                    if (user != null) {
                        Log.e(TAG, "onCompleted: Login success " + user.getEmail());
                        SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("uuid", user.getUuid());
                        editor.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure() {
                    Log.e(TAG, "onFailure: Login failed");
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void handleLoginGoogle() {
        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        handle result of login with google intent
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "onActivityResult: " + account.getId());
                handleAuthGoogleUseFirebase(account.getIdToken());
//                get userId
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAuthGoogleUseFirebase(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "handleAuthGoogleUseFirebase: login with google success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            ArrayList<LocalDateTime> history = new ArrayList<>();
                            history.add(LocalDateTime.now());
                            Log.d(TAG, "handleAuthGoogleUseFirebase: " + user.getDisplayName());
                            userModel.addUserByGoogle(user.getUid(), user.getDisplayName(), user.getEmail(), Roles.MANEGER, 20, user.getPhotoUrl(), "active", history);
                            if (cbRemember.isChecked()) {
                                SharedPreferences sharedPreferences = getSharedPreferences(App.SHARED_PREFERENCES_USER, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("uuid", user.getUid());
                                editor.apply();
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e(TAG, "handleAuthGoogleUseFirebase: login with google failed");
                        Toast.makeText(LoginActivity.this, "Login with google failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "handleAuthGoogleUseFirebase: login with google failed");
                    Toast.makeText(LoginActivity.this, "Login with google failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }
}